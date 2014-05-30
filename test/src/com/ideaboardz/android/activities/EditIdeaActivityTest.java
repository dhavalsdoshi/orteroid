package com.ideaboardz.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import com.ideaboardz.android.Callback;
import com.ideaboardz.android.R;
import com.ideaboardz.android.constants.Constants;
import com.ideaboardz.android.models.Board;
import com.ideaboardz.android.models.Point;
import com.ideaboardz.android.repositories.BoardRepository;
import com.ideaboardz.android.utils.TestUtilities;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class EditIdeaActivityTest extends BaseActivityTest<EditIdeaActivity> {

    Point point;

    public EditIdeaActivityTest() {
        super(EditIdeaActivity.class);
    }
    String oldmessage = "dummy text";

    public void setUp() throws Exception {
        point = new Point(1, 1, oldmessage, 1, "2013/01/29 20:40:18 +0000");
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
        assertEquals(oldmessage, existingText);
    }

    public void testShouldEditIdeaWhenSubmitPressed() {
        Activity activity = getActivity();
        EditText editText = (EditText) activity.findViewById(R.id.editIdea);
        String text = "Edited Text";


        Button submitButton = (Button) activity.findViewById(R.id.submitEdit);
        TestUtilities.editTextAndSubmit(this, editText, text, submitButton);
        verify(BoardRepository.getInstance()).editIdea(eq(text), eq(1), eq(oldmessage), (Callback) any());
    }

}
