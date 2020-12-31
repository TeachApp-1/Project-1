package com.example.redchapters;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.redchapters.R;

public class MainActivity extends AppCompatActivity {
    private TextView title;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.main_title);
        login = findViewById(R.id.main_loginB);

        Typeface typeface = ResourcesCompat.getFont(this, R.font.blacklist);
        title.setTypeface(typeface);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                startActivity(intent);
            }
        });
    }
}
