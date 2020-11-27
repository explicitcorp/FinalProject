package com.example.finalproject;


import android.graphics.Bitmap;

public class AlbumInfo {
    String id;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }


    public void setAlbumArt(Bitmap albumArt) {
        this.albumArt = albumArt;
    }

    public Bitmap getAlbumArt() {
        return albumArt;
    }

    Bitmap albumArt;
    String artistName;
    String albumName;
    String year;

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }



    public AlbumInfo(String id, String artistName, String albumName, String year,Bitmap albumArt ){
setId(id);
setArtistName(artistName);
setAlbumName(albumName);
setYear(year);
setAlbumArt(albumArt);

    }



}
