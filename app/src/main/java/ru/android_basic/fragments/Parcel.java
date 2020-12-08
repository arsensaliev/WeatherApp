package ru.android_basic.fragments;

import android.os.Parcelable;

public class Parcel implements Parcelable {
    private String cityName;
    private int cityId;
    protected Parcel(android.os.Parcel in) {
        cityName = in.readString();
        cityId = in.readInt();
    }

    public Parcel(String cityName, int cityId) {
        this.cityName = cityName;
        this.cityId = cityId;
    }

    public static final Creator<Parcel> CREATOR = new Creator<Parcel>() {
        @Override
        public Parcel createFromParcel(android.os.Parcel in) {
            return new Parcel(in);
        }

        @Override
        public Parcel[] newArray(int size) {
            return new Parcel[size];
        }
    };

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel, int i) {
        parcel.writeString(cityName);
        parcel.writeInt(cityId);
    }
}
