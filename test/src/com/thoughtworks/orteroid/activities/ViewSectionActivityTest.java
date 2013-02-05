package com.thoughtworks.orteroid.activities;

import android.content.Intent;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.thoughtworks.orteroid.Callback;
import com.thoughtworks.orteroid.R;
import com.thoughtworks.orteroid.constants.Constants;
import com.thoughtworks.orteroid.models.Board;
import com.thoughtworks.orteroid.models.Point;
import com.thoughtworks.orteroid.models.Section;
import com.thoughtworks.orteroid.repositories.BoardRepository;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

public class ViewSectionActivityTest extends BaseActivityTest<ViewSectionActivity> {
    public ViewSectionActivityTest() {
        super(ViewSectionActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
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
        Intent intent = new Intent((getInstrumentation().getTargetContext()), ViewBoardActivity.class);
        intent.putExtra(Constants.BOARD_KEY, boardName);
        String boardId = "2";
        intent.putExtra(Constants.BOARD_ID, boardId);

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((Callback) invocation.getArguments()[2]).execute(board);
                return null;
            }
        }).when(boardRepository).retrieveBoard(anyString(), anyString(), any(Callback.class));

        this.setActivityIntent(intent);
        super.setUp();
    }

    public void testShouldHaveBoardName() {
        TextView firstButton = (TextView) activity.findViewById(R.id.board_name_header);
        assertEquals("test", firstButton.getText());
    }

    public void testShouldHaveBoardSectionNames() {

        ListView listView = (ListView) activity.findViewById(android.R.id.list);
        TextView firstButton = (TextView) listView.findViewById(R.id.section_names);

        assertEquals("What went well", firstButton.getText());
    }
}
