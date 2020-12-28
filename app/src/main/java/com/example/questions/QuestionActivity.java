package com.example.questions;

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

import java.util.ArrayList;
import java.util.List;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView question, quest_num, countdown;
    private Button answer1, answer2, answer3, answer4;
    private List<Question> questionList;
    int questionNumber;
    private CountDownTimer countDownTimer;
    private int score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        question = findViewById(R.id.question);
        quest_num = findViewById(R.id.quest_num);
        countdown = findViewById(R.id.countdown);

        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);

        answer1.setOnClickListener(this);
        answer2.setOnClickListener(this);
        answer3.setOnClickListener(this);
        answer4.setOnClickListener(this);

        getQuestionList();
    }

    private void getQuestionList()
    {
        questionList = new ArrayList<>();
        questionList.add(new Question("Question 1", "1", "2", "3", "4", 1));
        questionList.add(new Question("Question 2", "1", "2", "3", "4", 2));
        questionList.add(new Question("Question 3", "1", "2", "3", "4", 3));
        questionList.add(new Question("Question 4", "1", "2", "3", "4", 4));
        questionList.add(new Question("Question 5", "1", "2", "3", "4", 1));

        setQuestion();
        score = 0;
    }

    private void setQuestion()
    {
        countdown.setText(String.valueOf(10));
        question.setText(questionList.get(0).getQuestion());
        answer1.setText(questionList.get(0).getOption1());
        answer2.setText(questionList.get(0).getOption2());
        answer3.setText(questionList.get(0).getOption3());
        answer4.setText(questionList.get(0).getOption4());

        quest_num.setText(String.valueOf(1) + "/" + String.valueOf(questionList.size()));

        startTimer();

        questionNumber = 0;
    }

    private void startTimer()
    {
        countDownTimer = new CountDownTimer(12000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(millisUntilFinished < 10000)
                    countdown.setText(String.valueOf(millisUntilFinished/1000));// convert to sec
            }

            @Override
            public void onFinish() {
                changeQuestion();
            }
        };
        countDownTimer.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        int selectedOption = 0;
        switch (v.getId())
        {
            case R.id.answer1:
                selectedOption = 1;
                break;

            case R.id.answer2:
                selectedOption = 2;
                break;

            case R.id.answer3:
                selectedOption = 3;
                break;

            case R.id.answer4:
                selectedOption = 4;
                break;
            default:
        }
        countDownTimer.cancel();
        checkAnswer(selectedOption, v);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void checkAnswer(int selectedOption, View view)
    {
        if(selectedOption == questionList.get(questionNumber).getCorrectAnswer())
        {
            //Right answer
            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            score++;
        }
        else
        {
            //Wrong answer
            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            switch (questionList.get(questionNumber).getCorrectAnswer())
            {
                case 1:
                    answer1.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 2:
                    answer2.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 3:
                    answer3.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 4:
                    answer4.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
            }
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() { }},2000);
        changeQuestion();
    }

    private void changeQuestion()
    {
        if(questionNumber<questionList.size() - 1)
        {
            questionNumber++;

            playAnim(question,0,0);
            playAnim(answer1,0,1);
            playAnim(answer2,0,2);
            playAnim(answer3,0,3);
            playAnim(answer4,0,4);

            quest_num.setText(String.valueOf(questionNumber + 1) + String.valueOf(questionList.size()));

            countdown.setText(String.valueOf(10));
            startTimer();
        }
        else
        {
            //Last question (go to Score Activity)
            Intent intent = new Intent(QuestionActivity.this, ScoreActivity.class);
            intent.putExtra("SCORE", String.valueOf(score) + "/" + String .valueOf(questionList.size()));
            startActivity(intent);
            QuestionActivity.this.finish();
        }

    }

    private void playAnim(View view, final int value,int viewNum)
    {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100).
                setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onAnimationEnd(Animator animation) {
                if(value == 0)
                {
                    switch(viewNum)
                    {
                        case 0:
                            ((TextView)view).setText(questionList.get(questionNumber).getQuestion());
                            break;

                        case 1:
                            ((Button)view).setText(questionList.get(questionNumber).getOption1());

                        case 2:
                            ((Button)view).setText(questionList.get(questionNumber).getOption2());

                        case 3:
                            ((Button)view).setText(questionList.get(questionNumber).getOption3());

                        case 4:
                            ((Button)view).setText(questionList.get(questionNumber).getOption4());
                    }

                    if(viewNum != 0)
                        ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0000")));

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
}