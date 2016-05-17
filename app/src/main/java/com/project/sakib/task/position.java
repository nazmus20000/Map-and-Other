package com.project.sakib.task;

/**
 * Created by acer on 2/13/2016.
 */
public class position {

    private String phone;
    private String latitude;
    private String longitude;
    private String time;

    public position(String phone, String latitude, String longitude, String time) {
        this.latitude = latitude;
        this.phone = phone;
        this.longitude = longitude;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public String getPhone() {
        return phone;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "\nLatitude="+latitude +"\n"+ "Longitude="+longitude+"\n"+ "Time="+time+"\n";
    }
}
