package com.thoughtworks.orteroid.utils;

import android.app.Activity;
import android.test.InstrumentationTestCase;
import android.test.TouchUtils;
import android.widget.Button;
import android.widget.EditText;

public class TestUtilities {

    private static void setEditText(final EditText editText, final String text, Activity currentActivity, InstrumentationTestCase instrumentationTestCase) {
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                editText.setText(text);
            }
        });
        instrumentationTestCase.getInstrumentation().waitForIdleSync();
    }

    public static void setEditText(int id, String text, Activity currentActivity, InstrumentationTestCase instrumentationTestCase) {
        EditText editText = (EditText) currentActivity.findViewById(id);
        setEditText(editText, text, currentActivity, instrumentationTestCase);
    }

    public static void clickButton(int id, Activity activity, InstrumentationTestCase instrumentationTestCase) {
        Button view = (Button) activity.findViewById(id);
        TouchUtils.clickView(instrumentationTestCase, view);
    }


}