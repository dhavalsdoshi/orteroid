package com.thoughtworks.orteroid.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.thoughtworks.orteroid.R;
import com.thoughtworks.orteroid.constants.Constants;
import com.thoughtworks.orteroid.utilities.Font;
import com.thoughtworks.orteroid.utilities.JSONParser;
import com.thoughtworks.orteroid.utilities.SharedData;

public class MainActivity extends Activity {

    private MainActivity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonForDemo = (Button) findViewById(R.id.viewTestBoard);
        Button buttonForFaq = (Button) findViewById(R.id.faq);
        Button buttonForFeedback = (Button) findViewById(R.id.feedback);
        Button buttonForViewing = (Button) findViewById(R.id.viewBoard);
        Button buttonForRecentBoards = (Button) findViewById(R.id.recent);
        buttonForDemo.setTypeface(Font.setFont(this));
        buttonForViewing.setTypeface(Font.setFont(this));
        buttonForFaq.setTypeface(Font.setFont(this));
        buttonForFeedback.setTypeface(Font.setFont(this));
        buttonForRecentBoards.setTypeface(Font.setFont(this));

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void viewDemo(View view) {
        Intent intent = new Intent(this, ViewBoardActivity.class);
        startViewBoardActivity("test", "2", intent);
    }

    public void viewBoard(View view) {
        final Intent intent = new Intent(this, ViewBoardActivity.class);
        alertForDetails(intent);
    }

    public void faq(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://www.ideaboardz.com/page/faq"));
        startActivity(browserIntent);
    }

    public void recent(View view) {
        alertForRecentBoards();
    }

    private void alertForRecentBoards() {

        SharedPreferences sharedPreferences = getSharedPreferences(SharedData.PREFS_NAME, 0);
        String data = sharedPreferences.getString("boards", null);
        final String[] recentBoardNames = JSONParser.parseStringToRecentBoardsName(data);
        final String[] recentBoardId = JSONParser.parseStringToRecentBoardsId(data);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Make your selection");
        builder.setItems(recentBoardNames, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Intent intent = new Intent(context,ViewBoardActivity.class);
                startViewBoardActivity(recentBoardNames[item],recentBoardId[item],intent);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void feedback(View view) {
        Intent intent = new Intent(this, ViewBoardActivity.class);
        startViewBoardActivity("feedback", "1", intent);
    }

    private void alertForDetails(final Intent intent) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LayoutInflater li = LayoutInflater.from(this);
        final View promptsView = li.inflate(R.layout.select_board, null);
        alert.setView(promptsView);
        alert.setPositiveButton("Go", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                EditText boardKeyText = (EditText) promptsView.findViewById(R.id.boardKey);
                EditText boardIdText = (EditText) promptsView.findViewById(R.id.boardId);
                String boardKey = boardKeyText.getText().toString();
                String boardId = boardIdText.getText().toString();
                if (boardId != null && boardKey != null) {
                    startViewBoardActivity(boardKey, boardId, intent);
                }
            }
        });

        alert.show();
    }

    private void startViewBoardActivity(String boardKey, String boardId, Intent intent) {
        intent.putExtra(Constants.BOARD_KEY, boardKey);
        intent.putExtra(Constants.BOARD_ID, boardId);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        super.onBackPressed();
    }
}
