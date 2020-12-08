package com.example.finalproject;

public class CovidSavedInfo {
    long id;
    String country;
    String date;
    String link;
    public CovidSavedInfo(){}
    public CovidSavedInfo(long id, String country, String date, String link) {
        this.id = id;
        this.country = country;
        this.date = date;
        this.link = link;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
