package com.thoughtworks.orteroid.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.thoughtworks.orteroid.R;
import com.thoughtworks.orteroid.adapters.SectionListAdapter;
import com.thoughtworks.orteroid.models.Board;
import com.thoughtworks.orteroid.models.Point;
import com.thoughtworks.orteroid.utilities.ColorSticky;

import java.util.List;

public class ViewPointsFragment extends Fragment {
    private Board board;
    private int position;
    private Context context;


    public ViewPointsFragment(Board board, int position, Context context) {
        this.board = board;
        this.position = position;
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_of_points, container, false);
        int selectedItem = board.sections().get(position).id();
        setPointsIntoList(selectedItem, board.pointsOfSection(selectedItem), view);
        return view;
    }

    private void setPointsIntoList(int selectedItem, List<Point> points, View view) {
        String colourCode = ColorSticky.getColorCode(selectedItem);
        SectionListAdapter sectionListAdapter = new SectionListAdapter(context, points, colourCode);
        final ListView listView = (ListView) view.findViewById(android.R.id.list);
        int currentItem = listView.getFirstVisiblePosition();
        listView.setEmptyView(view.findViewById(R.id.no_ideas_added));
        listView.setAdapter(sectionListAdapter);
        listView.setSelectionFromTop(currentItem, 0);
    }
}
