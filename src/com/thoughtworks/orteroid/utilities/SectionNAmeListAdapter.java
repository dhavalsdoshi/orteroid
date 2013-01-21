package com.thoughtworks.orteroid.utilities;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import com.thoughtworks.orteroid.R;

import java.util.List;

public class SectionNameListAdapter extends ArrayAdapter<String> {
    private final Context context;
    View rowView;
    private final List<String> sectionNames;

    public SectionNameListAdapter(Context context, List<String> objects) {
        super(context, R.layout.board_view_layout, R.id.section_names, objects);
        this.context = context;
        this.sectionNames = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        rowView = layoutInflater.inflate(R.layout.board_view_layout, parent, false);
        Button button = (Button) rowView.findViewById(R.id.section_names);
        button.setText(sectionNames.get(position));
        return rowView;
    }
}
