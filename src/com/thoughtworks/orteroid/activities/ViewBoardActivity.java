package com.thoughtworks.orteroid.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.thoughtworks.orteroid.Callback;
import com.thoughtworks.orteroid.R;
import com.thoughtworks.orteroid.constants.Constants;
import com.thoughtworks.orteroid.models.Board;
import com.thoughtworks.orteroid.models.Section;
import com.thoughtworks.orteroid.repositories.BoardRepository;
import com.thoughtworks.orteroid.utilities.SectionListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewBoardActivity extends Activity {
    private ActionBar actionBar;
    private Board board;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_board);
        Intent intent = getIntent();
        String boardKey = intent.getStringExtra(Constants.BOARD_KEY);
        String boardId = intent.getStringExtra(Constants.BOARD_ID);
        ProgressDialog dialog = ProgressDialog.show(ViewBoardActivity.this, null, "Fetching details of " + boardKey + " board", true);
        dialog.show();
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setTitle(boardKey);
        BoardRepository.getInstance().retrieveBoard(boardKey, boardId, viewBoardCallback(dialog));
    }

    public void addIdea(View view) {
        Intent intent = new Intent(this, AddIdeaActivity.class);
        int selectedIndex = actionBar.getSelectedNavigationIndex();
        Integer selectedSectionId = board.sections().get(selectedIndex).id();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.BOARD,this.board);
        intent.putExtra(Constants.SECTION_ID, selectedSectionId.toString());
        intent.putExtra(Constants.BOARD,ViewBoardActivity.this.board);
        startActivity(intent);
    }


    private Callback<Board> viewBoardCallback(final ProgressDialog dialog) {  //TODO: dialog is smell....
        return new Callback<Board>() {
            @Override
            public void execute(Board board) {
                dialog.dismiss();
               ViewBoardActivity.this.board = board;
                setActionBar(board);
            }
        };
    }

    private void setPoints(Board board, int selectedItem) {
        int colourCode = selectedItem % 6;
        SectionListAdapter sectionListAdapter = new SectionListAdapter(this, board.pointsOfSection(selectedItem), colourCode);
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(sectionListAdapter);
    }

    private void setActionBar(final Board board) {
        List<Section> spinnerArray = board.sections();
        final List<String> sectionNames = inflateSpinner(spinnerArray);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(actionBar.getThemedContext(), android.R.layout.simple_spinner_dropdown_item, sectionNames);
        actionBar.setListNavigationCallbacks(spinnerArrayAdapter, new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                int selected = board.sections().get(itemPosition).id();
                setPoints(board, selected);
                return true;
            }
        });
    }

    private List<String> inflateSpinner(List<Section> spinnerArray) {
        List<String> sectionNames = new ArrayList<String>();
        for (Section section : spinnerArray) {
            sectionNames.add(section.name());
        }
        return sectionNames;
    }
}
