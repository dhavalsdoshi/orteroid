package com.thoughtworks.orteroid.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Point implements Parcelable {

    private int id;
    private String message;
    private int sectionId;
    private Integer votes;

    public Point(int sectionId, int id, String message, Integer votes) {
        this.id = id;
        this.message = message;
        this.sectionId = sectionId;
        this.votes = votes;
    }

    private Point(Parcel parcel) {
        this.id = parcel.readInt();
        this.message = parcel.readString();
        this.sectionId = parcel.readInt();
        this.votes = parcel.readInt();
    }

    public String message() {
        return message;
    }

    public Integer sectionId() {
        return sectionId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        Log.v("", "Write to Parcel: " + flags);
        parcel.writeInt(id);
        parcel.writeString(message);
        parcel.writeInt(sectionId);
        parcel.writeInt(votes);
    }

    public static final Parcelable.Creator<Point> CREATOR = new Parcelable.Creator<Point>() {
        public Point createFromParcel(Parcel in) {
            return new Point(in);
        }

        public Point[] newArray(int size) {
            return new Point[size];
        }
    };

    public int id() {
        return id;
    }

    public String votes() {
        return votes.toString();
    }

    public Point clone() {
        return new Point(sectionId, id, message, votes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (id != point.id) return false;
        if (sectionId != point.sectionId) return false;
        if (!message.equals(point.message)) return false;
        if (votes != null ? !votes.equals(point.votes) : point.votes != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + message.hashCode();
        result = 31 * result + sectionId;
        result = 31 * result + (votes != null ? votes.hashCode() : 0);
        return result;
    }
}
