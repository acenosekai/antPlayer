package com.acenosekai.antplayer.models;

import com.acenosekai.antplayer.realms.Music;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Acenosekai on 1/17/2016.
 * Rock On
 */
public class Album {
    private String name;
    private String albumKey;
    private String artist;
    private String path;
    private int totalSong;

    public RealmResults<Music> getSongs(Realm r) {
        return r.where(Music.class).equalTo("albumKey", this.albumKey).findAll();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbumKey() {
        return albumKey;
    }

    public void setAlbumKey(String albumKey) {
        this.albumKey = albumKey;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getTotalSong() {
        return totalSong;
    }

    public void setTotalSong(int totalSong) {
        this.totalSong = totalSong;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
