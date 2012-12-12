package com.thoughtworks.orteroid.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.*;
import android.widget.ArrayAdapter;
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
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class AddIdeaActivity extends Activity {

    private String idea;
    private Board board;
    private ActionBar actionBar;
    private int selectedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String selectedPosition = intent.getStringExtra(Constants.SELECTED_POSITION);
        if (selectedPosition == null) {
            selectedIndex = 0;
        } else {
            selectedIndex = Integer.parseInt(selectedPosition);
        }
        board = intent.getParcelableExtra(Constants.BOARD);
        List<Section> listForDefault = new ArrayList<Section>(){{
            add(new Section("section1",0));
        }};
        if(board == null) board = new Board("test",2,listForDefault);             //TODO: what if board is null
        actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setIcon(R.drawable.ic_launcher);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setTitle(board.name());
        setContentView(R.layout.add_idea);
        setBackgroundLayout();
        setActionBar(board);
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
    }

    public void addAnIdea(View view) {
        final EditText ideaText = (EditText) findViewById(R.id.ideaMessage);
        idea = ideaText.getText().toString();
        postIdea();
        ideaText.setText("");
    }

    private void postIdea() {
        Callback callback = addIdeaCallback();
        int selectedNavigationIndex = actionBar.getSelectedNavigationIndex();
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


    private void setActionBar(final Board board) {
        List<Section> spinnerArray = board.sections();
        final List<String> sectionNames;
        actionBar.setSelectedNavigationItem(selectedIndex);
        sectionNames = inflateSpinner(spinnerArray);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(actionBar.getThemedContext(), android.R.layout.simple_spinner_dropdown_item, sectionNames);
        actionBar.setListNavigationCallbacks(spinnerArrayAdapter, new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                selectedIndex = itemPosition;
                setBackgroundLayout();
                return true;
            }
        });
    }

    private List<String> inflateSpinner(List<Section> spinnerArray) {
        List<String> sectionNames = new ArrayList<String>();
        for (Section section : spinnerArray) {
            sectionNames.add(section.name());
        }
        return sectionNames;
    }
}