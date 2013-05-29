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

public class BoardListAdapterTemp extends ArrayAdapter {
    private String[] names = null;
    private final Context context;
    private View view;

    public BoardListAdapterTemp(Context context, String[] objects) {
        super(context, R.layout.alert_box_names, R.id.nameOfSection, objects);
        this.context = context;
        this.names = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        view = layoutInflater.inflate(R.layout.alert_box_names, parent, false);

        TextView sectionName = (TextView) view.findViewById(R.id.nameOfSection);
        sectionName.setTypeface(Font.setFontForIdea((Activity) this.getContext()));
        sectionName.setText(names[position]);
        sectionName.setTextSize(25);
        sectionName.setPadding(5, 5, 5, 5);
        return view;
    }
}
