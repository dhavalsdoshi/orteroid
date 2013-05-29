package com.thoughtworks.orteroid.utilities;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.thoughtworks.orteroid.R;

import java.util.List;

public class SectionNameListAdapterTemp extends ArrayAdapter<String> {
    private final Context context;
    View rowView;
    private final List<String> sectionNames;
    private final Activity activity;

    public SectionNameListAdapterTemp(Context context, List<String> objects, Activity activity) {
        super(context, R.layout.board_view_layout, R.id.section_names, objects);
        this.context = context;
        this.sectionNames = objects;
        this.activity = activity;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        rowView = layoutInflater.inflate(R.layout.board_view_layout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.section_names);
        textView.setText(sectionNames.get(position));
        textView.setTextSize(20);
        textView.setTypeface(Font.setFontForIdea(activity));
        return rowView;
    }
}
