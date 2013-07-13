package com.tobiasschuerg.tmf;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tobiasschuerg.tmf.rest.AsyncPostAnswers;

import org.json.JSONArray;

/**
 * Activity for showing feedback questions.
 * Created by Tobias on 07.06.13.
 */
public class AnswerActivity extends Activity {

    private TextView question;
    private EditText answerEdit;
    private Lecture lecture;
    private int currentQuestion;
    private int max;
    private Button next;
    private Button back;
    private ProgressBar bar;
    private int previousQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        question = (TextView) findViewById(R.id.textView);
        answerEdit = (EditText) findViewById(R.id.editText);
        next = (Button) findViewById(R.id.next);
        back = (Button) findViewById(R.id.back);
        bar = (ProgressBar) findViewById(R.id.progressBar);

        lecture = LectureActivity.lecture;
        lecture.reorder();
        setTitle(lecture.getId() + " | " + lecture.getName());

        previousQuestion = -1;
        currentQuestion = -1;
        max = lecture.getQuestions().size();
        bar.setMax(max);

        if (max > 0) {
            nextQuestion();
        } else {
            Toast.makeText(this, "There are no questions for this lecture", Toast.LENGTH_LONG).show();
            setResult(RESULT_CANCELED);
            finish();
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousQuestion();
            }
        });

    }

    private void nextQuestion() {
        previousQuestion = currentQuestion;
        currentQuestion++;

        if (currentQuestion < max) {
            setQuestion();
            checkButtons();
        } else {
            next.setEnabled(false);
            currentQuestion = max - 1;
            savePreviousAnswer();
            new AsyncPostAnswers() {

                @Override
                public void pushSuccessful(JSONArray jsonArray) {
                    setResult(RESULT_OK);
                    finish();
                }

                @Override
                public void pushFailed() {
                    Log.e("Answer", "Creation failed");
                    Toast.makeText(AnswerActivity.this, "Failed!", Toast.LENGTH_LONG).show();
                    next.setEnabled(true);
                }
            }.execute(lecture);
        }
    }

    private void previousQuestion() {
        previousQuestion = currentQuestion;
        currentQuestion--;
        if (currentQuestion < 0) {
            finish();
        } else {
            setQuestion();
            checkButtons();
        }
    }

    private void setQuestion() {
        if (previousQuestion >= 0) {
            savePreviousAnswer();
        }
        String number = "[" + (currentQuestion + 1) + "] ";
        question.setText(number + lecture.getQuestions().get(currentQuestion).getQuestion());
        answerEdit.setText(lecture.getQuestions().get(currentQuestion).getAnswer());
    }

    private void savePreviousAnswer() {
        Editable answer = answerEdit.getText();
        if (answer != null && lecture.getQuestions().size() > 0 && previousQuestion < lecture.getQuestions().size()) {
            lecture.getQuestions().get(previousQuestion).setAnswer(answer.toString());
        }
    }

    private void checkButtons() {
        bar.setProgress(currentQuestion + 1); // +1 since counting begins a 0

        // BACK button
        if (currentQuestion > 0) {
            back.setEnabled(true);
            next.setTextColor(Color.BLUE);
        } else {
            back.setEnabled(false);
            next.setTextColor(Color.BLACK);
        }

        // NEXT button
        if (currentQuestion < max - 1) {
            next.setText("Next");
            next.setTextColor(Color.BLUE);
        } else {
            next.setText("Submit feedback");
            next.setTextColor(Color.GREEN);
        }
    }
}
