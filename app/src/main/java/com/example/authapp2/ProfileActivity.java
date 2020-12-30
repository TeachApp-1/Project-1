package com.example.authapp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.usage.StorageStats;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

public class ProfileActivity extends AppCompatActivity {

    //Objects for the ProfileActivity
    private FirebaseAuth FbAuth;
    private FirebaseUser FbUser;
    private DatabaseReference databaseReference;
    private String userID;
    private Button logout,editProfile,deleteAccount,updateProfilePicBtn;
    private ProgressBar progressBar;
    private ImageView profilePic;

    //func to be called as the ProfileActivity starts
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Setting values to objects
        ImageView profilePic = (ImageView)findViewById(R.id.profilePicure);
        Button logout = (Button)findViewById(R.id.logout);
        Button editProfile = (Button)findViewById(R.id.editProfileBtn);
        Button deleteAccount = (Button)findViewById(R.id.deleteUser);
        Button updateProfile = (Button)findViewById(R.id.updateProfilePicBtn);
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBarProfileActivity);

        FbAuth = FirebaseAuth.getInstance();
        FbUser = FbAuth.getCurrentUser();


        //Delete account function, to be called with a button
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ProfileActivity.this);
                dialog.setTitle("Are you sure?");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressBar.setVisibility(View.VISIBLE);
                        FbUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                   Toast.makeText(ProfileActivity.this,"Account Deleted",Toast.LENGTH_LONG).show();
                                    FirebaseAuth.getInstance().signOut();
                                    startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                                }
                                else
                                {
                                    Toast.makeText(ProfileActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                }
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                });

                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,UpdateProfilePicture.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this,MainActivity.class));
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,EditProfile.class));
            }
        });
        FbUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = FbUser.getUid();

        final TextView greetingTextView = (TextView)findViewById(R.id.greeting);
        final TextView fullNameTextView = (TextView)findViewById(R.id.fullnameView2);
        final TextView emailTextView = (TextView)findViewById(R.id.emailView2);
        final TextView ageTextView = (TextView)findViewById(R.id.ageView2);

        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String fullName = userProfile.fullName;
                    String email = userProfile.email;
                    String age = userProfile.age;

                    fullNameTextView.setText(fullName);
                    emailTextView.setText(email);
                    ageTextView.setText(age);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
            }
        });
    }
}