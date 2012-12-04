package com.thoughtworks.orteroid.utilities;

import android.os.AsyncTask;
import android.util.Log;
import com.thoughtworks.orteroid.Callback;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ContentFetcher extends AsyncTask<String, Void, String> {

    private Callback callback;

    public ContentFetcher(Callback callback) {
        this.callback = callback;
    }

    //TODO write functional tests

    @Override
    protected String doInBackground(String... urls) {
        for (String url : urls) {
            return response(url);
        }
        return null;
    }

    public String response(String url) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        String result = null;
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream content = httpEntity.getContent();
                result = toString(content);
            }
        }
        catch (IOException httpResponseError) {
            Log.e("HTTP Response", "IO error");
            result = "404 error";
        }
        return result;
    }

    @Override
    protected void onPostExecute(String resultString) {
        super.onPostExecute(resultString);
        try {
            callback.execute(resultString);
        } catch (JSONException e) {
           throw new RuntimeException(e.getMessage());
        }

    }

    private String toString(InputStream content) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
        StringBuilder result = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException readerException) {
            readerException.printStackTrace();
        }
        return result.toString();
    }
}
