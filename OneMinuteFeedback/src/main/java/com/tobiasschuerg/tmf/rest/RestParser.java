package com.tobiasschuerg.tmf.rest;

import android.util.Log;

import com.tobiasschuerg.tmf.Lecture;
import com.tobiasschuerg.tmf.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Parsing rest answers.
 * Created by Tobias on 07.06.13.
 */
public class RestParser {

    public Lecture parseLecture(JSONObject json) {
        Log.d("JSON", json.toString());
        Lecture lecture = new Lecture();

        try {
            lecture.setId(json.getLong("id"));
            lecture.setName(json.getString("name"));

            JSONArray jqs = json.getJSONArray("questions");
            for (int i = 0; i < jqs.length(); i++) {
                JSONObject js = jqs.getJSONObject(i);

                Question question = new Question();
                question.setId(js.getLong("id"));
                question.setQuestion(js.getString("question"));
                question.setSortId(js.getLong("sortId"));
                if (question.getQuestion().length() > 0) {
                    lecture.addQuestion(question);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return lecture;
    }
}
