package com.github.hintofbasil.crabbler.BackgroundDataPost.DataPostHelpers;

import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by will on 27/05/16.
 */
public class RegisterHelper extends PostHelper {

    SharedPreferences prefs;

    public RegisterHelper(SharedPreferences prefs) {
        super("http://crab.napier.ac.uk/api/0.1/users");
        this.prefs = prefs;
    }

    @Override
    protected String getData() {
        return "{\"phone\":{\"phone_id\":\"10090801001000000\",\"role\":\"fisherman\"}}";
    }

    @Override
    public boolean successful() {
        if(responseData == null) {
            Log.d("RegisterHelper", "No response");
            return false;
        }
        try {
            JSONObject responseJson = new JSONObject(responseData);
            String id = responseJson.getString("phone_id");
            //TODO use contant
            prefs.edit().putString("PHONE_ID_KEY", id).commit();
            Log.i("RegisterHelper", "Phone id: " + id);
            return true;
        } catch (JSONException e) {
            Log.e("RegisterHelper", "Invalid response\n" + Log.getStackTraceString(e));
            return false;
        }
    }
}