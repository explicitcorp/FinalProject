package com.example.finalproject;


public class TrackInfo {


    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    long index;


    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    String artistName;

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    String albumName;
    String songName;

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public TrackInfo(long index, String artistName, String albumName, String songName ){
        setIndex(index);
        setArtistName(artistName);
        setAlbumName(albumName);
        setSongName(songName);

    }

    public TrackInfo(String id, String artistName, String albumName, String songName ){
        this(0,artistName,albumName,songName);


    }

    public TrackInfo( String artistName, String albumName, String songName ){
this ("",artistName,albumName,songName);


    }

}
