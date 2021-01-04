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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.admin.CategoryActivity.catList;
import static com.example.admin.CategoryActivity.selectedCatIndex;
import static com.example.admin.SetsActivity.selectedSetIndex;
import static com.example.admin.SetsActivity.setIDs;

public class QuestionActivity extends AppCompatActivity
{
    private RecyclerView questionRecView;
    private Button addQuestionBtn;
    public static List<com.example.admin.QuestionModel> questionList = new ArrayList<>();
    private com.example.admin.QuestionAdapter questionAdapter;
    private FirebaseFirestore firestore;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Toolbar toolbar = findViewById(R.id.questionToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Questions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        questionRecView = findViewById(R.id.questionRecView);
        addQuestionBtn = findViewById(R.id.addQuestionBtn);

        loadingDialog = new Dialog(QuestionActivity.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        addQuestionBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(QuestionActivity.this, com.example.admin.QuestionDetailActivity.class);
                intent.putExtra("ACTION","ADD");
                startActivity(intent);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        questionRecView.setLayoutManager(layoutManager);

        firestore = FirebaseFirestore.getInstance();

        loadQuestions();
    }

    private void loadQuestions()
    {
        questionList.clear();

        loadingDialog.show();

        firestore.collection("QUIZ").document(catList.get(selectedCatIndex).getId())
                .collection(setIDs.get(selectedSetIndex)).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
                {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots)
                    {
                        Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();

                        for(QueryDocumentSnapshot doc : queryDocumentSnapshots)
                        {
                            docList.put(doc.getId(),doc);
                        }
                        QueryDocumentSnapshot questionListDoc = docList.get("QUESTION_LIST");
                        String count = questionListDoc.getString("COUNT");
                        for(int i=0;i< Integer.valueOf(count);i++)
                        {
                            String questionID = questionListDoc.getString("Q" + String.valueOf(i+1) + "_ID");
                            QueryDocumentSnapshot questionDoc = docList.get(questionID);

                            questionList.add(new com.example.admin.QuestionModel(
                                    questionID,
                                    questionDoc.getString("QUESTION"),
                                    questionDoc.getString("A"),
                                    questionDoc.getString("B"),
                                    questionDoc.getString("C"),
                                    questionDoc.getString("D"),
                                    Integer.valueOf(questionDoc.getString("ANSWER"))));
                        }

                        questionAdapter = new com.example.admin.QuestionAdapter(questionList);
                        questionRecView.setAdapter(questionAdapter);

                        loadingDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(QuestionActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    }
                });
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if(questionAdapter != null)
        {
            questionAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            QuestionActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(QuestionActivity.this,SetsActivity.class);
        startActivity(intent);
    }
}