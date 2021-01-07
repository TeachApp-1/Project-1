package com.example.admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AdminSelectPage extends AppCompatActivity {

    private Button uploadQuiz, uploadMaterial;
    private TextView title;

    private FirebaseStorage firebaseStorage;
    private StorageReference mStorageRef;
    public Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_select_page);

        uploadMaterial = findViewById(R.id.upload_button);
        uploadQuiz = findViewById(R.id.category_button);
        title = findViewById(R.id.admin_select_title);

        Typeface typeface = ResourcesCompat.getFont(this, R.font.blacklist);
        title.setTypeface(typeface);

        uploadQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminSelectPage.this, CategoryActivity.class);
                startActivity(intent);
            }
        });

        uploadMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminSelectPage.this, UploadMaterial.class);
                startActivity(intent);
            }
        });
        }
    }
