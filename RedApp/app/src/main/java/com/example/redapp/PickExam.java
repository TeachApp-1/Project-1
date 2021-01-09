package com.example.redapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PickExam extends AppCompatActivity {

    private Button slow, fast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_exam);


        slow = findViewById(R.id.slow_mode_button);
        fast = findViewById(R.id.fast_mode_button);

        fast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PickExam.this, MainActivity.class);
                startActivity(intent);
            }
        });

        slow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PickExam.this, SlowQuestionActivity.class);
                startActivity(intent);
            }
        });
    }
}