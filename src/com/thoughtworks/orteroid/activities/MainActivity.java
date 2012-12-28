package com.thoughtworks.orteroid.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.thoughtworks.orteroid.R;
import com.thoughtworks.orteroid.constants.Constants;
import com.thoughtworks.orteroid.utilities.Font;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonForDemo = (Button) findViewById(R.id.viewTestBoard);
        Button buttonForFaq = (Button) findViewById(R.id.faq);
        Button buttonForFeedback = (Button) findViewById(R.id.feedback);
        Button buttonForViewing = (Button) findViewById(R.id.viewBoard);
        buttonForDemo.setTypeface(Font.setFont(this));
        buttonForViewing.setTypeface(Font.setFont(this));
        buttonForFaq.setTypeface(Font.setFont(this));
        buttonForFeedback.setTypeface(Font.setFont(this));

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
        final Intent intent = new Intent(this, ViewBoardActivity.class);
        alertForDetails(intent);
    }

    public void faq(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://www.ideaboardz.com/page/faq"));
        startActivity(browserIntent);
    }

    public void feedback(View view){
        Intent intent = new Intent(this, ViewBoardActivity.class);
        intent.putExtra(Constants.BOARD_KEY, "feedback");
        intent.putExtra(Constants.BOARD_ID, "1");
        startActivity(intent);
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
                    intent.putExtra(Constants.BOARD_KEY, boardKey);
                    intent.putExtra(Constants.BOARD_ID, boardId);
                    startActivity(intent);
                }
            }
        });

        alert.show();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        super.onBackPressed();
    }
}
