package com.thoughtworks.orteroid;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import com.thoughtworks.orteroid.constants.Constants;

public class MainActivity extends Activity {

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

    public void viewDemo(View view){
        Intent intent = new Intent(this, ViewBoardActivity.class);
        intent.putExtra(Constants.BOARD_KEY,"test");
        startActivity(intent);
    }

}
