package com.example.quizadmin;

import android.app.AppComponentFactory;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity
{
    private TextView title;
    private Button login;
    private EditText userEmail, userPassword;
    private FirebaseAuth firebaseAuth;
    private Dialog loadingDialog;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.main_title);
        userEmail = findViewById(R.id.emailText);
        userPassword = findViewById(R.id.passwordText);
        login = findViewById(R.id.main_loginB);
        progressBar = findViewById(R.id.progressBarMain);
        firebaseAuth = FirebaseAuth.getInstance();


        Typeface typeface = ResourcesCompat.getFont(this, R.font.blacklist);
        title.setTypeface(typeface);

        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email = userEmail.getText().toString().trim();
                String password = userPassword.getText().toString().trim();

                if(email.isEmpty()){
                    userEmail.setError("Email is required");
                    userEmail.requestFocus();
                    return;
                }
                else
                {
                    userEmail.setError(null);
                }
                if(password.isEmpty()){
                    userPassword.setError("Password is required");
                    userPassword.requestFocus();
                    return;
                }
                else if(password.length() < 6){
                    userPassword.setError("Password needs to be at least 6 digits");
                    userPassword.requestFocus();
                    return;
                }
                else
                {
                    userPassword.setError(null);
                }

                fireBaseLogin();
            }
        });

        if(firebaseAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(MainActivity.this,CategoryActivity.class));
        }
    }

    private void fireBaseLogin()
    {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(userEmail.getText().toString(), userPassword.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {

                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()){
                        //startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                        //loadData();
                        startActivity(new Intent(MainActivity.this,CategoryActivity.class));
                    }
                    else{
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this,"Check your email to verify your account",Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    Toast.makeText(MainActivity.this,"Failed to login, check your credentials",Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

}



