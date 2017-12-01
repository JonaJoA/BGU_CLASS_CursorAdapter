package com.jonajoapps.caculatortest;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by SHAY on 11/23/2017.
 */

public class LineObject implements Parcelable{
    public String title;
    public String titleA;
    public String titleB;
    public String titleC;
    public String titleD;
    public String titleE;
    public Date date;

    public LineObject(String title) {
        this.title = title;
        this.date = new Date();
    }

    protected LineObject(Parcel in) {
        title = in.readString();
        titleA = in.readString();
        titleB = in.readString();
        titleC = in.readString();
        titleD = in.readString();
        titleE = in.readString();
    }

    public static final Creator<LineObject> CREATOR = new Creator<LineObject>() {
        @Override
        public LineObject createFromParcel(Parcel in) {
            return new LineObject(in);
        }

        @Override
        public LineObject[] newArray(int size) {
            return new LineObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(titleA);
        dest.writeString(titleB);
        dest.writeString(titleC);
        dest.writeString(titleD);
        dest.writeString(titleE);
    }
}
