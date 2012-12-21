package com.thoughtworks.orteroid.utilities;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.thoughtworks.orteroid.models.Board;

import java.util.List;

public class SpinnerSetup {

    public static Spinner setSpinner(Context context,Board board, int selectedIndex,Spinner spinner) {
        List<String> sectionNames = board.getSectionNames();
        ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, sectionNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(selectedIndex);
        return spinner;
    }
}
