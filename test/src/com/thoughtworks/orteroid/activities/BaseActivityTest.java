package com.thoughtworks.orteroid.activities;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import com.thoughtworks.orteroid.utils.TestUtilities;

import java.util.HashMap;
import java.util.Map;


public class BaseActivityTest<T extends Activity> extends ActivityInstrumentationTestCase2<T> {

    protected T activity;

    public BaseActivityTest(Class<T> activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        activity = getActivity();
    }

    protected void assertNavigationToTarget(int buttonIdToBeClicked, Class<? extends Activity> targetClass) {
        assertNavigationToTargetWithParameters(buttonIdToBeClicked, targetClass, new HashMap<String, String>());
    }

    protected void assertNavigationToTargetWithParameters(int buttonIdToBeClicked, Class<? extends Activity> targetClass, Map<String, String> bundleExtras) {
        Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(targetClass.getName(), null, false);
        TestUtilities.clickButton(buttonIdToBeClicked, activity, this);
        Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);
        assertEquals(targetClass, nextActivity.getClass());
        for (String key : bundleExtras.keySet()) {
            assertEquals(nextActivity.getIntent().getStringExtra(key), bundleExtras.get(key));
        }
        nextActivity.finish();
    }

}