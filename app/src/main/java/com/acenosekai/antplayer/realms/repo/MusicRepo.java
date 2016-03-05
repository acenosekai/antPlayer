package com.acenosekai.antplayer.realms.repo;

import com.acenosekai.antplayer.ant.AntRepo;
import com.acenosekai.antplayer.realms.Music;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Acenosekai on 2/13/2016.
 * Rock On
 */
public class MusicRepo extends AntRepo {
    public MusicRepo(Realm r) {
        super(r);
    }

    public RealmResults<Music> findAll() {
        return r.where(Music.class).findAll();
    }

    public Music findOne(String path) {
        return r.where(Music.class).equalTo("path", path).findFirst();
    }


    public RealmResults<Music> findMusicByAlbumKey(String albumKey) {
        return r.where(Music.class).equalTo("albumKey", albumKey).findAll();
    }

    public RealmResults<Music> findMusicByArtist(String artist) {
        return r.where(Music.class).equalTo("artist", artist).findAll();
    }

    public RealmResults<Music> findMusicInDirectory(String path) {
        return r.where(Music.class).beginsWith("path", path).findAll();
    }

    public RealmResults<Music> searchByAlbumKey(String albumKey) {
        return r.where(Music.class).contains("album", albumKey, false).findAll();
    }

    public RealmResults<Music> searchByArtist(String artist) {
        return r.where(Music.class).contains("artist", artist, false).findAll();
    }

    public RealmResults<Music> searchByTitle(String title) {
        return r.where(Music.class).contains("title", title, false).findAll();
    }

    public int countMusic() {
        return r.where(Music.class).findAll().size();
    }
}
