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
import com.ideaboardz.android.models.Section;
import com.ideaboardz.android.repositories.BoardRepository;
import com.ideaboardz.android.utils.TestUtilities;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AddIdeaActivityTest extends BaseActivityTest<AddIdeaActivity> {

    public AddIdeaActivityTest() {
        super(AddIdeaActivity.class);
    }

    public void setUp() throws Exception {
        List<Section> listOfSections = new ArrayList<Section>();
        final Point point = new Point(1, 1, "point", 1, "2013/01/29 20:40:18 +0000");
        final Point secondPoint = new Point(1, 1, "point2", 1, "2013/01/29 20:40:18 +0000");
        final Point thirdPoint = new Point(2, 1, "point3", 1, "2013/01/29 20:40:18 +0000");
        final Point fourthPoint = new Point(1, 1, "point4", 1, "2013/01/29 20:40:18 +0000");
        Section section = new Section("What went well", 1);
        Section section2 = new Section("What did not go well", 2);
        section.addPoint(point);
        section.addPoint(secondPoint);
        section2.addPoint(thirdPoint);
        section.addPoint(fourthPoint);
        listOfSections.add(section);
        listOfSections.add(section2);
        BoardRepository boardRepository = mock(BoardRepository.class);
        BoardRepository.setBoardRepository(boardRepository);

        String boardName = "test";
        final Board board = new Board(boardName, 2, listOfSections);
        Intent intent = new Intent(getInstrumentation().getTargetContext(), AddIdeaActivity.class);
        intent.putExtra(Constants.SELECTED_POSITION, "1");
        intent.putExtra(Constants.BOARD, board);
        this.setActivityIntent(intent);
        super.setUp();
    }

    public void testShouldAddIdeaWhenSubmitPressed() {
        Activity activity = getActivity();
        EditText editText = (EditText) activity.findViewById(R.id.ideaMessage);
        String text = "Edited Text for new idea";

        Button submitButton = (Button) activity.findViewById(R.id.submitButton);
        TestUtilities.editTextAndSubmit(this, editText, text, submitButton);
        verify(BoardRepository.getInstance()).addIdea(eq(text), eq(2), (Callback) any());
    }


}
