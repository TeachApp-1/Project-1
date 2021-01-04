package com.example.admin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import static com.example.admin.CategoryActivity.catList;
import static com.example.admin.CategoryActivity.selectedCatIndex;
import static com.example.admin.QuestionActivity.questionList;
import static com.example.admin.SetsActivity.selectedSetIndex;
import static com.example.admin.SetsActivity.setIDs;

public class QuestionDetailActivity extends AppCompatActivity
{
    private EditText question,optionA,optionB,optionC,optionD,answer;
    private Button addQuestionBtn;
    private String questionStr,optionAStr,optionBStr,optionCStr,optionDStr,answerStr;
    private Dialog loadingDialog;
    private FirebaseFirestore firestore;
    private String action;
    private int questionID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);

        Toolbar toolbar = findViewById(R.id.questionDetailToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        question = findViewById(R.id.question);
        optionA = findViewById(R.id.optionA);
        optionB = findViewById(R.id.optionB);
        optionC = findViewById(R.id.optionC);
        optionD = findViewById(R.id.optionD);
        answer = findViewById(R.id.answer);
        addQuestionBtn = findViewById(R.id.questionDetailBtn);

        loadingDialog = new Dialog(QuestionDetailActivity.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        firestore = FirebaseFirestore.getInstance();

        action = getIntent().getStringExtra("ACTION");

        if(action.compareTo("EDIT") == 0)
        {
            questionID = getIntent().getIntExtra("Q_ID",0);
            loadData(questionID);
            getSupportActionBar().setTitle("Question" + String.valueOf(questionID + 1));
            addQuestionBtn.setText("UPDATE");
        }
        else
        {
            getSupportActionBar().setTitle("Question" + String.valueOf(questionList.size() + 1));
            addQuestionBtn.setText("ADD");
        }

        addQuestionBtn.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v)
            {
                questionStr = question.getText().toString();
                optionAStr = optionA.getText().toString();
                optionBStr = optionB.getText().toString();
                optionCStr = optionC.getText().toString();
                optionDStr = optionD.getText().toString();
                answerStr = answer.getText().toString();

                if(questionStr.isEmpty())
                {
                    question.setError("Enter Question");
                    return;
                }
                if(optionAStr.isEmpty())
                {
                    optionA.setError("Enter Option A");
                    return;
                }
                if(optionBStr.isEmpty())
                {
                    optionB.setError("Enter Option B");
                    return;
                }
                if(optionCStr.isEmpty())
                {
                    optionC.setError("Enter Option C");
                    return;
                }
                if(optionDStr.isEmpty())
                {
                    optionD.setError("Enter Option D");
                    return;
                }
                if(answerStr.isEmpty())
                {
                    answer.setError("Enter Answer");
                    return;
                }
                if(action.compareTo("EDIT") == 0)
                {
                    editQuestion();
                }
                else
                {
                    addNewQuestion();
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void addNewQuestion()
    {
        loadingDialog.show();

        Map<String,Object> questionData = new ArrayMap<>();

        questionData.put("QUESTION",questionStr);
        questionData.put("A",optionAStr);
        questionData.put("B",optionBStr);
        questionData.put("C",optionCStr);
        questionData.put("D",optionDStr);
        questionData.put("ANSWER",answerStr);

        final String docID = firestore.collection("QUIZ").document(catList.get(selectedCatIndex).getId())
                .collection(setIDs.get(selectedSetIndex)).document().getId();

        firestore.collection("QUIZ").document(catList.get(selectedCatIndex).getId())
                .collection(setIDs.get(selectedSetIndex)).document(docID).set(questionData)
                .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        Map<String,Object> questionDoc = new ArrayMap<>();
                        questionDoc.put("Q" + String.valueOf(questionList.size() + 1) + "_ID",docID);
                        questionDoc.put("COUNT",String.valueOf(questionList.size() + 1));

                        firestore.collection("QUIZ").document(catList.get(selectedCatIndex).getId())
                                .collection(setIDs.get(selectedSetIndex)).document("QUESTION_LIST").update(questionDoc)
                                .addOnSuccessListener(new OnSuccessListener<Void>()
                                {
                                    @Override
                                    public void onSuccess(Void aVoid)
                                    {
                                        Toast.makeText(QuestionDetailActivity.this,"Question Added Successfully",Toast.LENGTH_SHORT).show();

                                        questionList.add(new com.example.admin.QuestionModel(docID,questionStr,optionAStr,optionBStr,optionCStr,optionDStr,Integer.valueOf(answerStr)));

                                        loadingDialog.dismiss();
                                        QuestionDetailActivity.this.finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener()
                                {
                                    @Override
                                    public void onFailure(@NonNull Exception e)
                                    {
                                        Toast.makeText(QuestionDetailActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                        loadingDialog.dismiss();
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(QuestionDetailActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });

    }
    private void loadData(int id)
    {
        question.setText(questionList.get(id).getQuestion());
        optionA.setText(questionList.get(id).getOptionA());
        optionB.setText(questionList.get(id).getOptionB());
        optionC.setText(questionList.get(id).getOptionC());
        optionD.setText(questionList.get(id).getOptionD());
        answer.setText(String.valueOf(questionList.get(id).getCorrectAnswer()));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void editQuestion()
    {
        loadingDialog.show();

        Map<String,Object> questionData = new ArrayMap<>();
        questionData.put("QUESTION",questionStr);
        questionData.put("A",optionAStr);
        questionData.put("B",optionBStr);
        questionData.put("C",optionCStr);
        questionData.put("D",optionDStr);
        questionData.put("ANSWER",answerStr);

        firestore.collection("QUIZ").document(catList.get(selectedCatIndex).getId())
                .collection(setIDs.get(selectedSetIndex)).document(questionList.get(questionID).getQuestionID())
                .set(questionData)
                .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        Toast.makeText(QuestionDetailActivity.this,"Question updated successfully",Toast.LENGTH_SHORT).show();
                        questionList.get(questionID).setQuestion(questionStr);
                        questionList.get(questionID).setOptionA(optionAStr);
                        questionList.get(questionID).setOptionB(optionBStr);
                        questionList.get(questionID).setOptionC(optionCStr);
                        questionList.get(questionID).setOptionD(optionDStr);
                        questionList.get(questionID).setCorrectAnswer(Integer.valueOf(answerStr));

                        loadingDialog.dismiss();
                        QuestionDetailActivity.this.finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(QuestionDetailActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            QuestionDetailActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}