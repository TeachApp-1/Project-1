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
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryActivity extends AppCompatActivity
{
    private Button addCatBtn,dialogAddCatBtn;
    private RecyclerView catRecyclerView;
    public static List<com.example.admin.CategoryModel> catList = new ArrayList<>();
    public static int selectedCatIndex=0;
    private FirebaseFirestore firestore;
    private Dialog addCatDialog;
    private EditText dialogCatName;
    private com.example.admin.CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


        Toolbar toolbar = findViewById(R.id.catToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Categories");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addCatBtn = findViewById(R.id.addSetBtn);
        catRecyclerView = findViewById(R.id.setRecView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        catRecyclerView.setLayoutManager(manager);
        catRecyclerView.setHasFixedSize(true);
        adapter = new com.example.admin.CategoryAdapter(catList);
        catRecyclerView.setAdapter(adapter);

        addCatDialog = new Dialog(CategoryActivity.this); //creating a new dialog object for CategoryActivity
        addCatDialog.setContentView(R.layout.add_category_dialog); //setting addCatDialog content view to add_category_dialog
        addCatDialog.setCancelable(true);
        addCatDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT); //getting dialog window while setting its layout both width,height to wrap content

        dialogAddCatBtn = addCatDialog.findViewById(R.id.addCatDialogBtn);
        dialogCatName = addCatDialog.findViewById(R.id.addCatNameDialog);

        firestore = FirebaseFirestore.getInstance();

        addCatBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialogCatName.getText().clear();
                addCatDialog.show();
            }
        });

        dialogAddCatBtn.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v)
            {
                if(dialogCatName.getText().toString().isEmpty())
                {
                    dialogCatName.setError("Enter Category Name");
                    return;
                }

                addNewCategory(dialogCatName.getText().toString());
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        catRecyclerView.setLayoutManager(layoutManager);

        loadData();
    }

    private void loadData()
    {
        catList.clear();
        firestore.collection("QUIZ").document("CATEGORIES")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if(task.isSuccessful())
                {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists())
                    {
                        long count = (long)doc.get("COUNT");
                        for(int i = 1;i <= count;i++)
                        {
                            String catName = doc.getString("CAT" + String.valueOf(i) + "_NAME"); //getting category name from firestore
                            String catID = doc.getString("CAT" + String.valueOf(i) + "_ID"); //getting category id from firestore
                            catList.add(new com.example.admin.CategoryModel(catID,catName,"0","1")); //adding the category catList array
                        }
                        adapter = new com.example.admin.CategoryAdapter(catList); //creating a new CategoryAdapter object to hold all categories in recycler view
                        catRecyclerView.setAdapter(adapter); //setting the category recycler view to display categories
                    }
                    else
                    {
                        Toast.makeText(CategoryActivity.this,"No categories exists",Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
                else
                {
                    Toast.makeText(CategoryActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void addNewCategory(String title)
    {
        addCatDialog.dismiss();

        Map<String,Object> catData = new ArrayMap<>();
        catData.put("NAME",title);
        catData.put("SETS",0);
        catData.put("COUNTER","1");

        final String doc_id = firestore.collection("QUIZ").document().getId();

        firestore.collection("QUIZ").document(doc_id).set(catData).addOnSuccessListener(new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void aVoid)
            {
                Map<String,Object> catDoc = new ArrayMap<>();
                catDoc.put("CAT" + String.valueOf(catList.size()+1) + "_NAME",title);
                catDoc.put("CAT" + String.valueOf(catList.size()+1) + "_ID",doc_id);
                catDoc.put("COUNT",catList.size()+1);

                firestore.collection("QUIZ").document("CATEGORIES").update(catDoc)
                        .addOnSuccessListener(new OnSuccessListener<Void>()
                        {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                Toast.makeText(CategoryActivity.this,"Category Has Been Added",Toast.LENGTH_LONG).show();

                                catList.add(new com.example.admin.CategoryModel(doc_id,title,"0","1"));

                                adapter.notifyItemInserted(catList.size());
                            }
                        }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(CategoryActivity.this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(CategoryActivity.this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            CategoryActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(CategoryActivity.this,MainActivity.class);
        startActivity(intent);
    }

}