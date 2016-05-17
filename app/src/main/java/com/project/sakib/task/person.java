package com.project.sakib.task;

/**
 * Created by acer on 2/13/2016.
 */
public class person {

    private String fname;
    private String lname;
    private String phone;
    private String interests;
    private String image;

    public person(String fname, String lname, String phone, String interests, String image) {
        this.image = image;
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.interests = interests;
    }

    public String getFname() {
        return fname;
    }

    public String getImage() {
        return image;
    }

    public String getLname() {
        return lname;
    }

    public String getPhone() {
        return phone;
    }

    public String getInterests() {
        return interests;
    }
}
