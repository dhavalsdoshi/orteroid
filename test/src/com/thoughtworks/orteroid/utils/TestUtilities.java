package com.thoughtworks.orteroid.utils;

import android.app.ActionBar;
import android.app.Activity;
import android.test.InstrumentationTestCase;
import android.test.TouchUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

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

        if (!(activity.findViewById(id) instanceof Button)) {
            ImageButton view = (ImageButton) activity.findViewById(id);
            TouchUtils.clickView(instrumentationTestCase, view);
        } else {
            Button view = (Button) activity.findViewById(id);
            TouchUtils.clickView(instrumentationTestCase, view);
        }
    }


    public static void navigateActionBarToIndex(final ActionBar actionBar, final int indexToSelect, InstrumentationTestCase instrumentationTestCase) {
        try {
            instrumentationTestCase.runTestOnUiThread(new Runnable() {
                @Override
                public void run() {

                    actionBar.setSelectedNavigationItem(indexToSelect);
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
        instrumentationTestCase.getInstrumentation().waitForIdleSync();
    }
    public static void navigateSpinnerToIndex(final Spinner spinner, final int indexToSelect, InstrumentationTestCase instrumentationTestCase) {
        try {
            instrumentationTestCase.runTestOnUiThread(new Runnable() {
                @Override
                public void run() {

                    spinner.setSelection(indexToSelect);
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
        instrumentationTestCase.getInstrumentation().waitForIdleSync();
    }
}