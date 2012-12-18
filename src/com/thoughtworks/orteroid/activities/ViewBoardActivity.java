package com.thoughtworks.orteroid.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import com.thoughtworks.orteroid.Callback;
import com.thoughtworks.orteroid.R;
import com.thoughtworks.orteroid.constants.Constants;
import com.thoughtworks.orteroid.models.Board;
import com.thoughtworks.orteroid.repositories.BoardRepository;
import com.thoughtworks.orteroid.utilities.ActionBarSetup;
import com.thoughtworks.orteroid.utilities.ColorSticky;
import com.thoughtworks.orteroid.utilities.SectionListAdapter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

public class ViewBoardActivity extends Activity {
    private ActionBar actionBar;
    private Board board;
    private Spinner spinner;
    String boardKey;
    String boardId;
    private int selectedIndex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_board);
        Intent intent = getIntent();
        String urlOfBoard = intent.getDataString();
        setParameters(intent, urlOfBoard);
        ProgressDialog dialog = ProgressDialog.show(ViewBoardActivity.this, null, "Fetching details of " + decodeBoardKey() + " board", true);
        dialog.show();
        setLayoutForDifferentVersions();
        BoardRepository.getInstance().retrieveBoard(boardKey, boardId, viewBoardCallback(dialog));
    }
    private void setParameters(Intent intent, String urlOfBoard) {
        if (urlOfBoard == null) {
            boardKey = intent.getStringExtra(Constants.BOARD_KEY);
            boardId = intent.getStringExtra(Constants.BOARD_ID);
            if (intent.getStringExtra(Constants.SELECTED_POSITION) != null) {
                selectedIndex = Integer.parseInt(intent.getStringExtra(Constants.SELECTED_POSITION));
            } else {
                selectedIndex = 0;
            }
        } else {

            boardId = extractURLFragment(urlOfBoard);
            urlOfBoard = urlOfBoard.substring(0, urlOfBoard.lastIndexOf('/'));
            boardKey = extractURLFragment(urlOfBoard);
        }
    }
    private void setLayoutForDifferentVersions() {
        if (Build.VERSION.SDK_INT <= Constants.VERSION_CODE_FOR_ANDROID_3) {
            useSpinner();
            spinner.setVisibility(View.VISIBLE);
        } else {
            actionBar = ActionBarSetup.useActionBar(this, true);
        }
    }

    private String decodeBoardKey() {
        try {
            return URLDecoder.decode(boardKey, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }



    private void useSpinner() {
        spinner = (Spinner) findViewById(R.id.spinnerForSections);
    }

    private String extractURLFragment(String url) {
        int lastIndex = url.lastIndexOf('/');
        return url.substring(lastIndex + 1, url.length());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addIdea(View view) {
        Intent intent = new Intent(this, AddIdeaActivity.class);
        Integer selectedIndex;
        if (actionBar == null) selectedIndex = spinner.getSelectedItemPosition();
        else selectedIndex = actionBar.getSelectedNavigationIndex();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.BOARD, this.board);
        intent.putExtra(Constants.SELECTED_POSITION, selectedIndex.toString());
        intent.putExtra(Constants.BOARD, ViewBoardActivity.this.board);
        startActivity(intent);
    }

    private Callback<Board> viewBoardCallback(final ProgressDialog dialog) {
        return new Callback<Board>() {
            @Override
            public void execute(Board board) {
                dialog.dismiss();
                ViewBoardActivity.this.board = board;
                if (actionBar == null) {
                    setSpinner(board);
                    setTitle(board.name());
                } else {
                    setActionBar(board);
                }
            }
        };
    }

    private void setActionBar(Board board) {
        actionBar.setListNavigationCallbacks(ActionBarSetup.setActionBar(board), actionBarNavigation(board));
        actionBar.setSelectedNavigationItem(selectedIndex);
        actionBar.setTitle(board.name());
    }

    private void setSpinner(final Board board) {
        List<String> sectionNames = board.getSectionNames();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sectionNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(selectedIndex);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int selected, long id) {
                int selectedSection = board.sections().get(selected).id();
                setPoints(board, selectedSection);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void setPoints(Board board, int selectedItem) {
        String colourCode = ColorSticky.getColorCode(selectedItem);
        SectionListAdapter sectionListAdapter = new SectionListAdapter(this, board.pointsOfSection(selectedItem), colourCode);
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(sectionListAdapter);
    }


    private ActionBar.OnNavigationListener actionBarNavigation(final Board board) {
        return new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                int selected = board.sections().get(itemPosition).id();
                setPoints(board, selected);
                return true;
            }
        };
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}