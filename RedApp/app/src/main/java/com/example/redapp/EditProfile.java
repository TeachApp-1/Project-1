package com.example.redapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditProfile extends AppCompatActivity {

    private TextView UserInfo;
    private EditText newFullName, newEmail, newSchool, newClassroom;
    private Button update;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    DocumentReference firebaseUserRef;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("עריכת משתמש");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        newFullName = (EditText) findViewById(R.id.fullNameEditProfile);
        newEmail = (EditText) findViewById(R.id.emailEditProfile);
        newSchool = (EditText) findViewById(R.id.schoolEditProfile);
        newClassroom = (EditText) findViewById(R.id.classroomEditProfile);
        update = (Button) findViewById(R.id.updateProfileBtn);
        progressBar = (ProgressBar) findViewById(R.id.progressBarUpdateProfile);

        update.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                editUserData();
            }
        });
    }

    private void editUserData()
    {
        firebaseUserRef = firestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid());
        Map<String,Object> updatedUserData = new HashMap<>();
        if (firebaseUserRef == null) {
            Toast.makeText(EditProfile.this, "User info doesn't exist", Toast.LENGTH_LONG).show();
        } else {
            if(!newFullName.getText().toString().isEmpty())
            {
                updatedUserData.put("Name",newFullName.getText().toString().trim());
            }
            if(!newEmail.getText().toString().isEmpty())
            {
                updatedUserData.put("Email",newEmail.getText().toString().trim());
            }
            if(!newSchool.getText().toString().isEmpty())
            {
                updatedUserData.put("School",newSchool.getText().toString().trim());
            }
            if(!newClassroom.getText().toString().isEmpty())
            {
                updatedUserData.put("Classroom",newClassroom.getText().toString().trim());
            }
            Toast.makeText(EditProfile.this, "User's info has been updated", Toast.LENGTH_LONG).show();
            firebaseUserRef.update(updatedUserData);
        }
        startActivity(new Intent(EditProfile.this, MainActivity.class));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            EditProfile.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}