package com.thoughtworks.orteroid.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Point implements Parcelable{

    private int id;
    private String message;
    private int sectionId;

    public Point(int sectionId, int id, String message) {
        this.id = id;
        this.message = message;
        this.sectionId = sectionId;
    }

    private Point(Parcel parcel) {
        this.id = parcel.readInt();
        this.message = parcel.readString();
        this.sectionId = parcel.readInt();
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
        Log.v("","Write to Parcel: " + flags);
        parcel.writeInt(id);
        parcel.writeString(message);
        parcel.writeInt(sectionId);
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
}
