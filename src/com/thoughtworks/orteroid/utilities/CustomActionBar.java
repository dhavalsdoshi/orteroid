package com.thoughtworks.orteroid.utilities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.thoughtworks.orteroid.Callback;
import com.thoughtworks.orteroid.R;
import com.thoughtworks.orteroid.constants.Constants;
import com.thoughtworks.orteroid.models.Board;
import com.thoughtworks.orteroid.models.Section;

import java.util.ArrayList;
import java.util.List;

public class CustomActionBar {

    private Spinner spinner;
    private ActionBar actionBar;
    private Activity activity;
    private Callback<Integer> actionBarCallback;
    private Integer selectedIndex;

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
        Integer selectedIndex;
        if (actionBar == null) selectedIndex = spinner.getSelectedItemPosition();
        else selectedIndex = actionBar.getSelectedNavigationIndex();
        return selectedIndex;
    }

    public  ActionBar useActionBar(boolean setNavigationMode) {
        actionBar = activity.getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setIcon(R.drawable.ic_launcher);
        if(setNavigationMode)actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
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

    public  Spinner setSpinner(Context context,Board board) {
        List<String> sectionNames = board.getSectionNames();
        ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, sectionNames);
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



    private void setNavigationOfSpinner(final Board board){
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
        actionBarCallback.execute(selectedSection);
    }


    public void updateSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
        if(actionBar == null)
            spinner.setSelection(selectedIndex);
        else
            actionBar.setSelectedNavigationItem(selectedIndex);

    }

    public ArrayAdapter<String> actionBarFor(final Board board) {
        List<Section> spinnerArray = board.sections();
        List<String> sectionNames1 = new ArrayList<String>();
        for (Section section : spinnerArray) {
            sectionNames1.add(section.name());
        }
        final List<String> sectionNames = sectionNames1;
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(actionBar.getThemedContext(), android.R.layout.simple_spinner_dropdown_item, sectionNames);
        return spinnerArrayAdapter;
    }


}
