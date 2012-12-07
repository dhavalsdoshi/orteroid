package com.thoughtworks.orteroid.utilities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.thoughtworks.orteroid.R;
import com.thoughtworks.orteroid.models.Point;

import java.util.ArrayList;
import java.util.List;

public class SectionListAdapter extends ArrayAdapter<Point> {

    private final Context context;
    private final List<Point> points;
    View rowView;
    private List<String> colours = new ArrayList<String>();
    String colour;
    int colourCode;

    public SectionListAdapter(Context context, List<Point> objects, int colourCode) {
        super(context, R.layout.section_view_row_layout, R.id.row_text, objects);
        this.context = context;
        this.points = objects;
        this.colourCode = colourCode;
        populateColours();
    }

    private void populateColours() {
        colours.add("#ffff88");
        colours.add("#ffb058");
        colours.add("#bcee6b");
        colours.add("#d07ddf");
        colours.add("#ccffff");
        colours.add("#17b6ff");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        if (convertView == null) {
            rowView = layoutInflater.inflate(R.layout.section_view_row_layout, parent, false);
        } else {
            rowView = convertView;
        }
        TextView textView = (TextView) rowView.findViewById(R.id.row_text);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Handwritten_Crystal_v2.ttf");
        textView.setTypeface(tf);
        textView.setText(points.get(position).message());
        textView.setBackgroundResource(R.drawable.sticky);
        GradientDrawable drawable = (GradientDrawable) textView.getBackground();
        colour = colours.get(colourCode);
        drawable.setColor(Color.parseColor(colour));
        return rowView;
    }

}
