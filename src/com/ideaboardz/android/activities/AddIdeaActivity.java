package com.ideaboardz.android.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.*;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.ideaboardz.android.Callback;
import com.ideaboardz.android.R;
import com.ideaboardz.android.constants.Constants;
import com.ideaboardz.android.models.Board;
import com.ideaboardz.android.repositories.BoardRepository;
import com.ideaboardz.android.utilities.ColorSticky;
import com.ideaboardz.android.utilities.CustomActionBar;

public class AddIdeaActivity extends Activity {

    private String idea;
    private Board board;
    private CustomActionBar customActionBar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_idea);
        Intent intent = getIntent();
        customActionBar = new CustomActionBar(this, R.id.spinnerForIdeas, actionBarCallback());
        String selectedPosition = intent.getStringExtra(Constants.SELECTED_POSITION);
        setSelectedPosition(selectedPosition);
        board = intent.getParcelableExtra(Constants.BOARD);
        setBackgroundLayout();
        customActionBar.setActionBar(board, this);
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
            public void execute(Integer object) {
                setBackgroundLayout();
            }
        };
    }

    private void setSelectedPosition(String selectedPosition) {
        customActionBar.updateSelectedIndex(selectedPosition == null ? 0 : Integer.parseInt(selectedPosition));
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
        String rawIdea = ideaText.getText().toString();
        idea = convertLineBreakToSpace(rawIdea);
        if (idea.length() != 0) {
            postIdea();
            progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.VISIBLE);
        }
        ideaText.setText(null);
    }

    private String convertLineBreakToSpace(String rawIdea) {
        idea = rawIdea.replaceAll("\n", " ");
        return idea;
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
                progressBar.setVisibility(View.INVISIBLE);
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
        Intent intent = new Intent();
        intent.putExtra(Constants.SELECTED_POSITION, customActionBar.selectedIndex());
        setResult(Activity.RESULT_CANCELED, intent);
        super.onBackPressed();
    }
}