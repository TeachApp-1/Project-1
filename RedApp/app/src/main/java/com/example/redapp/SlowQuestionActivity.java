package com.example.redapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;

public class SlowQuestionActivity extends AppCompatActivity {

    private TextView question, qCount, timer, bookmarkTextView;
    private Button option1, option2, option3, option4, clearPick;
    private ImageView bookmark, checkAnswer;
    private Button redButton, greenButton, blueButton, shareButton;
    private ConstraintLayout layout;
    private List<Question> questionList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slow_question);

        question = findViewById(R.id.slow_question);
        qCount = findViewById(R.id.slow_quest_num);
        timer = findViewById(R.id.slow_timer);

        option1 = findViewById(R.id.slow_option1);
        option2 = findViewById(R.id.slow_option2);
        option3 = findViewById(R.id.slow_option3);
        option4 = findViewById(R.id.slow_option4);

        clearPick = findViewById(R.id.slow_delete_pick);

        checkAnswer = findViewById(R.id.slow_question_menu);

        bookmark = findViewById(R.id.slow_bookmark_question);
        bookmarkTextView = findViewById(R.id.bookmark_text_view);

        redButton = findViewById(R.id.slow_red_button);
        greenButton = findViewById(R.id.slow_green_button);
        blueButton = findViewById(R.id.slow_blue_button);
        layout = findViewById(R.id.constraintLayout6);
        shareButton = findViewById(R.id.slow_share_button);

        SpannableStringBuilder ssb = new SpannableStringBuilder(bookmarkTextView.getText().toString());
        ssb.setSpan(new ImageSpan(this, R.drawable.ic_slow_bookmark_fixed),ssb.length() - 1, ssb.length() ,0);

        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                option2.setBackgroundResource(R.drawable.round_corner);
                option3.setBackgroundResource(R.drawable.round_corner);
                option4.setBackgroundResource(R.drawable.round_corner);
                option1.setBackgroundResource(R.drawable.slow_pick_background);
            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                option1.setBackgroundResource(R.drawable.round_corner);
                option3.setBackgroundResource(R.drawable.round_corner);
                option4.setBackgroundResource(R.drawable.round_corner);
                option2.setBackgroundResource(R.drawable.slow_pick_background);
            }
        });

        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                option1.setBackgroundResource(R.drawable.round_corner);
                option2.setBackgroundResource(R.drawable.round_corner);
                option4.setBackgroundResource(R.drawable.round_corner);
                option3.setBackgroundResource(R.drawable.slow_pick_background);
            }
        });

        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                option1.setBackgroundResource(R.drawable.round_corner);
                option2.setBackgroundResource(R.drawable.round_corner);
                option3.setBackgroundResource(R.drawable.round_corner);
                option4.setBackgroundResource(R.drawable.slow_pick_background);
            }
        });

        clearPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                option1.setBackgroundResource(R.drawable.round_corner);
                option2.setBackgroundResource(R.drawable.round_corner);
                option3.setBackgroundResource(R.drawable.round_corner);
                option4.setBackgroundResource(R.drawable.round_corner);
            }
        });

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bookmarkTextView.getText().toString() == " ")
                    bookmarkTextView.setText(ssb);
                else
                    bookmarkTextView.setText(" ");
            }
        });

        checkAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SlowQuestionActivity.this, CheckAnswersMenu.class);
                startActivity(intent);
            }
        });
/*
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareSub = questionList.get(quesNum).getQuestion();
                String shareHelp =  "1. " + questionList.get(quesNum).getOptionA() + "\n" +
                        "2. " + questionList.get(quesNum).getOptionB() + "\n" +
                        "3. " + questionList.get(quesNum).getOptionC() + "\n" +
                        "4. " + questionList.get(quesNum).getOptionD() + "\n";
                String shareBody = shareHelp;
                intent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(intent, "Share using"));
            }
        });
*/
        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setBackgroundResource(R.drawable.red_background);
            }
        });

        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setBackgroundResource(R.drawable.green_background);
            }
        });

        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setBackgroundResource(R.drawable.blue_background);
            }
        });
    }

}