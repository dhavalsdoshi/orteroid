package com.thoughtworks.orteroid.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import com.thoughtworks.orteroid.R;
import com.thoughtworks.orteroid.constants.Constants;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void viewDemo(View view) {
        Intent intent = new Intent(this, ViewBoardActivity.class);
        intent.putExtra(Constants.BOARD_KEY, "test");
        intent.putExtra(Constants.BOARD_ID, "2");
        startActivity(intent);
    }
    public void viewBoard(View view) {
        Intent intent = new Intent(this,SelectBoardActivity.class);
        startActivity(intent);
    }

}
