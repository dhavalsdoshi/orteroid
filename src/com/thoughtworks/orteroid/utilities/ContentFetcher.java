package com.thoughtworks.orteroid.utilities;

import android.os.AsyncTask;
import android.util.Log;
import com.thoughtworks.orteroid.Callback;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ContentFetcher extends AsyncTask<String,Void,JSONObject>{

    private Callback callback;

    public ContentFetcher(Callback callback) {
        this.callback = callback;
    }

    //TODO write functional tests

    @Override
    protected JSONObject doInBackground(String... urls) {
        for (String url : urls) {
            return response(url);
        }
        return null;
    }

    public JSONObject response(String url) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        String result = null;
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if(statusCode == 200){
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream content = httpEntity.getContent();
                result = toString(content);
            }
        } catch (IOException httpResponseError) {
            Log.e("HTTP Response", "IO error");
            result = "404 error";
        }
        try {
            return new JSONObject(result);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        callback.execute(jsonObject);
    }

    private String toString(InputStream content) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
        StringBuilder result = new StringBuilder();
        String line;
        try {
            while((line = reader.readLine()) != null){
                result.append(line);
            }
        } catch (IOException readerException) {
            readerException.printStackTrace();
        }
        return result.toString();
    }
}
