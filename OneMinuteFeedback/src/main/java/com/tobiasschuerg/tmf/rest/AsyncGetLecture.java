package com.tobiasschuerg.tmf.rest;

import android.util.Log;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Abstract class for fetching lectures.
 * Created by Tobias on 07.06.13.
 */
public abstract class AsyncGetLecture extends AsyncHTTPRequest<Long, Void, RESTResponse> {

    @Override
    protected RESTResponse doInBackground(Long... ids) {
        String path = "http://tmf3-rwth.rhcloud.com/rest/lecture/get";
        Long id = ids[0];

        JSONObject params = new JSONObject();
        RESTResponse response = null;
        try {
            params.put("id", id);
            response = super.postJSON(path, params);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(RESTResponse result) {
        if (result != null) {

            switch (result.statusCode) {
                case HttpStatus.SC_UNAUTHORIZED:
                    // TODO:
                case HttpStatus.SC_OK:
                    requestSuccessful(result.getJsonObject());
                    break;

                default:
                    Log.d("RESULT", "code: " + result.statusCode);
                    // requestFailed();
                    break;
            }

        }
        super.onPostExecute(result);
    }

    public abstract void requestSuccessful(JSONObject jsonArray);
}