package com.example.eldroid_fernandez;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class Movie implements Serializable, Parcelable {

    String movID;
    String movTit;
    String movYear;
    String movRunT;
    String movLanguage;
    String movCount;
    Timestamp movReleaseD;
    String image;

    public Movie(String movID, String movTit, String movYear, String movRunT, String movLanguage, String movCount, Timestamp movReleaseD, String image) {
        this.movID = movID;
        this.movTit = movTit;
        this.movYear = movYear;
        this.movRunT = movRunT;
        this.movLanguage = movLanguage;
        this.movCount = movCount;
        this.movReleaseD = movReleaseD;
        this.image = image;
    }

    public Movie(String id, String title, Timestamp releaseDate, String year, String runTime, String language, String country, String image) {
    }

    protected Movie(Parcel in) {
        movID = in.readString();
        movTit = in.readString();
        movYear = in.readString();
        movRunT = in.readString();
        movLanguage = in.readString();
        movCount = in.readString();
        movReleaseD = in.readParcelable(Timestamp.class.getClassLoader());
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMovID() {
        return movID;
    }

    public void setMovID(String movID) {
        this.movID = movID;
    }

    public String getMovTit() {
        return movTit;
    }

    public void setMovTit(String movTit) {
        this.movTit = movTit;
    }

    public String getMovYear() {
        return movYear;
    }

    public void setMovYear(String movYear) {
        this.movYear = movYear;
    }

    public String getMovRunT() {
        return movRunT;
    }

    public void setMovRunT(String movRunT) {
        this.movRunT = movRunT;
    }

    public String getMovLanguage() {
        return movLanguage;
    }

    public void setMovLanguage(String movLanguage) {
        this.movLanguage = movLanguage;
    }

    public String getMovCount() {
        return movCount;
    }

    public void setMovCount(String movCount) {
        this.movCount = movCount;
    }

    public Timestamp getMovReleaseD() {
        return movReleaseD;
    }

    public void setMovReleaseD(Timestamp movReleaseD) {
        this.movReleaseD = movReleaseD;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(movID);
        dest.writeString(movTit);
        dest.writeString(movYear);
        dest.writeString(movRunT);
        dest.writeString(movLanguage);
        dest.writeString(movCount);
        dest.writeParcelable(movReleaseD, i);
    }
}


