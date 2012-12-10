package com.thoughtworks.orteroid.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.thoughtworks.orteroid.Callback;
import com.thoughtworks.orteroid.R;
import com.thoughtworks.orteroid.constants.Constants;
import com.thoughtworks.orteroid.models.Board;
import com.thoughtworks.orteroid.models.Section;
import com.thoughtworks.orteroid.repositories.BoardRepository;
import com.thoughtworks.orteroid.utilities.SectionListAdapter;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class AddIdeaActivity extends Activity {

    private int sectionId;
    private String idea;
    private Board board;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String sectionId = intent.getStringExtra(Constants.SECTION_ID);
        if (sectionId == null) {
            this.sectionId = 0;
        } else {
            this.sectionId = Integer.parseInt(sectionId);
        }
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            board = bundle.getParcelable(Constants.BOARD);
        }
        setContentView(R.layout.add_idea);
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setTitle(board.name());
        setActionBar(board);
    }

    public void addAnIdea(View view) {
        final EditText ideaText = (EditText) findViewById(R.id.ideaMessage);
        idea = ideaText.getText().toString();
        postIdea();
        ideaText.setText("");
    }

    private void postIdea() {
        Callback callback = addIdeaCallback();
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

    private void setPoints(Board board, int selectedItem) {
        int colourCode = selectedItem % 6;
        SectionListAdapter sectionListAdapter = new SectionListAdapter(this, board.pointsOfSection(selectedItem), colourCode);
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(sectionListAdapter);
    }

    private void setActionBar(final Board board) {
        List<Section> spinnerArray = board.sections();
        final List<String> sectionNames = inflateSpinner(spinnerArray);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(actionBar.getThemedContext(), android.R.layout.simple_spinner_dropdown_item, sectionNames);
        actionBar.setListNavigationCallbacks(spinnerArrayAdapter, new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                int selected = board.sections().get(itemPosition).id();
                setPoints(board, selected);
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