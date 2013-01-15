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
        super(context, R.layout.section_view_row_layout, R.id.idea_text, objects);
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
        TextView ideas = (TextView) rowView.findViewById(R.id.idea_text);
        ideas.setTypeface(Font.setFontForIdea((Activity) this.getContext()));
        ideas.setText(points.get(position).message());
        ideas.setTextSize(18);
        TextView votes = (TextView) rowView.findViewById(R.id.vote_count);
        votes.setTypeface(Font.setFontForIdea((Activity) this.getContext()));
        String votesForPoint = points.get(position).votes();
        if(votesForPoint == null)votes.setText("+" + 0);
        else votes.setText("+" + votesForPoint);
        View view = rowView.findViewById(R.id.row_text);
        GradientDrawable drawable = (GradientDrawable) view.getBackground();
        drawable.setColor(Color.parseColor(colour));
        return rowView;
    }

}
