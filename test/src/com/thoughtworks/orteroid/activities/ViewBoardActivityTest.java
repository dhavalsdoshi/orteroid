package com.thoughtworks.orteroid.activities;

import android.test.TouchUtils;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import com.thoughtworks.orteroid.Callback;
import com.thoughtworks.orteroid.R;
import com.thoughtworks.orteroid.models.Board;
import com.thoughtworks.orteroid.models.Point;
import com.thoughtworks.orteroid.models.Section;
import com.thoughtworks.orteroid.repositories.BoardRepository;
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
        BoardRepository boardRepository = mock(BoardRepository.class);
        BoardRepository.setBoardRepository(boardRepository);
        List<Section> listOfSections = new ArrayList<Section>();
        Point point = new Point(1, 1, "point");
        Point secondPoint = new Point(1, 1, "point2");
        Point fourthPoint = new Point(1, 1, "point4");
        Point thirdPoint = new Point(2, 1, "point3");
        Section section = new Section("What went well", 1);
        Section section2 = new Section("What did not go well", 2);
        section.addPoint(point);
        section.addPoint(secondPoint);
        section.addPoint(fourthPoint);
        section2.addPoint(thirdPoint);
        listOfSections.add(section);
        listOfSections.add(section2);
        final Board board = new Board("test", 2, "test description", listOfSections);

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((Callback) invocation.getArguments()[2]).execute(board);
                return null;
            }
        }).when(boardRepository).retrieveBoard(anyString(), anyString(), any(Callback.class));
        super.setUp();
    }

    public void testShouldShowSpinnerWithFirstSectionAsDefault() {
        assertEquals("What went well", ((Spinner) activity.findViewById(R.id.spinnerForSections)).getSelectedItem());
    }

    public void testShouldHaveSpinnerWithMultipleChoices() {
        Spinner spinner = (Spinner) activity.findViewById(R.id.spinnerForSections);
        assertEquals("What did not go well", spinner.getItemAtPosition(1));
    }

    public void testShouldListThePointsOfDefaultSection() {
        activity = getActivity();
        Button firstButton = (Button) activity.findViewById(R.id.row_text);

        ListView listView = (ListView) activity.findViewById(android.R.id.list);
        Button secondButton = (Button) listView.getChildAt(1).findViewById(R.id.row_text);
        Button thirdButton = (Button) listView.getChildAt(2).findViewById(R.id.row_text);

        assertEquals("point", firstButton.getText());
        assertEquals("point2", secondButton.getText());
        assertEquals("point4", thirdButton.getText());
    }

    public void testShouldListThePointsOfSelectedSection() {
        final Spinner spinner = (Spinner) activity.findViewById(R.id.spinnerForSections);
        try{
            runTestOnUiThread(new Runnable() {
                @Override
                public void run() {
                    spinner.setSelection(1);
                }
            });
        }catch (Throwable e){
            e.printStackTrace();
        }

        TouchUtils.tapView(this, spinner);
        sendKeys(KeyEvent.KEYCODE_DPAD_DOWN);
        sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);


        Button button = (Button)activity.findViewById(R.id.row_text);
        assertEquals("point3", button.getText());
    }
}
