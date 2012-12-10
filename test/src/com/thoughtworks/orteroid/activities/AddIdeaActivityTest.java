package com.thoughtworks.orteroid.activities;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import com.thoughtworks.orteroid.R;

public class AddIdeaActivityTest extends BaseActivityTest<AddIdeaActivity> {

    public AddIdeaActivityTest() {
        super(AddIdeaActivity.class);
    }

    public void testShouldContainEditTextWithMessageEnterYourIdea() {
        Activity activity = getActivity();
        EditText editText = (EditText) activity.findViewById(R.id.ideaMessage);
        String defaultMessage = editText.getText().toString();

        assertNotNull(editText);
        assertEquals("Enter your idea", defaultMessage);
    }

    public void testShouldContainSubmitButton() {
        Activity activity = getActivity();
        Button submitButton = (Button) activity.findViewById(R.id.submitButton);
        String buttonMessage = submitButton.getText().toString();

        assertNotNull(submitButton);
        assertEquals("Submit", buttonMessage);
    }
}
