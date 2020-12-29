package com.example.teachapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignUpActivity extends AppCompatActivity {

    private EditText name, email, pass, confirmPass;
    private Button signUpB;
    private ImageView backB;
    private FirebaseAuth mAuth;
    private String emailStr,passStr;
    private String confirmPassStr;
    private String nameStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.user_name);
        email = findViewById(R.id.emailId);
        pass = findViewById(R.id.password);
        confirmPass = findViewById(R.id.confirm_pass);
        signUpB = findViewById(R.id.signUpB);
        backB = findViewById(R.id.backB);

        mAuth = FirebaseAuth.getInstance();

        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        signUpB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate()) {
                    singUpNewUser();
                }
            }
        });

    }
    @SuppressLint("ShowToast")
    private boolean validate()
    {
        nameStr = name.getText().toString().trim();
        emailStr = email.getText().toString().trim();
        passStr = pass.getText().toString().trim();
        confirmPassStr = confirmPass.getText().toString().trim();

        if (nameStr.isEmpty())
        {
            name.setError("Enter Your Name");
            return false;
        }

        if(emailStr.isEmpty())
        {
            email.setError("Enter E-Mail ID");
            return false;
        }

        if(passStr.isEmpty())
        {
            pass.setError("Enter Password");
            return false;
        }

        if(confirmPassStr.isEmpty())
        {
            confirmPass.setError("Enter Password");
            return false;
        }

        if(passStr.compareTo(confirmPassStr) != 0 )
        {
            Toast toast = Toast.makeText(SignUpActivity.this, "Password and confirm Password should be the same ! ", Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }

    private void singUpNewUser()
    {

        mAuth.createUserWithEmailAndPassword(emailStr, passStr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task <AuthResult> task){
                    if (task.isSuccessful()) {

                        Toast.makeText(SignUpActivity.this,"Sign Up Successful",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(intent);
                        SignUpActivity.this.finish();
                    }
                    else {
                        Toast.makeText(SignUpActivity.this, "Sign Up Failed",
                                Toast.LENGTH_SHORT).show();
                    }
                    }
                });

    }
}