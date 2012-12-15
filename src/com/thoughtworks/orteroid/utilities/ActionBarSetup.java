package com.thoughtworks.orteroid.utilities;

import android.app.ActionBar;
import android.app.Activity;
import android.widget.ArrayAdapter;
import com.thoughtworks.orteroid.R;
import com.thoughtworks.orteroid.models.Board;
import com.thoughtworks.orteroid.models.Section;

import java.util.ArrayList;
import java.util.List;

public class ActionBarSetup {
    private static ActionBar actionBar;

    public static ActionBar useActionBar(Activity activity, boolean setNavigationMode) {
        actionBar = activity.getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setIcon(R.drawable.ic_launcher);
        if(setNavigationMode)actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        return actionBar;

    }
    public static ArrayAdapter<String> setActionBar(final Board board) {
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
