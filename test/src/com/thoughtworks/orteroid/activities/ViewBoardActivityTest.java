package com.thoughtworks.orteroid.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
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
        final Point point = new Point(1, 1, "point",1);
        final Point secondPoint = new Point(1, 1, "point2",1);
        final Point thirdPoint = new Point(2, 1, "point3",1);
        final Point fourthPoint = new Point(1, 1, "point4",1);
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
        Intent intent = new Intent((getInstrumentation().getTargetContext()),ViewBoardActivity.class);
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
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((Callback) invocation.getArguments()[2]).execute(new ArrayList<Point>(){{
                    add(point);
                    add(secondPoint);
                    add(thirdPoint);
                    add(fourthPoint.clone());
                    add(new Point(1, 3, "point5",1));
                }});
                return null;
            }
        }).when(boardRepository).retrievePoints(anyString(), anyString(), any(Callback.class));
        this.setActivityIntent(intent);
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

    public void testShouldUpdateTheBoardWithNewIdea(){
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
        Button fourthIdea = (Button) listView.getChildAt(3).findViewById(R.id.row_text);

        assertEquals(4,listView.getCount());
        assertEquals("point5", fourthIdea.getText());

    }

    public void testShouldFindTotalNumberOfNavigationItemsInCorrespondingSpinner() {
        ActionBar actionBar;
        Spinner spinner;
        if(Build.VERSION.SDK_INT <= Constants.VERSION_CODE_FOR_ANDROID_3) {
            spinner = (Spinner) activity.findViewById(R.id.spinnerForSections);
            assertEquals(2, spinner.getCount());
        }
        else{
            actionBar = activity.getActionBar();
            assertEquals(2, actionBar.getNavigationItemCount());
        }
    }

    public void testShouldHaveDefaultSectionSelectedOnStart() {
        ActionBar actionBar;
        Spinner spinner;
        if(Build.VERSION.SDK_INT <= Constants.VERSION_CODE_FOR_ANDROID_3) {
            spinner = (Spinner) activity.findViewById(R.id.spinnerForSections);
            assertEquals(0, spinner.getSelectedItemPosition());
        }
        else{
            actionBar = activity.getActionBar();
            assertEquals(0, actionBar.getSelectedNavigationIndex());
        }
    }

    public void testShouldShowIdeasOfOtherSection() {
        if(Build.VERSION.SDK_INT <= Constants.VERSION_CODE_FOR_ANDROID_3) {
            Spinner spinner = (Spinner) activity.findViewById(R.id.spinnerForSections);
            TestUtilities.navigateSpinnerToIndex(spinner,1,this);
        }
        else{
            TestUtilities.navigateActionBarToIndex(activity.getActionBar(), 1, this);
        }

        Button firstButton = (Button) activity.findViewById(R.id.row_text);

        assertEquals("point3", firstButton.getText());
    }

    public void testShouldNavigateToAddIdeaActivity() {
        if(Build.VERSION.SDK_INT <= Constants.VERSION_CODE_FOR_ANDROID_3) {
            Spinner spinner = (Spinner) activity.findViewById(R.id.spinnerForSections);
            TestUtilities.navigateSpinnerToIndex(spinner,1,this);
        }
        else{
            TestUtilities.navigateActionBarToIndex(activity.getActionBar(), 1, this);
        }
        Map<String, String> bundleExtras = new HashMap<String, String>();
        bundleExtras.put(Constants.SELECTED_POSITION, "1");
        assertNavigationToTargetWithParameters(R.id.addButton, AddIdeaActivity.class, bundleExtras);
    }

    public void testShouldShowDeleteButtonOnLongPressOfSticky(){
        final Button button = (Button) activity.findViewById(R.id.row_text);

        try {
            runTestOnUiThread(new Runnable() {
                @Override
                public void run() {
                    button.performLongClick();
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        ImageButton deleteButton = (ImageButton) activity.findViewById(R.id.deleteButton);
        int visibility = deleteButton.getVisibility();

        assertEquals(View.VISIBLE, visibility);
    }

    public void testShouldNavigateToEditIdeaActivity() {
        assertNavigationToTarget(R.id.row_text, EditIdeaActivity.class);
    }

}
