package com.thoughtworks.orteroid.activities;

import com.thoughtworks.orteroid.R;
import com.thoughtworks.orteroid.constants.Constants;

import java.util.HashMap;
import java.util.Map;

public class MainActivityTest extends BaseActivityTest<MainActivity> {

    public MainActivityTest() {
        super(MainActivity.class);
    }

    public void testShouldNavigateToDemoBoard() {
        Map<String, String> bundleExtras = new HashMap<String, String>();
        bundleExtras.put(Constants.BOARD_KEY, "test");
        bundleExtras.put(Constants.BOARD_ID, "2");
        assertNavigationToTargetWithParameters(R.id.viewTestBoard, ViewSectionActivity.class, bundleExtras);
    }

    public void testShouldNavigateToFeedbackBoard() {
        Map<String, String> bundleExtras = new HashMap<String, String>();
        bundleExtras.put(Constants.BOARD_KEY, "Feedback");
        bundleExtras.put(Constants.BOARD_ID, "6733");
        assertNavigationToTargetWithParameters(R.id.feedback, ViewSectionActivity.class, bundleExtras);
    }
}
