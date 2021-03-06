package com.ideaboardz.android.utilities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.ideaboardz.android.Callback;
import com.ideaboardz.android.R;
import com.ideaboardz.android.activities.ViewBoardActivity;
import com.ideaboardz.android.constants.Constants;
import com.ideaboardz.android.models.Board;
import com.ideaboardz.android.models.Section;

import java.util.ArrayList;
import java.util.List;

public class CustomActionBar {

    private Spinner spinner;
    private ActionBar actionBar;
    private Activity activity;
    private Callback<Integer> actionBarCallback;
    private Integer selectedIndex = 0;

    public CustomActionBar(Activity activity, int spinnerId, Callback<Integer> actionBarCallback) {
        this.activity = activity;
        this.actionBarCallback = actionBarCallback;
        if (Build.VERSION.SDK_INT <= Constants.VERSION_CODE_FOR_ANDROID_3) {
            spinner = (Spinner) activity.findViewById(spinnerId);
            spinner.setVisibility(View.VISIBLE);
        } else {
            actionBar = useActionBar(true);
        }

    }

    public Integer selectedIndex() {
        return actionBar == null ? spinner.getSelectedItemPosition() : actionBar.getSelectedNavigationIndex();
    }

    public ActionBar useActionBar(boolean setNavigationMode) {
        actionBar = activity.getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setIcon(R.drawable.ideaboardz_icon);
        if (setNavigationMode) actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        return actionBar;

    }

    public void setActionBar(Board board, Context context) {
        if (actionBar == null) {
            setSpinner(context, board);
            setNavigationOfSpinner(board);
            activity.setTitle(board.name());
        } else {
            setActionBar(board);
        }
    }

    public Spinner setSpinner(Context context, Board board) {
        List<String> sectionNames = board.getSectionNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, sectionNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(selectedIndex);
        return spinner;
    }

    private void setActionBar(Board board) {
        actionBar.setListNavigationCallbacks(actionBarFor(board), actionBarNavigation(board));
        actionBar.setSelectedNavigationItem(selectedIndex);
        actionBar.setTitle(board.name());
    }


    private void setNavigationOfSpinner(final Board board) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int itemPosition, long id) {
                onActionBarItemSelected(itemPosition, board);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private ActionBar.OnNavigationListener actionBarNavigation(final Board board) {
        return new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                onActionBarItemSelected(itemPosition, board);
                return true;
            }
        };
    }

    private void onActionBarItemSelected(int itemPosition, Board board) {
        int selectedSection = board.sections().get(itemPosition).id();
        selectedIndex = itemPosition;
        if(activity.getClass() == ViewBoardActivity.class) {
            makeSearchTextInvisible();
        }
        actionBarCallback.execute(selectedSection);
    }

    private void makeSearchTextInvisible() {
        TextView searchView = (TextView) activity.findViewById(R.id.searchText);
        if (searchView.getVisibility() == View.VISIBLE) {
            searchView.setVisibility(View.GONE);
        }
    }


    public void updateSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
        if (actionBar == null)
            spinner.setSelection(selectedIndex);
        else
            actionBar.setSelectedNavigationItem(selectedIndex);

    }

    public ArrayAdapter<String> actionBarFor(final Board board) {
        List<Section> spinnerArray = board.sections();
        List<String> sectionNames = new ArrayList<String>();
        for (Section section : spinnerArray) {
            sectionNames.add(section.name());
        }
        return new ArrayAdapter<String>(actionBar.getThemedContext(), android.R.layout.simple_spinner_dropdown_item, sectionNames);
    }


}
