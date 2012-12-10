package com.thoughtworks.orteroid.activities;

import android.app.ActionBar;
import android.widget.Button;
import android.widget.ListView;
import com.thoughtworks.orteroid.Callback;
import com.thoughtworks.orteroid.R;
import com.thoughtworks.orteroid.constants.Constants;
import com.thoughtworks.orteroid.models.Board;
import com.thoughtworks.orteroid.models.Point;
import com.thoughtworks.orteroid.models.Section;
import com.thoughtworks.orteroid.repositories.BoardRepository;
import com.thoughtworks.orteroid.utils.TestUtilities;
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

public class ViewBoardActivityTest extends BaseActivityTest<ViewBoardActivity> {

    public ViewBoardActivityTest() {
        super(ViewBoardActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        List<Section> listOfSections = new ArrayList<Section>();
        Point point = new Point(1, 1, "point");
        Point secondPoint = new Point(1, 1, "point2");
        Point thirdPoint = new Point(2, 1, "point3");
        Point fourthPoint = new Point(1, 1, "point4");
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

        final Board board = new Board("test", 2, listOfSections);

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((Callback) invocation.getArguments()[2]).execute(board);
                return null;
            }
        }).when(boardRepository).retrieveBoard(anyString(), anyString(), any(Callback.class));
        super.setUp();
    }

    public void testShouldListThePointsOfDefaultSection() {
        Button firstButton = (Button) activity.findViewById(R.id.row_text);

        ListView listView = (ListView) activity.findViewById(android.R.id.list);
        Button secondButton = (Button) listView.getChildAt(1).findViewById(R.id.row_text);
        Button thirdButton = (Button) listView.getChildAt(2).findViewById(R.id.row_text);

        assertEquals("point", firstButton.getText());
        assertEquals("point2", secondButton.getText());
        assertEquals("point4", thirdButton.getText());
    }

    public void testShouldFindTotalNumberOfNavigationItemsInActionBar() {
        ActionBar actionBar = activity.getActionBar();
        assertEquals(2, actionBar.getNavigationItemCount());
    }

    public void testShouldHaveDefaultSectionSelectedOnStart() {
        ActionBar actionBar = activity.getActionBar();
        int selectedIndex = actionBar.getSelectedNavigationIndex();
        assertEquals(0, selectedIndex);
    }

    public void testShouldShowIdeasOfOtherSection() {
        TestUtilities.navigateActionBarToIndex(activity.getActionBar(), 1, this);
        Button firstButton = (Button) activity.findViewById(R.id.row_text);

        assertEquals("point3", firstButton.getText());
    }

    public void testShouldNavigateToAddIdeaActivity() {

        TestUtilities.navigateActionBarToIndex(activity.getActionBar(), 1, this);

        Map<String, String> bundleExtras = new HashMap<String, String>();
        bundleExtras.put(Constants.SECTION_ID, "2");
        assertNavigationToTargetWithParameters(R.id.addButton, AddIdeaActivity.class, bundleExtras);
    }

}
