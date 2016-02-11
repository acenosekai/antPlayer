package com.acenosekai.antplayer.realms;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Acenosekai on 1/17/2016.
 * Rock On
 */
public class Registry extends RealmObject {
    @PrimaryKey
    private int key;
    private String val;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
