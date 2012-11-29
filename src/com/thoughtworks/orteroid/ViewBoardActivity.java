package com.thoughtworks.orteroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.thoughtworks.orteroid.constants.Constants;
import org.json.JSONArray;
import org.json.JSONObject;

public class ViewBoardActivity extends Activity implements HTTPRequester{

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_board);
        Intent intent = getIntent();
        String boardKey = intent.getStringExtra(Constants.BOARD_KEY);
        String boardId = intent.getStringExtra(Constants.BOARD_ID);
        new ContentFetcher(this).response(new URLGenerator().getBoardURL(boardKey,boardId));
    }

    @Override
    public void callback(JSONObject jsonObject) {

    }
}
