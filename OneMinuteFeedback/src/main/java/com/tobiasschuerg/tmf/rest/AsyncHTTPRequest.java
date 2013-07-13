package com.tobiasschuerg.tmf.rest;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;

/**
 * Abstract class for requesting http.
 * Created by Tobias on 07.06.13.
 */
public abstract class AsyncHTTPRequest<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    public static DefaultHttpClient httpclient = null;

    protected RESTResponse makePostRequest(String path, JSONObject params) throws Exception {
        if (httpclient == null) {
            // instantiates httpclient to make request
            httpclient = new DefaultHttpClient();
        }

        // url with the post data
        HttpPost httpost = new HttpPost(path);

        if (params != null) {
            // passes the results to a string builder/entity
            StringEntity se = new StringEntity(params.toString(), HTTP.UTF_8);
            // sets the post request as the resulting string
            httpost.setEntity(se);
        }

        // sets a request header so the page receving the request
        // will know what to do with it
        httpost.setHeader("Accept", "application/json");
        httpost.setHeader("Content-type", "application/json; charset=utf-8");

        // Handles what is returned from the page
        DeluxeRestResponseHandler responseHandler = new DeluxeRestResponseHandler();
        return httpclient.execute(httpost, responseHandler);
    }

    public RESTResponse postJSON(String path, JSONObject params) {
        RESTResponse response = null;
        try {
            Log.i("HttpRequest", "Posting request:\n" + params.toString());
            response = makePostRequest(path, params);
            Log.d("HttpRequest", response.getJsonObject().toString());
        } catch (UnknownHostException uhe) {
            response = new RESTResponse();
            try {
                response.setJsonObject(new JSONObject("status:unknown host"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            response.statusCode = 1000;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

}
