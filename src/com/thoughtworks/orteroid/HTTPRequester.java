package com.thoughtworks.orteroid;

import org.json.JSONArray;
import org.json.JSONObject;

public interface HTTPRequester {
    public void callback(JSONObject jsonObject);
}
