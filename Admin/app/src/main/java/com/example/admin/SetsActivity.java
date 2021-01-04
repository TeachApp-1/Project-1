package com.example.admin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.admin.CategoryActivity.catList;
import static com.example.admin.CategoryActivity.selectedCatIndex;


public class SetsActivity extends AppCompatActivity
{
    private RecyclerView setRecView;
    private Button addSetBtn;
    private SetAdapter setsAdapter;
    private FirebaseFirestore firestore;
    private Dialog loadingDialog;
    public static List<String> setIDs = new ArrayList<>();
    public static int selectedSetIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sets);

        Toolbar toolbar = findViewById(R.id.setsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sets");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setRecView = findViewById(R.id.setRecView);
        addSetBtn = findViewById(R.id.addSetBtn);

        loadingDialog = new Dialog(SetsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        addSetBtn.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v)
            {
                addNewSet();
            }

        });
        firestore = FirebaseFirestore.getInstance();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        setRecView.setLayoutManager(layoutManager);

        loadSets();
    }

    private void loadSets()
    {
        setIDs.clear(); //clearing list

        loadingDialog.show();

        firestore.collection("QUIZ").document(catList.get(selectedCatIndex).getId()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
                {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot)
                    {
                        long numOfSets = (long)documentSnapshot.get("SETS");
                        for(int i=1;i<=numOfSets;i++)
                        {
                            setIDs.add(documentSnapshot.getString("SET" + String.valueOf(i) + "_ID"));
                        }
                        catList.get(selectedCatIndex).setSetCounter(documentSnapshot.getString("COUNTER"));
                        catList.get(selectedCatIndex).setNumOfSets(String.valueOf(numOfSets));

                        setsAdapter = new SetAdapter(setIDs);
                        setRecView.setAdapter(setsAdapter);

                        loadingDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(SetsActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    }
                });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void addNewSet()
    {
        loadingDialog.show();

        final String currentCatID = catList.get(selectedCatIndex).getId();
        final String currentCounter = catList.get(selectedCatIndex).getSetCounter();

        Map<String,Object> qData = new ArrayMap<>();
        qData.put("COUNT","0");

        firestore.collection("QUIZ").document(currentCatID).collection(currentCounter)
                .document("QUESTION_LIST").set(qData)
                .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        Map<String,Object> catDoc = new ArrayMap<>();
                        catDoc.put("COUNTER", String.valueOf(Integer.valueOf(currentCounter) + 1));
                        catDoc.put("SET" + String.valueOf(setIDs.size() + 1) + "_ID",currentCounter);
                        catDoc.put("SETS",setIDs.size() + 1);

                        firestore.collection("QUIZ").document(currentCatID).update(catDoc)
                                .addOnSuccessListener(new OnSuccessListener<Void>()
                                {
                                    @Override
                                    public void onSuccess(Void aVoid)
                                    {
                                        Toast.makeText(SetsActivity.this,"Set Added Successfully",Toast.LENGTH_SHORT).show();

                                        setIDs.add(currentCounter);
                                        catList.get(selectedCatIndex).setNumOfSets(String.valueOf(setIDs.size()));
                                        catList.get(selectedCatIndex).setSetCounter(String.valueOf(Integer.valueOf(currentCounter)+1));

                                        setsAdapter.notifyItemInserted(setIDs.size());
                                        loadingDialog.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener()
                                {
                                    @Override
                                    public void onFailure(@NonNull Exception e)
                                    {
                                        Toast.makeText(SetsActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                        loadingDialog.dismiss();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(SetsActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
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