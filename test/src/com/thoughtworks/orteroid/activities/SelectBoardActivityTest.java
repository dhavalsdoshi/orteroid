package com.thoughtworks.orteroid.activities;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import com.thoughtworks.orteroid.R;

public class SelectBoardActivityTest extends BaseActivityTest<SelectBoardActivity> {

    public SelectBoardActivityTest() {
        super(SelectBoardActivity.class);
    }

    public void testShouldContainEditTextWithDefaultHintWritten() {
        Activity activity = getActivity();
        EditText editText = (EditText) activity.findViewById(R.id.url);
        String defaultMessage = editText.getHint().toString();

        assertNotNull(editText);
        assertEquals("Enter url fragment here", defaultMessage);
    }

    public void testShouldContainSubmitButton() {
        Activity activity = getActivity();
        Button submitButton = (Button) activity.findViewById(R.id.goButton);
        String buttonMessage = submitButton.getText().toString();

        assertNotNull(submitButton);
        assertEquals("Go", buttonMessage);
    }
}
