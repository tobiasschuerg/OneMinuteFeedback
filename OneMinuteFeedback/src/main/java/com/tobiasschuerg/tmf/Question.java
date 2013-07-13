package com.tobiasschuerg.tmf;

import android.util.Log;

/**
 * Activity for managing the answering process.
 * Created by Tobias on 07.06.13.
 */
public class Question implements Comparable<Question> {

    public void setQuestion(String question) {
        mQuestion = question;
    }

    public void setId(long id) {
        mId = id;
    }

    public void setSortId(Long sortId) {
        this.sortId = sortId;
    }

    public long getSortId() {
        return sortId;
    }

    Long sortId = -1L;

    public Long getId() {
        return mId;
    }

    public String getQuestion() {
        return mQuestion;
    }

    private long mId;
    private String mQuestion;

    public String getAnswer() {
        return mAnswer;
    }

    public void setAnswer(String answer) {
        mAnswer = answer;
    }

    private String mAnswer;

    @Override
    public int compareTo(Question another) {
        Log.d("Question", "sortId: " + this.getSortId());
        if (this.getSortId() > another.getSortId()) {
            return 1;
        } else if (this.getSortId() < another.getSortId()) {
            return -1;
        } else {
            return 0;
        }
    }
}
