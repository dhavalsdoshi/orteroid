package com.thoughtworks.orteroid.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.thoughtworks.orteroid.R;
import com.thoughtworks.orteroid.constants.Constants;

public class SelectBoardActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_board);
    }

    public void navigateToUrl(View view){
        EditText editText = (EditText) findViewById(R.id.url);
        String url = editText.getText().toString();
        String boardKey = url.substring(0, url.indexOf('/'));
        String boardId = url.substring(url.indexOf('/')+1,url.length());
        Intent intent = new Intent(this, ViewBoardActivity.class);
        intent.putExtra(Constants.BOARD_KEY, boardKey);
        intent.putExtra(Constants.BOARD_ID, boardId);
        startActivity(intent);
    }

}
