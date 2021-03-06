package com.ideaboardz.android.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.ideaboardz.android.Callback;
import com.ideaboardz.android.R;
import com.ideaboardz.android.models.Board;
import com.ideaboardz.android.models.Point;
import com.ideaboardz.android.repositories.BoardRepository;
import com.ideaboardz.android.utilities.Font;
import com.ideaboardz.android.utilities.SectionNAmeListAdapter;
import com.ideaboardz.android.utilities.SharedData;
import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import static com.ideaboardz.android.constants.Constants.*;

public class ViewSectionActivity extends Activity {
    private String boardKey;
    private String boardId;
    private Board board;
    private Context context = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_section);
        Intent intent = getIntent();
        String urlOfBoard = intent.getDataString();
        setParameters(intent, urlOfBoard);
        TextView boardNameHeader = (TextView) findViewById(R.id.board_name_header);
        boardNameHeader.setText(decodeBoardKey());
        boardNameHeader.setTypeface(Font.setFontForIdea(this));
        if (board == null) {
            ProgressDialog dialog = ProgressDialog.show(ViewSectionActivity.this, null, "Fetching details of " + decodeBoardKey() + " board", true);
            final Callback<Board> boardCallback = viewBoardCallback(dialog);
            BoardRepository.getInstance().retrieveBoard(boardKey, boardId, boardCallback);
            dialog.setCancelable(true);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            });
            dialog.show();
        }

    }

    private Callback<List<Point>> viewPointsCallback() {
        final Context context = this;
        return new Callback<List<Point>>() {
            @Override
            public void execute(List<Point> points) {
                if (points != null) {
                    ViewSectionActivity.this.board.update(points);
                } else {
                    connectionIssueNotification();
                }
            }
        };
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    void addRecentBoardNameToSharedPreferences() {
        SharedData.add(boardId, boardKey, this);
        JSONArray jsonArray = SharedData.getJsonArrayOfRecentBoard();

        SharedPreferences sharedPreferences = getSharedPreferences(SharedData.PREFS_NAME, 0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("boards", jsonArray.toString());
        edit.commit();
    }

    private Callback<Board> viewBoardCallback(final ProgressDialog dialog) {
        final Activity activity = this;
        return new Callback<Board>() {
            @Override
            public void execute(Board board) {
                if (board != null) {
                    dialog.dismiss();
                    ViewSectionActivity.this.board = board;
                    ListView listView = (ListView) findViewById(android.R.id.list);
                    System.out.println(board.getSectionNames());
                    listView.setAdapter(new SectionNAmeListAdapter(context, board.getSectionNames(), activity));
                    addRecentBoardNameToSharedPreferences();
                    BoardRepository.getInstance().retrievePoints(boardKey, boardId, viewPointsCallback());
                } else {
                    connectionIssueNotification();
                }
            }
        };
    }

    private void connectionIssueNotification() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this)
                        .setTitle("Failed to connect to the board")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(ViewSectionActivity.this, MainActivity.class);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private String decodeBoardKey() {
        try {
            return URLDecoder.decode(boardKey, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void setParameters(Intent intent, String urlOfBoard) {
        if (urlOfBoard == null) {
            boardKey = intent.getStringExtra(BOARD_KEY);
            boardId = intent.getStringExtra(BOARD_ID);
        } else {
            boardId = extractURLFragment(urlOfBoard);
            urlOfBoard = urlOfBoard.substring(0, urlOfBoard.lastIndexOf('/'));
            boardKey = extractURLFragment(urlOfBoard);
        }
    }

    private String extractURLFragment(String url) {
        int lastIndex = url.lastIndexOf('/');
        return url.substring(lastIndex + 1, url.length());
    }

    public void viewPoints(View view) {
        TextView button = (TextView) view.findViewById(R.id.section_names);
        String selectedSectionName = button.getText().toString();
        int selectedSection = 0;
        for (String name : board.getSectionNames()) {
            if (name.equals(selectedSectionName)) selectedSection = board.getSectionNames().indexOf(name);
        }
        Intent intent = new Intent(this, ViewBoardActivity.class);
        intent.putExtra(BOARD, this.board);
        intent.putExtra(BOARD_KEY, this.boardKey);
        intent.putExtra(BOARD_ID, this.boardId);
        intent.putExtra(SELECTED_POSITION, selectedSection);
        startActivityForResult(intent, REQUEST_CODE);
    }


}
