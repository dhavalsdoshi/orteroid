package com.thoughtworks.orteroid.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.thoughtworks.orteroid.Callback;
import com.thoughtworks.orteroid.R;
import com.thoughtworks.orteroid.constants.Constants;
import com.thoughtworks.orteroid.models.Board;
import com.thoughtworks.orteroid.models.Section;
import com.thoughtworks.orteroid.repositories.BoardRepository;
import com.thoughtworks.orteroid.utilities.SectionListAdapter;

public class ViewBoardActivity extends ListActivity implements Callback<Board> {

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
        ArrayAdapter<Section> adapter = new SectionListAdapter(this, board.sections());
        ListView listView = (ListView)findViewById(android.R.id.list);
        listView.setAdapter(adapter);
    }
}
