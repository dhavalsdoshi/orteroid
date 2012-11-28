package com.thoughtworks.orteroid;

import com.thoughtworks.orteroid.constants.Constants;

import java.util.HashMap;
import java.util.Map;

public class MainActivityTest extends BaseActivityTest<MainActivity> {

    public MainActivityTest() {
        super(MainActivity.class);
    }

    public void testShouldNavigateToDemoBoard() {
        Map<String,String> bundleExtras = new HashMap<String, String>();
        bundleExtras.put(Constants.BOARD_KEY,"test");
        assertNavigationToTargetWithParameters(R.id.viewTestBoard, ViewBoardActivity.class,bundleExtras );

    }
}
