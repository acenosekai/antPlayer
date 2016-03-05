package com.acenosekai.antplayer.realms.repo;

import com.acenosekai.antplayer.ant.AntRepo;
import com.acenosekai.antplayer.realms.Library;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Acenosekai on 2/13/2016.
 * Rock On
 */
public class LibraryRepo extends AntRepo {
    public LibraryRepo(Realm r) {
        super(r);
    }

    public RealmResults<Library> findAll() {
        return r.where(Library.class).findAll();
    }

    public RealmResults<Library> findLibraryBeginWithPath(String path) {
        return r.where(Library.class).beginsWith("path", path).findAll();
    }


}
