package com.acenosekai.antplayer.realms;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Acenosekai on 1/17/2016.
 * Rock On
 */
public class Playlist extends RealmObject {
    @PrimaryKey
    private String name;
    private int type;
    private String musicList;
    private String musicListShuffle;

    public String getMusicList() {
        return musicList;
    }

    public void setMusicList(String musicList) {
        this.musicList = musicList;
    }

    public String getMusicListShuffle() {
        return musicListShuffle;
    }

    public void setMusicListShuffle(String musicListShuffle) {
        this.musicListShuffle = musicListShuffle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
