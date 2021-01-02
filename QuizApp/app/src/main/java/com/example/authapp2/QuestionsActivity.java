package com.example.authapp2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import static com.example.authapp2.SetsActivity.category_id;

public class QuestionsActivity extends AppCompatActivity implements View.OnClickListener
{
    private TextView question,qCount,qTimer; //TextView variables holding by order: question title,question number,question time
    private Button option1,option2,option3,option4; //answer buttons for question
    private List<Question>questionList; //list for holding questions
    private int qNum,score,setNum; //Int variables holding by order: question number (inside set),score points,set number (inside category)
    private CountDownTimer countDownTimer; //timer object
    private FirebaseFirestore firestore; //firestore database object

    @Override
    protected void onCreate(Bundle savedInstanceState) //on create method that runs as QuestionActivity starts
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        //implementing values to question,question_number,question_timer
        question = (TextView)findViewById(R.id.question);
        qCount = (TextView)findViewById(R.id.questionNum);
        qTimer = (TextView)findViewById(R.id.questionTime);

        //implementing values to answer buttons
        option1 = (Button)findViewById(R.id.option1);
        option2 = (Button)findViewById(R.id.option2);
        option3 = (Button)findViewById(R.id.option3);
        option4 = (Button)findViewById(R.id.option4);

        //implementing on click listener for every button
        option1.setOnClickListener(this);
        option2.setOnClickListener(this);
        option3.setOnClickListener(this);
        option4.setOnClickListener(this);

        setNum = getIntent().getIntExtra("SET_NUM",1);

        //getting firestore instance (reference)
        firestore = FirebaseFirestore.getInstance();

        getQuestionList();

        score = 0;
    }

    //method for getting question list from Firebase
    private void getQuestionList()
    {
        //creating new array named questionList to hold all questions
        questionList = new ArrayList<>();

        firestore.collection("QUIZ").document("CAT" + String.valueOf(category_id))
                .collection("SET" + String.valueOf(setNum))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if(task.isSuccessful())
                {
                    QuerySnapshot questions = task.getResult();

                    for(QueryDocumentSnapshot doc : questions)
                    {
                        questionList.add(new Question(doc.getString("QUESTION"),
                                doc.getString("A"),
                                doc.getString("B"),
                                doc.getString("C"),
                                doc.getString("D"),
                                Integer.valueOf(doc.getString("ANSWER"))));
                    }
                    setQuestion(); //after getting question fields set the question
                }
                else
                {
                    Toast.makeText(QuestionsActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setQuestion()
    {
        qTimer.setText(String.valueOf(10));

        question.setText(questionList.get(0).getQuestion());
        option1.setText(questionList.get(0).getOptionA());
        option2.setText(questionList.get(0).getOptionB());
        option3.setText(questionList.get(0).getOptionC());
        option4.setText(questionList.get(0).getOptionD());

        qCount.setText(String.valueOf(1)+"/"+String.valueOf(questionList.size()));

        startTimer();

        qNum = 0;
    }

    private void startTimer()
    {
        countDownTimer = new CountDownTimer(12000,1000)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                if(millisUntilFinished < 10000)
                    qTimer.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish()
            {
                changeQuestion();
            }
        };
        countDownTimer.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v)
    {
        int selectedOption = 0;
        switch(v.getId())
        {
            case R.id.option1:
                selectedOption = 1;
                break;

            case R.id.option2:
                selectedOption = 2;
                break;

            case R.id.option3:
                selectedOption = 3;
                break;

            case R.id.option4:
                selectedOption = 4;
                break;
            default:
        }
        countDownTimer.cancel();
        checkAnswer(selectedOption,v);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void checkAnswer(int selectedOption, View view)
    {
        if(selectedOption == questionList.get(qNum).getCorrectAns())
        {
            //right answer
            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            score++;
        }
        else
        {
            //wrong answer
            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.RED));

            switch(questionList.get(qNum).getCorrectAns())
            {
                case 1:
                    option1.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 2:
                    option1.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 3:
                    option1.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 4:
                    option1.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;

            }
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                changeQuestion();
            }
        },2000);

    }

    //changing question while qNum is smaller than question list, else goes to score activity
    private void changeQuestion()
    {
        if(qNum < questionList.size()-1)
        {
            //increasing the question number
            qNum++;

            playAnim(question,0,0);
            playAnim(option1,0,1);
            playAnim(option2,0,2);
            playAnim(option3,0,3);
            playAnim(option4,0,4);

            qCount.setText(String.valueOf(qNum+1) + "/" + String.valueOf(questionList.size()));
            qTimer.setText(String.valueOf(10));
            startTimer();
        }
        else
        {
            //go to score activity
            Intent intent = new Intent(QuestionsActivity.this,ScoreActivity.class);
            intent.putExtra("SCORE",String.valueOf(score)+"/"+String.valueOf(questionList.size()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            //QuestionsActivity.this.finish();
        }
    }

    private void playAnim(final View view,final int value,int viewNum)
    {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100).
                setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener()
                {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onAnimationEnd(Animator animation)
            {
                if(value == 0)
                {
                    switch(viewNum)
                    {
                        case 0:
                            ((TextView)view).setText(questionList.get(qNum).getQuestion());
                            break;

                        case 1:
                            ((Button)view).setText(questionList.get(qNum).getOptionA());
                            break;

                        case 2:
                            ((Button)view).setText(questionList.get(qNum).getOptionB());
                            break;

                        case 3:
                            ((Button)view).setText(questionList.get(qNum).getOptionC());
                            break;

                        case 4:
                            ((Button)view).setText(questionList.get(qNum).getOptionD());
                            break;
                    }
                    if(viewNum != 0)
                    {
                        ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#AA00FF")));
                    }

                    playAnim(view,1,viewNum);
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        countDownTimer.cancel();
    }
}