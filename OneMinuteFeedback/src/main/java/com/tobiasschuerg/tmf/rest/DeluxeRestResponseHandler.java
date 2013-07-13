package com.tobiasschuerg.tmf.rest;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DeluxeRestResponseHandler implements ResponseHandler<RESTResponse> {

    @Override
    public RESTResponse handleResponse(HttpResponse response) throws IOException {
        RESTResponse rr = new RESTResponse();
        rr.setStatus(response.getStatusLine());
        Log.d("Response handler", "Status:\n" + rr.getStatus().getReasonPhrase());

        if (response.getEntity() != null) {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuilder builder = new StringBuilder();
            for (String line; (line = reader.readLine()) != null; ) {
                builder.append(line).append("\n");
            }
            JSONTokener tokener = new JSONTokener(builder.toString());
            Log.d("Response", builder.toString());
            try {
                rr.setJsonObject(new JSONObject(tokener));
            } catch (JSONException e) {
                Log.d("JSON parsing", "received json was no valid JSONObject.\n" + "Trying to parse as a JSONArray.");
                try {
                    tokener = new JSONTokener(builder.toString());
                    rr.setJsonArray(new JSONArray(tokener));
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }
        }
        return rr;
    }
}

class RESTResponse {
    private JSONArray jsonArray;
    private JSONObject jsonObject;
    private StatusLine status;
    int statusCode;

    public StatusLine getStatus() {
        return status;
    }

    public void setStatus(StatusLine status) {
        this.status = status;
        this.statusCode = status.getStatusCode();
    }

    public JSONObject getJsonObject() {
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JSONArray getJsonArray() {
        if (jsonArray == null) {
            jsonArray = new JSONArray();
        }
        return jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public String getMessage() {
        String message;
        try {
            message = getJsonObject().getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
            message = "";
        }
        return message;
    }
}
