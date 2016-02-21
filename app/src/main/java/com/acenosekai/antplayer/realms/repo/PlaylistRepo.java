package com.acenosekai.antplayer.realms.repo;

import com.acenosekai.antplayer.ant.AntRepo;
import com.acenosekai.antplayer.realms.Playlist;

import io.realm.Realm;

/**
 * Created by Acenosekai on 2/13/2016.
 * Rock On
 */
public class PlaylistRepo extends AntRepo {
    public PlaylistRepo(Realm r) {
        super(r);
    }

    public Playlist getCurrentPlaylist() {
        return findPlaylistByName("Current Playlist");
    }

    public Playlist findPlaylistByName(String name) {
        return r.where(Playlist.class).equalTo("name", name).findFirst();
    }
}
