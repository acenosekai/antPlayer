package com.acenosekai.antplayer.models;

import com.acenosekai.antplayer.realms.Music;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Acenosekai on 1/17/2016.
 * Rock On
 */
public class Artist {
    private String name;
    private int totalSong;

    public RealmResults<Music> getSongs(Realm r) {
        return r.where(Music.class).equalTo("artist", this.name).findAll();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalSong() {
        return totalSong;
    }

    public void setTotalSong(int totalSong) {
        this.totalSong = totalSong;
    }
}
