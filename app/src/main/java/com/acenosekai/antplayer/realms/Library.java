package com.acenosekai.antplayer.realms;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Acenosekai on 1/16/2016.
 * Rock On
 */
public class Library extends RealmObject {
    @PrimaryKey
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
