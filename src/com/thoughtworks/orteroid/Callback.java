package com.thoughtworks.orteroid;

import org.json.JSONException;

public interface Callback<T> {
    public void execute(T object);
}
