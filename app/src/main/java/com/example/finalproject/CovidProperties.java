package com.example.finalproject;

import java.io.Serializable;

public class CovidProperties implements Serializable {
    String country;
    String province;
    String cases;
    String date;

    public CovidProperties() {
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCases() {
        return cases;
    }

    public void setCases(String cases) {
        this.cases = cases;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public CovidProperties(String country, String province, String cases, String date) {
        this.country = country;
        this.province = province;
        this.cases = cases;
        this.date = date;
    }
}
