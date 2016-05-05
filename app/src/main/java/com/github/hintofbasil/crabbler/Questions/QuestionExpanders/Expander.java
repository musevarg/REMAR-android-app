package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.hintofbasil.crabbler.Questions.QuestionActivity;
import com.github.hintofbasil.crabbler.Questions.QuestionReader;
import com.github.hintofbasil.crabbler.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

/**
 * Interface to expand a layout using a question JSON object
 */
public abstract class Expander<T> {

    AppCompatActivity activity;

    public Expander(AppCompatActivity activity) {
        this.activity = activity;
    }

    public abstract void expandLayout(JSONObject question) throws JSONException;

    protected void nextQuestion() {
        int question = activity.getIntent().getIntExtra(activity.getString(R.string.question_id_key), 0);
        //TODO handle last question
        final Intent intent = new Intent(activity, QuestionActivity.class);
        intent.putExtra(activity.getString(R.string.question_id_key), question + 1);
        new CountDownTimer(1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                activity.startActivity(intent);
                activity.finish();
            }
        }.start();
    }

    protected String[] getCurrentAnswers() throws IOException, JSONException {
        SharedPreferences prefs = activity.getSharedPreferences(activity.getString(R.string.saved_preferences_key), Context.MODE_PRIVATE);
        String answers = prefs.getString(activity.getString(R.string.answers_key), null);
        if(answers==null) {
            //Create empty answers string
            QuestionReader qr = new QuestionReader();
            int noAnswers = qr.getJsonQuestions(activity).length();
            char[] buffer = new char[noAnswers-1];
            Arrays.fill(buffer, ',');
            answers = new String(buffer);
        }
        return answers.split(",", -1);
    }

    protected String getCurrentAnswer(int id) throws IOException, JSONException {
        return getCurrentAnswers()[0];
    }

    protected void saveAnswer(T answer, int questionId) throws IOException, JSONException {
        String[] answers = getCurrentAnswers();
        answers[questionId] = answer.toString();
        StringBuilder sb = new StringBuilder();
        for(String a:answers) {
            sb.append(a);
            sb.append(',');
        }
        int len = sb.length();
        String newAnswers = sb.substring(0, len - 1);
        SharedPreferences prefs = activity.getSharedPreferences(activity.getString(R.string.saved_preferences_key), Context.MODE_PRIVATE);
        prefs.edit().putString(activity.getString(R.string.saved_preferences_key), newAnswers).apply();
        Log.i("Expander", "Questions saved - " + newAnswers);
    }

}
