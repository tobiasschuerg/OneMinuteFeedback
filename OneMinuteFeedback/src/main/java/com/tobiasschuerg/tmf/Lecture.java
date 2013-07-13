package com.tobiasschuerg.tmf;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Lecture data object.
 * Created by Tobias on 07.06.13.
 */
public class Lecture {

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addQuestion(Question question) {
        mQuestions.add(question);
    }

    public ArrayList<Question> getQuestions() {
        return mQuestions;
    }

    long id = -1;

    String name = "";
    final ArrayList<Question> mQuestions = new ArrayList<Question>();

    @Override
    public String toString() {
        String string;
        string = getName() + "\n";

        for (Question q : mQuestions) {
            string += q.getQuestion() + "\n";
        }

        return string;
    }

    public void reorder() {
        Collections.sort(mQuestions);
    }


}
