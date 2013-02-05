package com.thoughtworks.orteroid.activities;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import com.thoughtworks.orteroid.Callback;
import com.thoughtworks.orteroid.R;
import com.thoughtworks.orteroid.constants.Constants;
import com.thoughtworks.orteroid.models.Board;
import com.thoughtworks.orteroid.models.Point;
import com.thoughtworks.orteroid.repositories.BoardRepository;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class EditIdeaActivityTest extends BaseActivityTest<EditIdeaActivity> {

    Point point;

    public EditIdeaActivityTest() {
        super(EditIdeaActivity.class);
    }

    public void setUp() throws Exception {
        point = new Point(1, 1, "dummy text", 1, "2013/01/29 20:40:18 +0000");
        BoardRepository boardRepository = mock(BoardRepository.class);
        BoardRepository.setBoardRepository(boardRepository);
        Board board = new Board("dummy", 2, null);
        Intent intent = new Intent(getInstrumentation().getTargetContext(), EditIdeaActivity.class);
        intent.putExtra(Constants.SELECTED_POINT, point);
        intent.putExtra(Constants.BOARD, board);
        intent.putExtra(Constants.SELECTED_POSITION, "3");
        this.setActivityIntent(intent);
        super.setUp();
    }

    public void testShouldContainStickyWithExistingStickyContent() {
        Activity activity = getActivity();
        EditText editText = (EditText) activity.findViewById(R.id.editIdea);
        String existingText = editText.getText().toString();

        assertNotNull(editText);
        assertEquals("dummy text", existingText);
    }

    public void testShouldEditIdeaWhenSubmitPressed() {
        Activity activity = getActivity();
        EditText editText = (EditText) activity.findViewById(R.id.editIdea);
        String text = "Edited Text";


        Button submitButton = (Button) activity.findViewById(R.id.submitEdit);
        editTextAndSubmit(editText, text, submitButton);
        verify(BoardRepository.getInstance()).editIdea(eq(text), eq(1), (Callback) any());
    }

    private void editTextAndSubmit(final EditText editText, final String text, final Button submitButton) {
        try {
            runTestOnUiThread(new Runnable() {
                @Override
                public void run() {
                    editText.setText(text);
                    submitButton.performClick();
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
