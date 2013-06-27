package com.ideaboardz.android.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import com.ideaboardz.android.Callback;
import com.ideaboardz.android.R;
import com.ideaboardz.android.constants.Constants;
import com.ideaboardz.android.models.Board;
import com.ideaboardz.android.models.Point;
import com.ideaboardz.android.models.Section;
import com.ideaboardz.android.repositories.BoardRepository;
import com.ideaboardz.android.utils.TestUtilities;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

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
        final Point point = new Point(1, 1, "point", 1, "2013/01/29 20:40:18 +0000");
        final Point secondPoint = new Point(1, 1, "point2", 4, "2013/01/29 20:40:18 +0000");
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
        intent.putExtra(Constants.BOARD, board);

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((Callback) invocation.getArguments()[2]).execute(new ArrayList<Point>() {{
                    add(new Point(1, 1, "point10", 5, "2013/01/29 20:40:18 +0000"));
                    add(fourthPoint.clone());
                    add(new Point(1, 3, "point5", 3, "2013/01/29 20:40:18 +0000"));
                }});
                return null;
            }
        }).when(boardRepository).retrievePoints(anyString(), anyString(), any(Callback.class));

        this.setActivityIntent(intent);
        super.setUp();
    }

    public void testShouldSortByVotes() {
        TestUtilities.clickButton(R.id.sortButton,activity,this);
        Spinner sortSpinner = (Spinner) activity.findViewById(R.id.sortSpinner);
        assertEquals(sortSpinner.getVisibility(), View.VISIBLE);
        TestUtilities.navigateSpinnerToIndex(sortSpinner, 0, this);
        assertEquals(((TextView)activity.findViewById(R.id.vote_count)).getText(), "+4");
    }


    public void testShouldListThePointsOfDefaultSection() {
        TextView firstButton = (TextView) activity.findViewById(R.id.idea_text);

        ListView listView = (ListView) activity.findViewById(android.R.id.list);
        TextView secondButton = (TextView) listView.getChildAt(1).findViewById(R.id.idea_text);
        TextView thirdButton = (TextView) listView.getChildAt(2).findViewById(R.id.idea_text);

        assertEquals("point", firstButton.getText());
        assertEquals("point2", secondButton.getText());
        assertEquals("point4", thirdButton.getText());
    }

    public void testShouldUpdateTheBoardWithNewIdea() {
        final ImageButton button = (ImageButton) activity.findViewById(R.id.refreshButton);

        try {
            runTestOnUiThread(new Runnable() {
                @Override
                public void run() {
                    button.performClick();
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        this.getInstrumentation().waitForIdleSync();


        ListView listView = (ListView) activity.findViewById(android.R.id.list);
        TextView fourthIdea = (TextView) listView.getChildAt(2).findViewById(R.id.idea_text);
        TextView firstButton = (TextView) activity.findViewById(R.id.idea_text);

        assertEquals(3, listView.getCount());
        assertEquals("point10", firstButton.getText());
        assertEquals("+5", ((TextView) listView.findViewById(R.id.vote_count)).getText());
        assertEquals("point5", fourthIdea.getText());

    }


    public void testShouldFindTotalNumberOfNavigationItemsInCorrespondingSpinner() {
        ActionBar actionBar;
        Spinner spinner;
        if (Build.VERSION.SDK_INT <= Constants.VERSION_CODE_FOR_ANDROID_3) {
            spinner = (Spinner) activity.findViewById(R.id.spinnerForSections);
            assertEquals(2, spinner.getCount());
        } else {
            actionBar = activity.getActionBar();
            assertEquals(2, actionBar.getNavigationItemCount());
        }
    }

    public void testShouldHaveDefaultSectionSelectedOnStart() {
        ActionBar actionBar;
        Spinner spinner;
        if (Build.VERSION.SDK_INT <= Constants.VERSION_CODE_FOR_ANDROID_3) {
            spinner = (Spinner) activity.findViewById(R.id.spinnerForSections);
            assertEquals(0, spinner.getSelectedItemPosition());
        } else {
            actionBar = activity.getActionBar();
            assertEquals(0, actionBar.getSelectedNavigationIndex());
        }
    }

    public void testShouldShowIdeasOfOtherSection() {
        if (Build.VERSION.SDK_INT <= Constants.VERSION_CODE_FOR_ANDROID_3) {
            Spinner spinner = (Spinner) activity.findViewById(R.id.spinnerForSections);
            TestUtilities.navigateSpinnerToIndex(spinner, 1, this);
        } else {
            TestUtilities.navigateActionBarToIndex(activity.getActionBar(), 1, this);
        }

        TextView firstButton = (TextView) activity.findViewById(R.id.idea_text);

        assertEquals("point3", firstButton.getText());
    }
}
