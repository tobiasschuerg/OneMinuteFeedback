package com.tobiasschuerg.tmf.rest;

import android.util.Log;

import com.tobiasschuerg.tmf.Lecture;
import com.tobiasschuerg.tmf.Question;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Abstract class for posting rest replies.
 * Created by Tobias on 11.06.13.
 */
public abstract class AsyncPostAnswers extends AsyncHTTPRequest<Lecture, Void, RESTResponse> {

    @Override
    protected RESTResponse doInBackground(Lecture... input) {
        Lecture lecture = input[0];
        RESTResponse response = null;

        if (lecture != null) {
            String path = "http://tmf3-rwth.rhcloud.com/rest/answer/create";
            JSONObject params = new JSONObject();

            try {
                params.put("lectureId", lecture.getId());
                ArrayList<Question> questions = lecture.getQuestions();
                JSONArray answers = new JSONArray();
                for (Question q : questions) {
                    if (q.getAnswer() != null) {
                        JSONObject answer = new JSONObject();
                        answer.put("answer", q.getAnswer());
                        answer.put("questionId", q.getId());
                        answers.put(answer);
                    }
                }
                params.put("answers", answers);
                response = super.postJSON(path, params);
                Log.d("AsyncAnswer", "got response");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return response;
    }


    @Override
    protected void onPostExecute(RESTResponse result) {
        if (result != null) {
            Log.d("AsyncAnswer", "status: " + result.statusCode);
            switch (result.statusCode) {
                case HttpStatus.SC_OK:
                    Log.d("PUSH", "ok");
                    pushSuccessful(result.getJsonArray());
                    // TODO
                    break;

                default:
                    Log.d("PUSH", "status code: " + result.statusCode);
                    Log.d("PUSH", "message: " + result.getJsonObject());
                    pushFailed();
                    break;
            }
        } else {
            pushFailed();
        }
        super.onPostExecute(result);
    }

    public abstract void pushSuccessful(JSONArray jsonArray);

    public abstract void pushFailed();
}
