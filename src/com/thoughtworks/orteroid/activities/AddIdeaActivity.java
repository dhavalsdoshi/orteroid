package com.thoughtworks.orteroid.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.*;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.thoughtworks.orteroid.Callback;
import com.thoughtworks.orteroid.R;
import com.thoughtworks.orteroid.constants.Constants;
import com.thoughtworks.orteroid.models.Board;
import com.thoughtworks.orteroid.models.Section;
import com.thoughtworks.orteroid.repositories.BoardRepository;
import com.thoughtworks.orteroid.utilities.ColorSticky;
import com.thoughtworks.orteroid.utilities.CustomActionBar;

import java.util.ArrayList;
import java.util.List;

public class AddIdeaActivity extends Activity {

    private String idea;
    private Board board;
    private CustomActionBar customActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        customActionBar = new CustomActionBar(this, R.id.spinnerForIdeas, actionBarCallback());
        String selectedPosition = intent.getStringExtra(Constants.SELECTED_POSITION);
        setSelectedPosition(selectedPosition);
        board = intent.getParcelableExtra(Constants.BOARD);
        if (board == null) {
            board = getDefaultBoard();           //TODO: what if board is null
        }
        setContentView(R.layout.add_idea);
        setBackgroundLayout();
        customActionBar.setActionBar(board, this);
    }

    private Callback<Integer> actionBarCallback() {
        return new Callback<Integer>() {
            @Override
            public void execute(Integer object) {
                setBackgroundLayout();
            }
        };
    }

    private Board getDefaultBoard() {
        List<Section> listForDefault = new ArrayList<Section>() {{
            add(new Section("section1", 0));
        }};
        String boardName = "test";
        int boardId = 2;
        return new Board(boardName, boardId, listForDefault);
    }

    private void setSelectedPosition(String selectedPosition) {
        int selectedIndex;
        if (selectedPosition == null) {
            selectedIndex = 0;
        } else {
            selectedIndex = Integer.parseInt(selectedPosition);
        }
        customActionBar.updateSelectedIndex(selectedIndex);
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

    private void setBackgroundLayout() {
        EditText editText = (EditText) findViewById(R.id.ideaMessage);
        editText.setBackgroundResource(R.drawable.sticky);
        GradientDrawable drawable = (GradientDrawable) editText.getBackground();
        drawable.setColor(Color.parseColor(ColorSticky.getColorCode(board.sections().get(customActionBar.selectedIndex()).id())));
        editText.invalidate();
    }

    public void addAnIdea(View view) {
        final EditText ideaText = (EditText) findViewById(R.id.ideaMessage);
        idea = ideaText.getText().toString();
        if(idea.length() != 0){
            postIdea();
        }
        ideaText.setText(null);
    }

    private void postIdea() {
        Callback<Boolean> callback = addIdeaCallback();
        Integer sectionId = board.sections().get(customActionBar.selectedIndex()).id();
        BoardRepository.getInstance().addIdea(idea, sectionId, callback);
    }

    private Callback<Boolean> addIdeaCallback() {
        return new Callback<Boolean>() {
            @Override
            public void execute(Boolean result) {
                if (result) generateSuccessToast();
                else generateFailureNotification();
            }
        };
    }

    private void generateFailureNotification() {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this)
                   .setTitle("The following idea failed :\n " + idea)
                   .setNegativeButton("Try Resending", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                postIdea();
                                dialog.dismiss();
                            }
                        })
                   .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                           dialog.dismiss();
                       }
                   });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void generateSuccessToast() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_layout_root));

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText("Idea posted successfully");
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ViewBoardActivity.class);
        intent.putExtra(Constants.BOARD_KEY, board.name().replace(" ", "%20"));
        intent.putExtra(Constants.BOARD_ID, board.id().toString());
        intent.putExtra(Constants.SELECTED_POSITION, customActionBar.selectedIndex().toString());
        startActivity(intent);
        super.onBackPressed();
    }
}