package com.acenosekai.antplayer;

import android.app.Application;

import com.acenosekai.antplayer.realms.Playlist;
import com.acenosekai.antplayer.realms.Registry;
import com.acenosekai.antplayer.realms.repo.PlaylistRepo;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Acenosekai on 1/16/2016.
 * Rock On
 */
public class App extends Application {

    //registry


    private static final String TAG = "AntPlay";
    private Realm realm;

    @Override
    public void onCreate() {
        super.onCreate();

        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .name("antPlayerDb")
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        this.realm = Realm.getInstance(config);


        realm.beginTransaction();
        if (realm.where(Playlist.class).equalTo("name", PlaylistRepo.DEFAULT_PLAYLIST).findAll().isEmpty()) {
            Playlist p = new Playlist();
            p.setName(PlaylistRepo.DEFAULT_PLAYLIST);
            p.setType(0);
            realm.copyToRealmOrUpdate(p);

            //assign default value to registry

        }
        setDefaultRegistryValue(REGISTRY.SONG_POSITION, "0");
        setDefaultRegistryValue(REGISTRY.SHUFFLE, "0");
        setDefaultRegistryValue(REGISTRY.REPEAT, String.valueOf(REPEAT.NO_REPEAT));
        realm.commitTransaction();

    }

    private void setDefaultRegistryValue(int key, String value) {
        if (realm.where(Registry.class).equalTo("key", key).findAll().isEmpty()) {
            setRegistry(key, value);
        }
    }

    public void setRegistry(int key, String val) {
        Registry r = new Registry();
        r.setKey(key);
        r.setVal(val);
        realm.copyToRealmOrUpdate(r);
    }

    public void saveRegistry(int key, String val) {
        realm.beginTransaction();
        setRegistry(key, val);
        realm.commitTransaction();
    }

    public String getRegistry(int key) {
        return realm.where(Registry.class).equalTo("key", key).findFirst().getVal();
    }

    public Realm getRealm() {
        return realm;
    }

    public static class REPEAT {
        public static final int NO_REPEAT = 0;
        public static final int REPEAT_ALL = 1;
        public static final int REPEAT_ONE = 2;
    }

    public static class REGISTRY {
        public static final int SONG_POSITION = 0;
        public static final int SHUFFLE = 1;
        public static final int REPEAT = 2;
    }


}
