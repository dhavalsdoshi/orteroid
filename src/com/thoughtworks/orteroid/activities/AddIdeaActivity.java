package com.thoughtworks.orteroid.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.thoughtworks.orteroid.Callback;
import com.thoughtworks.orteroid.R;
import com.thoughtworks.orteroid.constants.Constants;
import com.thoughtworks.orteroid.models.Board;
import com.thoughtworks.orteroid.models.Section;
import com.thoughtworks.orteroid.repositories.BoardRepository;
import com.thoughtworks.orteroid.utilities.ActionBarSetup;
import com.thoughtworks.orteroid.utilities.ColorSticky;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class AddIdeaActivity extends Activity {

    private String idea;
    private Board board;
    private ActionBar actionBar;
    private Integer selectedIndex;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String selectedPosition = intent.getStringExtra(Constants.SELECTED_POSITION);
        setSelectedPosition(selectedPosition);
        board = intent.getParcelableExtra(Constants.BOARD);
        if (board == null) {
            board = getDefaultBoard();           //TODO: what if board is null
        }
        setContentView(R.layout.add_idea);
        setBackgroundLayout();
        setLayoutForDifferentVersions();
    }

    private void setLayoutForDifferentVersions() {
        if (Build.VERSION.SDK_INT <= Constants.VERSION_CODE_FOR_ANDROID_3) {
            useSpinner();
            spinner.setVisibility(View.VISIBLE);
        } else {
            setActionBar();
        }
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
        if (selectedPosition == null) {
            selectedIndex = 0;
        } else {
            selectedIndex = Integer.parseInt(selectedPosition);
        }
    }

    private void setActionBar() {
        actionBar = ActionBarSetup.useActionBar(this, true);
        actionBar.setTitle(board.name());
        actionBar.setListNavigationCallbacks(ActionBarSetup.setActionBar(board), actionBarNavigation());
        actionBar.setSelectedNavigationItem(selectedIndex);
    }

    private ActionBar.OnNavigationListener actionBarNavigation() {
        return new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                selectedIndex = itemPosition;
                setBackgroundLayout();
                return true;
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

    private void setBackgroundLayout() {
        EditText editText = (EditText) findViewById(R.id.ideaMessage);
        editText.setBackgroundResource(R.drawable.sticky);
        GradientDrawable drawable = (GradientDrawable) editText.getBackground();
        drawable.setColor(Color.parseColor(ColorSticky.getColorCode(board.sections().get(selectedIndex).id())));
        editText.invalidate();
    }

    public void addAnIdea(View view) {
        final EditText ideaText = (EditText) findViewById(R.id.ideaMessage);
        idea = null;
        idea = ideaText.getText().toString();
        if(idea.length() != 0){
        postIdea();
        }
        ideaText.setText(null);
    }

    private void postIdea() {
        Callback callback = addIdeaCallback();
        int selectedNavigationIndex;
        if (actionBar == null) selectedNavigationIndex = spinner.getSelectedItemPosition();
        else selectedNavigationIndex = actionBar.getSelectedNavigationIndex();
        Integer sectionId = board.sections().get(selectedNavigationIndex).id();

        BoardRepository.getInstance().addIdea(idea, sectionId, callback);
    }

    private Callback addIdeaCallback() {
        return new Callback<Boolean>() {
            @Override
            public void execute(Boolean result) throws JSONException {
                if (result) generateSuccessToast();
                else generateFailureNotification();
            }
        };
    }

    private void generateFailureNotification() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("The following idea failed :\n " + idea);
        builder.setNegativeButton("Try Resending", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                postIdea();
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
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

    private void useSpinner() {
        setTitle(board.name());
        spinner = (Spinner) findViewById(R.id.spinnerForIdeas);
        setSpinner();
    }

    private void setSpinner() {
        List<String> sectionNames = board.getSectionNames();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sectionNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(selectedIndex);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int selected, long id) {
                selectedIndex = selected;
                setBackgroundLayout();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ViewBoardActivity.class);
        intent.putExtra(Constants.BOARD_KEY, board.name().replace(" ", "%20"));
        intent.putExtra(Constants.BOARD_ID, board.id().toString());
        intent.putExtra(Constants.SELECTED_POSITION, selectedIndex.toString());
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}