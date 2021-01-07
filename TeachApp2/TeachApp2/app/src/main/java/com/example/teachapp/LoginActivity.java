package com.example.teachapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText email,pass;
    private Button loginB;
    private TextView forgotPassB,singupB;
    private FirebaseAuth mAuth;
    private Dialog progressDialog;
    private TextView dialogText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        loginB = findViewById(R.id.loginB);
        forgotPassB = findViewById(R.id.forgot_pass);
        singupB = findViewById(R.id.singupB);


        progressDialog = new Dialog(LoginActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("מתחבר...");

        mAuth = FirebaseAuth.getInstance();

        
        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                if(validateData())
                {
                    login();
                }
            }

        });

        singupB.setOnClickListener((view) ->{
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
        });

    }

    private boolean validateData()
    {
        if(email.getText().toString().isEmpty())
        {
            email.setError("הכנס איימיל");
            return false;
        }
        if(pass.getText().toString().isEmpty()) {
            pass.setError("הכנס סיסמה");
            return false;
        }
        return true;
    }

    private void login()
    {

        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), pass.getText().toString().trim())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                        DbQuery.loadData(new MyCompleteListener() {
                            @Override
                            public void onSuccess() {
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onFailure() {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public TextView getForgotPassB() {
        return forgotPassB;
    }

    public void setForgotPassB(TextView forgotPassB) {
        this.forgotPassB = forgotPassB;
    }
}