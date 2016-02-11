package com.acenosekai.antplayer.realms;

import io.realm.RealmList;
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
    private RealmList<Music> musicFileList;
    private RealmList<Music> musicFileListShuffle;

    public RealmList<Music> getMusicFileListShuffle() {
        return musicFileListShuffle;
    }

    public void setMusicFileListShuffle(RealmList<Music> musicFileListShuffle) {
        this.musicFileListShuffle = musicFileListShuffle;
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

    public RealmList<Music> getMusicFileList() {
        return musicFileList;
    }

    public void setMusicFileList(RealmList<Music> musicFileList) {
        this.musicFileList = musicFileList;
    }
}
