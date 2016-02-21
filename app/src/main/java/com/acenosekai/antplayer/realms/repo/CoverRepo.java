package com.acenosekai.antplayer.realms.repo;

import com.acenosekai.antplayer.ant.AntRepo;
import com.acenosekai.antplayer.realms.Cover;

import io.realm.Realm;

/**
 * Created by Acenosekai on 2/13/2016.
 * Rock On
 */
public class CoverRepo extends AntRepo {

    public CoverRepo(Realm r) {
        super(r);
    }

    public Cover findOneCoverByAlbumKey(String albumKey) {
        return r.where(Cover.class).equalTo("albumKey", albumKey).findFirst();
    }
}
