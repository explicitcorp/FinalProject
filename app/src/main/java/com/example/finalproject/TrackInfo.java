package com.example.finalproject;


public class TrackInfo {
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



    public TrackInfo(String id, String artistName, String albumName, String songName ){
        setId(id);
        setArtistName(artistName);
        setAlbumName(albumName);
      setSongName(songName);

    }

    public TrackInfo( String artistName, String albumName, String songName ){
this ("",artistName,albumName,songName);
        setArtistName(artistName);
        setAlbumName(albumName);
        setSongName(songName);

    }

}
