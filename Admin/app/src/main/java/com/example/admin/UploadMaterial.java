package com.example.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class UploadMaterial extends AppCompatActivity {

    private TextView notification;
    private Uri pdfUri;// uri are actually URLs that are meant for local storage

    private FirebaseStorage storage;// used for uploading files
    private FirebaseDatabase database;// used for store URLs of uploading files
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_material);

        Toolbar toolbar = findViewById(R.id.uploadToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Upload Material");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        storage = FirebaseStorage.getInstance();// return an object of Firebase Storage
        database = FirebaseDatabase.getInstance();// return an object of Firebase Database

        Button selectFile = findViewById(R.id.select_file_button);
        Button upload = findViewById(R.id.upload_file_button);
        notification = findViewById(R.id.uploadTextView);

        selectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(UploadMaterial.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
                    selectPdf();
                }
                else
                    ActivityCompat.requestPermissions(UploadMaterial.this, new String [] {Manifest.permission.READ_EXTERNAL_STORAGE},9);
            }
        });

        upload.setOnClickListener(v -> {
            if(pdfUri != null)// the user selected the file
                uploadFile(pdfUri);
            else
                Toast.makeText(UploadMaterial.this, "select a file", Toast.LENGTH_SHORT).show();
        });
    }

    private void uploadFile(Uri pdfUri) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file...");
        progressDialog.setProgress(0);
        progressDialog.show();

        

        String fileName = System.currentTimeMillis() + "";
        StorageReference storageReference = storage.getReference();// return root path

        storageReference.child("Uploads").child(fileName).putFile(pdfUri).
                addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String url = Objects.requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata()).getReference()).getDownloadUrl().toString();// return the url of uploaded file
                        // store the url in realtime database
                        DatabaseReference reference = database.getReference();// return the path to root
                        reference.child(fileName).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                    Toast.makeText(UploadMaterial.this, "File successfully uploaded", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(UploadMaterial.this, "File not successfully uploaded", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadMaterial.this, "File not successfully uploaded", Toast.LENGTH_SHORT).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                // track the progress of our upload
                int currentProgress = (int) (100 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if(requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            selectPdf();
        }
        else
            Toast.makeText(UploadMaterial.this, "please provide permission..", Toast.LENGTH_SHORT).show();
    }

    private void selectPdf()
    {
        //to offer user to select a file using file manager
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);// to fetch files
        startActivityForResult(intent, 86);
    }

    @SuppressLint({"MissingSuperCall", "SetTextI18n"})
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //check whether user has selected a file or not

        if(requestCode == 86 && resultCode == RESULT_OK && data != null)
        {
            pdfUri = data.getData();// return the uri of selected file
            notification.setText("A file is selected : "+ data.getData().getLastPathSegment());
        }
        else
        {
            Toast.makeText(UploadMaterial.this, "please select a file", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            UploadMaterial.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(UploadMaterial.this,AdminSelectPage.class);
        startActivity(intent);
        startActivityForResult(intent, 86);
    }
}