package com.example.redapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DownloadMaterial extends AppCompatActivity {

    FirebaseFirestore db;
    RecyclerView mRecyclerView;
    ArrayList<DownModel> downModelArrayList = new ArrayList<>();
    DownloadAdapter downloadAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_material);

        setUpRV();
        setUpFB();
        dataFromFirebase();
    }
    private void dataFromFirebase() {
        if(downModelArrayList.size()>0)
            downModelArrayList.clear();

    //db=FirebaseFirestore.getInstance();

    db.collection("Uploads")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            for(DocumentSnapshot documentSnapshot: task.getResult()) {

                DownModel downModel= new DownModel(documentSnapshot.getString("name"),
                        documentSnapshot.getString("link"));
                downModelArrayList.add(downModel);
            }

            downloadAdapter= new DownloadAdapter(DownloadMaterial.this,downModelArrayList);
            mRecyclerView.setAdapter(downloadAdapter);
        }
    })

            .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(DownloadMaterial.this, "Error ;-.-;", Toast.LENGTH_SHORT).show();
        }
    })
    ;

}

    private void setUpFB(){
        db=FirebaseFirestore.getInstance();
    }

    private void setUpRV(){
        mRecyclerView= findViewById(R.id.download_recycle);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}

