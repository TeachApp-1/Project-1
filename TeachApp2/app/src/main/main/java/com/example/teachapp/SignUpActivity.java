package com.example.teachapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


public class SignUpActivity extends AppCompatActivity {

    private EditText name, email, pass, confirmPass;
    private EditText school,classroom;
    private Button signUpB;
    private ImageView backB;
    private FirebaseAuth mAuth;
    private String emailStr,passStr;
    private String confirmPassStr,nameStr;
    private String schooStr,gradeStr;
    private Dialog progressDialog;
    private TextView dialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.user_name);
        email = findViewById(R.id.emailId);
        pass = findViewById(R.id.password);
        confirmPass = findViewById(R.id.confirm_pass);
        school = findViewById(R.id.school_name);
        classroom = findViewById(R.id.grade);
        signUpB = findViewById(R.id.signUpB);
        backB = findViewById(R.id.backB);


        progressDialog = new Dialog(SignUpActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("מבצע רישום...");

        mAuth = FirebaseAuth.getInstance();

        backB.setOnClickListener(view -> finish());

        signUpB.setOnClickListener(view -> {

            if (validate()) {
                singUpNewUser();
            }
        });

    }
    private boolean validate()
    {
        nameStr = name.getText().toString().trim();
        emailStr = email.getText().toString().trim();
        passStr = pass.getText().toString().trim();
        confirmPassStr = confirmPass.getText().toString().trim();
        schooStr = school.getText().toString().trim();
        gradeStr = classroom.getText().toString().trim();


        if (nameStr.isEmpty())
        {
            name.setError("הכנס שם מלא");
            return false;
        }

        if(emailStr.isEmpty())
        {
            email.setError("הכנס סיסמה");
            return false;
        }

        if(passStr.isEmpty())
        {
            pass.setError("הכנס סיסמה");
            return false;
        }

        if(confirmPassStr.isEmpty())
        {
            confirmPass.setError("הכנס סיסמה");
            return false;
        }

        if(schooStr.isEmpty())
        {
            school.setError("הכנס את שם בית הספר");
            return false;
        }

        if(gradeStr.isEmpty())
        {
            pass.setError("הכנס כיתה");
            return false;
        }

        if(passStr.compareTo(confirmPassStr) != 0 )
        {
            Toast toast = Toast.makeText(SignUpActivity.this, "על הסיסמאות להיות תואמות!!", Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }

    private void singUpNewUser()
    {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(emailStr, passStr)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        Toast.makeText(SignUpActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();

                        DbQuery.createUserDate(emailStr,nameStr,schooStr,gradeStr, new MyCompleteListener() {
                            @Override
                            public void onSuccess() {

                                DbQuery.loadData(new MyCompleteListener() {
                                    @Override
                                    public void onSuccess()
                                    {
                                        progressDialog.dismiss();
                                        Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        SignUpActivity.this.finish();
                                    }

                                    @Override
                                    public void onFailure()
                                    {
                                        Toast.makeText(SignUpActivity.this,"משהו השתבש...נסה שוב מאוחר יותר!",Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });

                            }

                            @Override
                            public void onFailure() {

                                Toast.makeText(SignUpActivity.this,"משהו השתבש...נסה שוב מאוחר יותר!",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, "ההרשמה נכשלה", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}