package com.example.teachapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SelectLevelActivity extends AppCompatActivity {

    private Button amaB;
    private Button proB;
    private Button legB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_level);


        amaB = findViewById(R.id.amateurB);
        proB = findViewById(R.id.proB);
        legB = findViewById(R.id.legendB);


        amaB.setOnClickListener(v -> {
            DbQuery.g_selected_lev = "QUIZ_1-10";
            DbQuery.loadData(new MyCompleteListener() {
                @Override
                public void onSuccess() {
                    Intent intent = new Intent(SelectLevelActivity.this,MainActivity.class);
                    startActivity(intent);
                    SelectLevelActivity.this.finish();
                }

                @Override
                public void onFailure() {
                    Toast.makeText(SelectLevelActivity.this,"משהו השתבש...נסה שוב מאוחר יותר!",Toast.LENGTH_SHORT).show();
                }
            });
        });

        proB.setOnClickListener(v -> {
            DbQuery.g_selected_lev = "QUIZ_10-100";
            DbQuery.loadData(new MyCompleteListener() {
                @Override
                public void onSuccess() {

                    Intent intent = new Intent(SelectLevelActivity.this,MainActivity.class);
                    startActivity(intent);
                    SelectLevelActivity.this.finish();

                }

                @Override
                public void onFailure() {

                    Toast.makeText(SelectLevelActivity.this,"משהו השתבש...נסה שוב מאוחר יותר!",Toast.LENGTH_SHORT).show();

                }
            });
        });


        legB.setOnClickListener(v -> {
            DbQuery.g_selected_lev = "QUIZ_100-1000";
            DbQuery.loadData(new MyCompleteListener() {
                @Override
                public void onSuccess() {
                    Intent intent = new Intent(SelectLevelActivity.this,MainActivity.class);
                    startActivity(intent);
                    SelectLevelActivity.this.finish();
                }

                @Override
                public void onFailure() {

                    Toast.makeText(SelectLevelActivity.this,"משהו השתבש...נסה שוב מאוחר יותר!",Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}