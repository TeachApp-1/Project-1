package com.example.authapp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.authapp2.MainActivity.catList;

public class SetsActivity extends AppCompatActivity
{
    private GridView sets_grid;
    private FirebaseFirestore firestore;
    public static int category_id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sets);

        Toolbar toolbar = findViewById(R.id.setsToolbar);
        setSupportActionBar(toolbar);

        String title = getIntent().getStringExtra("CATEGORY");
        category_id = getIntent().getIntExtra("CATEGORY_ID",1);
        getSupportActionBar().setTitle(title);

        sets_grid = findViewById(R.id.setsGridView);

        firestore = FirebaseFirestore.getInstance(); //getting reference from firebase firestore

        loadSets();

        SetsAdapter adapter = new SetsAdapter(6);
        sets_grid.setAdapter(adapter);
    }

    private void loadSets()
    {
        firestore.collection("QUIZ").document("CAT" + String.valueOf(category_id))
                .get().addOnCompleteListener((task) ->
        {
            if(task.isSuccessful())
            {
                DocumentSnapshot doc = task.getResult();

                if(doc.exists())
                {
                    long sets = (long) doc.get("SETS");
                    SetsAdapter adapter = new SetsAdapter((int) sets);
                    sets_grid.setAdapter(adapter);
                }
                else
                {
                    Toast.makeText(SetsActivity.this,"No CAT Document Exists!",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
            else
            {
                Toast.makeText(SetsActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            SetsActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(SetsActivity.this,CategoryActivity.class);
        startActivity(intent);
    }
}