package ru.android_basic.fragments.location;

public class CityItem {
    private String cityName;
    private int id;

    public CityItem(String cityName, int id) {
        this.cityName = cityName;
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
