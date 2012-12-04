package com.thoughtworks.orteroid.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import com.thoughtworks.orteroid.Callback;
import com.thoughtworks.orteroid.R;
import com.thoughtworks.orteroid.constants.Constants;
import com.thoughtworks.orteroid.models.Board;
import com.thoughtworks.orteroid.models.Section;
import com.thoughtworks.orteroid.repositories.BoardRepository;
import com.thoughtworks.orteroid.utilities.SectionListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewBoardActivity extends Activity implements AdapterView.OnItemSelectedListener {
    private Board board;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_board);
        Intent intent = getIntent();
        String boardKey = intent.getStringExtra(Constants.BOARD_KEY);
        String boardId = intent.getStringExtra(Constants.BOARD_ID);
        new BoardRepository().retrieveBoard(boardKey, boardId, viewBoardCallback());
    }

    private Callback<Board> viewBoardCallback() {
        return new Callback<Board>() {
            @Override
            public void execute(Board board) {
                ViewBoardActivity.this.board = board;
                String selectedItem = setSpinner(board);
                setPoints(ViewBoardActivity.this.board,selectedItem);
            }
        };
    }



    private void setPoints(Board board, String selectedItem) {
        SectionListAdapter adapter = new SectionListAdapter(this, board.pointsOfSection(selectedItem));
        ListView listView = (ListView)findViewById(android.R.id.list);
        listView.setAdapter(adapter);
    }

    private String setSpinner(Board board) {
        List<Section> spinnerArray = board.sections();
        Spinner spinner = (Spinner)findViewById(R.id.spinnerForSections);
        List<String> sectionNames = inflateSpinner(spinnerArray);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sectionNames);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(this);
        return (String)spinner.getSelectedItem();
    }


    private List<String> inflateSpinner(List<Section> spinnerArray) {
        List<String> sectionNames = new ArrayList<String>();
        for (Section section : spinnerArray) {
            sectionNames.add(section.name());
        }
        return sectionNames;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String selected = parent.getItemAtPosition(pos).toString();
        setPoints(board,selected);

    }
    @Override
    public void onNothingSelected(AdapterView parent) {
        // Do nothing.
    }

}
