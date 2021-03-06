package com.ideaboardz.android.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Section implements Parcelable {

    private String name;
    private Integer id;
    private List<Point> points = new ArrayList<Point>();

    public Section(String name, Integer id) {
        this.name = name;
        this.id = id;
        points = new ArrayList<Point>();
    }

    private Section(Parcel in) {
        id = in.readInt();
        name = in.readString();
        points = new ArrayList<Point>();
        in.readTypedList(points, Point.CREATOR);
    }

    public String name() {
        return name;
    }

    public boolean contains(Point point) {
        return points.contains(point);
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public Integer id() {
        return id;
    }

    public List<Point> points() {
        return points;
    }

    public List<Point> sortedPointsByVotes() {
        Collections.sort(points, new Comparator<Point>() {

            @Override
            public int compare(Point lhs, Point rhs) {
                int lhsVotes = Integer.parseInt(lhs.votes());
                int rhsVotes = Integer.parseInt(rhs.votes());
                return lhsVotes > rhsVotes ? -1 : lhsVotes < rhsVotes ? 1 : 0;
            }
        });
        return points;
    }

    public List<Point> sortedPointsByTime() {
        Collections.sort(points, new Comparator<Point>() {

            @Override
            public int compare(Point lhs, Point rhs) {
                Long lhsTime = lhs.creationTime();
                Long rhsTime = rhs.creationTime();
                return lhsTime < rhsTime ? -1 : lhsTime > rhsTime ? 1 : 0;
            }
        });
        return points;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeTypedList(points);
    }

    public static final Parcelable.Creator<Section> CREATOR = new Parcelable.Creator<Section>() {
        public Section createFromParcel(Parcel in) {
            return new Section(in);
        }

        public Section[] newArray(int size) {
            return new Section[size];
        }
    };

    public Point getPointFromMessage(String message) {
        for (Point point : points) {
            if (point.message().equals(message)) {
                return point;
            }
        }
        return null;
    }

    public void empty() {
        points.clear();
    }


}
