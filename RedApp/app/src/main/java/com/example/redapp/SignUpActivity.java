package com.example.redapp;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class SignUpActivity extends AppCompatActivity {

    private EditText userName,userEmail,userPassword,userSchool,userClassroom, confirmPassword;
    private Button signUpB;
    private ImageView backB;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userName = findViewById(R.id.user_name);
        userEmail = findViewById(R.id.emailId);
        userPassword = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_pass);
        userSchool = findViewById(R.id.school_name);
        userClassroom = findViewById(R.id.grade);

        signUpB = findViewById(R.id.signUpB);
        backB = findViewById(R.id.backB);


        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        signUpB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                userRegister();
            }
        });
    }

    private void userRegister()
    {
        String name = userName.getText().toString().trim();
        String email = userEmail.getText().toString().trim();
        String password = userPassword.getText().toString().trim();
        String confirmPass = confirmPassword.getText().toString().trim();
        String school = userSchool.getText().toString().trim();
        String classroom = userClassroom.getText().toString().trim();

        if(name.isEmpty())
        {
            userName.setError("ספק שם מלא");
            userName.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            userPassword.setError("ספק סיסמא");
            userPassword.requestFocus();
            return;
        }
        if(confirmPass.isEmpty())
        {
            confirmPassword.setError("ספק סיסמה תואמת");
            confirmPassword.requestFocus();
            return;
        }
        if(email.isEmpty())
        {
            userEmail.setError("ספק אימייל");
            userEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            userEmail.setError("ספק אימייל תקין");
            userEmail.requestFocus();
            return;
        }
        if(school.isEmpty())
        {
            userSchool.setError("ספק בית ספר");
            userSchool.requestFocus();
            return;
        }
        if(classroom.isEmpty())
        {
            userClassroom.setError("ספק כיתה");
            userClassroom.requestFocus();
            return;
        }
        if(password.compareTo(confirmPass) != 0 )
        {
            userPassword.setError("על הסיסמאות להיות תואמות");
            userPassword.requestFocus();
            return;
        }
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    UserModel userModel = new UserModel(name,email,school,classroom);
                    userID = firebaseAuth.getCurrentUser().getUid();

                    DocumentReference documentReference = firestore.collection("Users").document(userID);
                    Map<String,Object> newUser = new HashMap<>();
                    newUser.put("Name",name);
                    newUser.put("Email",email);
                    newUser.put("School",school);
                    newUser.put("Classroom",classroom);
                    documentReference.set(newUser)
                            .addOnSuccessListener(new OnSuccessListener<Void>()
                            {
                                @Override
                                public void onSuccess(Void aVoid)
                                {
                                    Toast.makeText(SignUpActivity.this,"User Created",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Toast.makeText(SignUpActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(SignUpActivity.this,"Something Went Wrong",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}