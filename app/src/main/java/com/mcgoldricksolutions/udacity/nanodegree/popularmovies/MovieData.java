package com.mcgoldricksolutions.udacity.nanodegree.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dirtbag on 6/18/16.
 */
public class MovieData implements Parcelable {

    public int id;
    public String title;
    public String imageUrl;
    public String description;
    public String releaseDate;
    public double userRating;

    public MovieData (int vId, String vTitle, String vImageUrl, String vDescription, String vReleaseDate, double vUserRating ) {
        this.id = vId;
        this.title = vTitle;
        this.imageUrl = vImageUrl;
        this.description = vDescription;
        this.releaseDate = vReleaseDate;
        this.userRating = vUserRating;
    }


    protected MovieData(Parcel in) {
        id = in.readInt();
        title = in.readString();
        imageUrl = in.readString();
        description = in.readString();
        this.releaseDate = in.readString();
        this.userRating = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(imageUrl);
        dest.writeString(description);
        dest.writeString(releaseDate);
        dest.writeDouble(userRating);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MovieData> CREATOR = new Creator<MovieData>() {
        @Override
        public MovieData createFromParcel(Parcel in) {
            return new MovieData(in);
        }

        @Override
        public MovieData[] newArray(int size) {
            return new MovieData[size];
        }
    };
}