package com.example.authapp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class ForgotPassword extends AppCompatActivity {

    private TextView banner;
    private EditText emailEditText;
    private Button resetPasswordBtn;
    private ProgressBar progressBar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEditText = (EditText) findViewById(R.id.emailForgotPassword);
        resetPasswordBtn = (Button) findViewById(R.id.resetPasswordBtn);
        resetPasswordBtn.setOnClickListener(this::onClick);
        progressBar = (ProgressBar) findViewById(R.id.progressBarForgotPassword);
        banner = (TextView) findViewById(R.id.homeLogoResetPassword);
        banner.setOnClickListener(this::onClick);

        auth = FirebaseAuth.getInstance();

    }
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.homeLogoResetPassword:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.resetPasswordBtn:
                resetPassword();
                break;
            }

        }

    private void resetPassword() {
        String email = emailEditText.getText().toString().trim();

        if(email.isEmpty()){
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Enter a valid email");
            emailEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this,"Check your email to reset your password",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(ForgotPassword.this,"Something went wrong, Try again",Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
