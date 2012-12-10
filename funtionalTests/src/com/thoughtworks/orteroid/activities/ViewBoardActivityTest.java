package com.thoughtworks.orteroid.activities;

import android.test.ActivityInstrumentationTestCase2;
import com.jayway.android.robotium.solo.Solo;
import com.thoughtworks.orteroid.R;

public class ViewBoardActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;

    public ViewBoardActivityTest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testShouldNavigateToViewBoardActivity() {
        solo.assertCurrentActivity("Current Activity", MainActivity.class);

        solo.clickOnButton(solo.getString(R.string.option1));
        solo.assertCurrentActivity("should be ViewBoardActivity", ViewBoardActivity.class);

    }

    public void tearDown() {
        try {
            solo.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        getActivity().finish();
        try {
            super.tearDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

