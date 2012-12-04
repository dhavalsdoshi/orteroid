package com.thoughtworks.orteroid.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

public class ViewBoardActivity extends Activity implements Callback<Board> {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_board);
        Intent intent = getIntent();
        String boardKey = intent.getStringExtra(Constants.BOARD_KEY);
        String boardId = intent.getStringExtra(Constants.BOARD_ID);
        new BoardRepository().retrieveBoard(boardKey, boardId, this);
    }

    @Override
    public void execute(Board board) {
        setSpinner(board);
        setPoints(board);
    }

    private void setPoints(Board board) {
        SectionListAdapter adapter = new SectionListAdapter(this, board.names());
        ListView listView = (ListView)findViewById(android.R.id.list);
        listView.setAdapter(adapter);
    }

    private void setSpinner(Board board) {
        List<Section> spinnerArray = board.sections();
        Spinner spinner = (Spinner)findViewById(R.id.spinnerForSections);
        List<String> sectionNames = inflateSpinner(spinnerArray);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sectionNames);
        spinner.setAdapter(spinnerArrayAdapter);
    }

    private List<String> inflateSpinner(List<Section> spinnerArray) {
        List<String> sectionNames = new ArrayList<String>();
        for (Section section : spinnerArray) {
            sectionNames.add(section.name());
        }
        return sectionNames;
    }
}
