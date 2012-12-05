package com.thoughtworks.orteroid.utilities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.thoughtworks.orteroid.R;
import com.thoughtworks.orteroid.models.Point;

import java.util.List;

public class SectionListAdapter extends ArrayAdapter<Point> {

    private final Context context;
    private final List<Point> points;
    View rowView;

    public SectionListAdapter(Context context, List<Point> objects) {
        super(context, R.layout.section_view_row_layout, R.id.row_text, objects);
        this.context = context;
        this.points = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        rowView = layoutInflater.inflate(R.layout.section_view_row_layout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.row_text);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Handwritten_Crystal_v2.ttf");
        textView.setTypeface(tf);
        textView.setText(points.get(position).message());
        return rowView;
    }

}
