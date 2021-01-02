package com.example.authapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfile extends AppCompatActivity {

    private TextView UserInfo;
    private EditText newFullName, newEmail, newAge, newPassword;
    private Button update;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");

        newFullName = (EditText) findViewById(R.id.fullNameEditProfile);
        newEmail = (EditText) findViewById(R.id.emailEditProfile);
        newAge = (EditText) findViewById(R.id.ageEditProfile);
        newPassword = (EditText) findViewById(R.id.passwordEditProfile);
        update = (Button) findViewById(R.id.updateProfileBtn);
        progressBar = (ProgressBar) findViewById(R.id.progressBarUpdateProfile);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser == null) {
                    Toast.makeText(EditProfile.this, "User info doesn't exist", Toast.LENGTH_LONG).show();
                } else {
                    if(!newFullName.getText().toString().isEmpty()){
                        reference.child(firebaseUser.getUid()).child("fullName").setValue(newFullName.getText().toString().trim());
                    }
                    if(!newEmail.getText().toString().isEmpty()){
                        reference.child(firebaseUser.getUid()).child("email").setValue(newEmail.getText().toString().trim());
                    }
                    if(!newAge.getText().toString().isEmpty()){
                        reference.child(firebaseUser.getUid()).child("age").setValue(newAge.getText().toString().trim());
                    }
                    if(!newPassword.getText().toString().isEmpty()){
                        reference.child(firebaseUser.getUid()).child("password").setValue(newPassword.getText().toString().trim());
                    }
                    Toast.makeText(EditProfile.this, "User's info has been updated", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
                startActivity(new Intent(EditProfile.this, MainActivity.class));
            }
        });
    }
}