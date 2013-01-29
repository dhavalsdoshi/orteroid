package com.thoughtworks.orteroid.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.*;
import com.thoughtworks.orteroid.Callback;
import com.thoughtworks.orteroid.R;
import com.thoughtworks.orteroid.models.Board;
import com.thoughtworks.orteroid.models.Point;
import com.thoughtworks.orteroid.repositories.BoardRepository;
import com.thoughtworks.orteroid.utilities.ColorSticky;
import com.thoughtworks.orteroid.utilities.CustomActionBar;
import com.thoughtworks.orteroid.utilities.SectionListAdapter;

import java.util.List;

import static com.thoughtworks.orteroid.constants.Constants.*;

public class ViewBoardActivity extends Activity {
    private CustomActionBar customActionBar;
    private Board board;
    private String boardKey;
    private String boardId;
    private int selectedPosition;
    SectionListAdapter sectionListAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_board);
        Intent intent = getIntent();
        board = intent.getParcelableExtra(BOARD);
        boardKey = intent.getStringExtra(BOARD_KEY);
        boardId = intent.getStringExtra(BOARD_ID);
        selectedPosition = intent.getIntExtra(SELECTED_POSITION, 0);
        customActionBar = new CustomActionBar(this, R.id.spinnerForSections, actionBarCallback());
        customActionBar.setActionBar(board, this);
        customActionBar.updateSelectedIndex(selectedPosition);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            int selectedPosition = data.getIntExtra(SELECTED_POSITION, customActionBar.selectedIndex());
            customActionBar.updateSelectedIndex(selectedPosition);
        }
        refresh(null);
    }

    public void refresh(View view) {
        ImageButton refreshButton = (ImageButton) findViewById(R.id.refreshButton);
        refreshButton.setVisibility(View.GONE);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        BoardRepository.getInstance().retrievePoints(boardKey, boardId, viewPointsCallback());
    }

    public void search(View view) {
        TextView searchView = (TextView) findViewById(R.id.searchText);
        searchView.setVisibility(View.VISIBLE);
        searchView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence textToSearch, int arg1, int arg2, int arg3) {
                sectionListAdapter.getFilter().filter(textToSearch);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                sectionListAdapter.getFilter().filter(arg0.toString());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private Callback<Integer> actionBarCallback() {
        return new Callback<Integer>() {
            @Override
            public void execute(Integer selectedSection) {
                setPoints(board, selectedSection);
            }
        };
    }

    public void addIdea(View view) {
        Intent intent = new Intent(this, AddIdeaActivity.class);
        intent.putExtra(SELECTED_POSITION, customActionBar.selectedIndex().toString());
        intent.putExtra(BOARD, this.board);
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void editIdea(View view) {
        View viewParent = (View) view.getParent();
        TextView textView = (TextView) viewParent.findViewById(R.id.idea_text);
        Intent intent = new Intent(this, EditIdeaActivity.class);
        Point selectedPoint;
        String message = textView.getText().toString();
        selectedPoint = board.getPointFromMessage(message, customActionBar.selectedIndex());
        intent.putExtra(SELECTED_POINT, selectedPoint);
        intent.putExtra(BOARD, board);
        intent.putExtra(SELECTED_POSITION, customActionBar.selectedIndex().toString());
        startActivityForResult(intent, REQUEST_CODE);
    }


    public void voteForIdea(View view) {
        View parent = (View) view.getParent();
        TextView textView = (TextView) parent.findViewById(R.id.idea_text);
        String message = textView.getText().toString();
        Point selectedPoint = board.getPointFromMessage(message, customActionBar.selectedIndex());
        Callback<Boolean> callback = voteIdeaCallback(view);
        BoardRepository.getInstance().voteForIdea(selectedPoint, callback);
        generateToastForVote();
    }

    private void generateToastForVote() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_layout_root));

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText("Voting...");
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    private Callback<Boolean> voteIdeaCallback(final View view) {
        return new Callback<Boolean>() {
            @Override
            public void execute(Boolean object) {
                refresh(null);
            }
        };
    }

    public void deleteIdea(View view) {
        generateConfirmationMessage(view);
    }

    private void delete(View view) {
        View parent = (View) view.getParent();
        parent.setVisibility(View.GONE);
        TextView textView = (TextView) parent.findViewById(R.id.idea_text);
        String message = textView.getText().toString();
        Point selectedPoint = board.getPointFromMessage(message, customActionBar.selectedIndex());
        Callback<Boolean> callback = deleteIdeaCallback(view);
        showDeletionToast();
        BoardRepository.getInstance().deletePoint(selectedPoint, callback);
    }

    private void generateConfirmationMessage(final View view) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this)
                        .setTitle("Are you sure you want to delete this Idea?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                delete(view);
                            }
                        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDeletionToast() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_layout_root));
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText("deleting idea");
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    private Callback<Boolean> deleteIdeaCallback(final View view) {
        return new Callback<Boolean>() {
            @Override
            public void execute(Boolean result) {
                if (result != null) {
                    refresh(null);
                } else {
                    connectionIssueNotification();
                }
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        TextView searchView = (TextView) findViewById(R.id.searchText);
        if (searchView.getVisibility() == View.VISIBLE) {
            searchView.setVisibility(View.GONE);
        } else {
            Intent intent = new Intent();
            setResult(Activity.RESULT_CANCELED, intent);
            super.onBackPressed();
        }
    }

    private Callback<List<Point>> viewPointsCallback() {
        final Context context = this;
        return new Callback<List<Point>>() {
            @Override
            public void execute(List<Point> points) {
                if (points != null) {
                    ImageButton refreshButton = (ImageButton) findViewById(R.id.refreshButton);
                    refreshButton.setVisibility(View.VISIBLE);
                    ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);
                    progressBar.setVisibility(View.GONE);
                    ViewBoardActivity.this.board.update(points);
                    customActionBar.setActionBar(board, context);
                } else {
                    connectionIssueNotification();
                }
            }
        };
    }


    private void setPoints(Board board, final int selectedItem) {
        String colourCode = ColorSticky.getColorCode(selectedItem);
        sectionListAdapter = new SectionListAdapter(this, board.pointsOfSection(selectedItem), colourCode);
        final ListView listView = (ListView) findViewById(android.R.id.list);
        int currentItem = listView.getFirstVisiblePosition();
        listView.setEmptyView(findViewById(R.id.no_ideas_added));
        listView.setAdapter(sectionListAdapter);
        listView.setSelectionFromTop(currentItem, 0);
    }

    private void connectionIssueNotification() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this)
                        .setTitle("Failed to connect to the board")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(ViewBoardActivity.this, MainActivity.class);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}