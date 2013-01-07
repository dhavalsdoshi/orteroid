package com.thoughtworks.orteroid.utilities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
    String colour;

    public SectionListAdapter(Context context, List<Point> objects, String colourCode) {
        super(context, R.layout.section_view_row_layout, R.id.row_text, objects);
        this.context = context;
        this.points = objects;
        this.colour = colourCode;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        if (convertView == null) {
            rowView = layoutInflater.inflate(R.layout.section_view_row_layout, parent, false);
        } else {
            rowView = convertView;
        }
        TextView ideas = (TextView) rowView.findViewById(R.id.row_text);
        ideas.setTypeface(Font.setFontForIdea((Activity) this.getContext()));
        ideas.setText(points.get(position).message());
        TextView votes = (TextView) rowView.findViewById(R.id.votes);
        votes.setTypeface(Font.setFontForIdea((Activity) this.getContext()));
        votes.setText("+" + points.get(position).votes());
        GradientDrawable drawable = (GradientDrawable) ideas.getBackground();
        drawable.setColor(Color.parseColor(colour));
        return rowView;
    }

}
