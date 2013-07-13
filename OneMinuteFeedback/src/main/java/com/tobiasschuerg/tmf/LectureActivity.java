package com.tobiasschuerg.tmf;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bugsense.trace.BugSenseHandler;
import com.tobiasschuerg.tmf.rest.AsyncGetLecture;
import com.tobiasschuerg.tmf.rest.RestParser;

import org.json.JSONObject;

public class LectureActivity extends Activity {

    private Long lectureID = 0L;
    public static Lecture lecture;
    private TextView textView;
    private EditText editText;
    private Button send;
    private boolean success = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BugSenseHandler.initAndStartSession(getApplicationContext(), "83712958");
        setContentView(R.layout.activity_feedback);

        textView = (TextView) findViewById(R.id.message_lecture_id);
        editText = (EditText) findViewById(R.id.id);
        send = (Button) findViewById(R.id.button);

        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                editText.setEnabled(false);
                send.setEnabled(false);

                try {
                    Editable isToParse = editText.getText();
                    if (isToParse != null) {
                        lectureID = Long.valueOf(isToParse.toString());
                    }
                } catch (NumberFormatException e) {
                    lectureID = 0L;
                }

                textView.setText("fetching lecture " + lectureID + "...");

                new AsyncGetLecture() {

                    @Override
                    public void requestSuccessful(JSONObject json) {
                        RestParser restParser = new RestParser();
                        lecture = restParser.parseLecture(json);

                        Intent intent = new Intent(getApplicationContext(), AnswerActivity.class);
                        startActivityForResult(intent, 1337);
                    }

                }.execute(lectureID);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!success) {
            send.setEnabled(true);
            editText.setEnabled(true);
            textView.setText("Enter your lecture id:");
            textView.setEnabled(true);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.feedback, menu);
//        setTitle("Two minute feedback");
//        return true;
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            editText.setVisibility(View.GONE);
            textView.setText("Thank you very much for your feedback!");
            textView.setTextColor(Color.BLUE);

            send.setText("Close\nOne Minute Feedback");
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            send.setEnabled(true);
            success = true;
        }
    }
}
