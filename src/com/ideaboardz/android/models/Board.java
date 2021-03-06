package com.ideaboardz.android.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Board implements Parcelable {

    private String name;
    private Integer id;
    private List<Section> sections;

    public Board(String name, Integer id, List<Section> sections) {
        this.id = id;
        this.name = name;
        this.sections = sections;
    }

    private Board(Parcel in) {
        name = in.readString();
        id = in.readInt();
        sections = new ArrayList<Section>();
        in.readTypedList(sections, Section.CREATOR);
    }

    public String name() {
        return name;
    }

    public List<Section> sections() {
        return sections;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Board)) return false;

        Board board = (Board) o;

        return id.equals(board.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public void update(List<Point> points) {
        for (Section section : sections) {
            section.empty();
        }
        for (Point point : points) {
            for (Section section : sections) {
                if (point.sectionId().equals(section.id()) && !section.contains(point)) {
                    section.addPoint(point);
                }
            }
        }
    }

    public List<Point> pointsOfSection(int selectedSection) {
        for (Section section : sections) {
            if ((section.id()).equals(selectedSection))
                return section.points();
        }
        return null;
    }

    public List<Point> pointsOfSectionSortedByVotes(int selectedSection) {
        for (Section section : sections) {
            if ((section.id()).equals(selectedSection))
                return section.sortedPointsByVotes();
        }
        return null;
    }

    public List<Point> pointsOfSectionSortedByTime(int selectedSection) {
        for (Section section : sections) {
            if ((section.id()).equals(selectedSection))
                return section.sortedPointsByTime();
        }
        return null;
    }

    public Integer id() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(name);
        parcel.writeInt(id);
        parcel.writeTypedList(sections);
    }

    public static final Parcelable.Creator<Board> CREATOR = new Parcelable.Creator<Board>() {
        public Board createFromParcel(Parcel in) {
            return new Board(in);
        }

        public Board[] newArray(int size) {
            return new Board[size];
        }
    };

    public List<String> getSectionNames() {
        List<String> sectionNames = new ArrayList<String>();
        for (Section section : sections) {
            sectionNames.add(section.name());
        }
        return sectionNames;
    }

    public Point getPointFromMessage(String message, int selectedIndex) {
        Section selectedSection = sections.get(selectedIndex);
        return selectedSection.getPointFromMessage(message);
    }


}
