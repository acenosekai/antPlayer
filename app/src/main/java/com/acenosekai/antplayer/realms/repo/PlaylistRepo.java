package com.acenosekai.antplayer.realms.repo;

import com.acenosekai.antplayer.ant.AntRepo;
import com.acenosekai.antplayer.realms.Music;
import com.acenosekai.antplayer.realms.Playlist;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;

/**
 * Created by Acenosekai on 2/13/2016.
 * Rock On
 */
public class PlaylistRepo extends AntRepo {
    public static final String DEFAULT_PLAYLIST = "Current Playlist";

    public PlaylistRepo(Realm r) {
        super(r);
    }

    public Playlist getCurrentPlaylist() {
        return findPlaylistByName(DEFAULT_PLAYLIST);
    }

    public Playlist findPlaylistByName(String name) {
        return r.where(Playlist.class).equalTo("name", name).findFirst();
    }

    public List<Music> generateMusicListShuffle(Boolean reShuffle) {
        return generateMusicListShuffle(DEFAULT_PLAYLIST, reShuffle);
    }

    public List<Music> generateMusicListShuffle(String name, Boolean reShuffle) {
        Playlist p = findPlaylistByName(name);
        List<Music> listMusic = null;

        if (!reShuffle) {
            listMusic = _JsonPathToMusicList(p.getMusicListShuffle());
        } else {
            saveMusicListShuffle(name);
            return generateMusicListShuffle(name, false);

        }
        return listMusic;
    }

    public List<Music> generateMusicList() {
        return generateMusicList(DEFAULT_PLAYLIST);
    }

    public List<Music> generateMusicList(String name) {
        Playlist p = findPlaylistByName(name);
        List<Music> listMusic = new ArrayList<>();
        if (p.getMusicList() != null && !p.getMusicList().equals("")) {
            listMusic = _JsonPathToMusicList(p.getMusicList());
        }
        return listMusic;
    }

    public void saveMusicListShuffle() {
        saveMusicListShuffle(DEFAULT_PLAYLIST);
    }


    public void saveMusicListShuffle(String name) {
        Playlist p = findPlaylistByName(name);
        List<Music> musicList = _JsonPathToMusicList(p.getMusicList());
        Collections.shuffle(musicList);
        r.beginTransaction();
        String jsonPath = _musicListToJsonPath(musicList);
        p.setMusicListShuffle(jsonPath);
        r.commitTransaction();
    }

    public void saveMusicList(List<Music> musicList) {
        saveMusicList(DEFAULT_PLAYLIST, musicList);
    }

    public void saveMusicList(String name, List<Music> musicList) {
        Playlist p = findPlaylistByName(name);
        r.beginTransaction();
        String jsonPath = _musicListToJsonPath(musicList);
        p.setMusicList(jsonPath);
        r.commitTransaction();
        saveMusicListShuffle(name);
    }

    public void addToPlaylist(List<Music> musicList) {
        addToPlaylist(DEFAULT_PLAYLIST, musicList);
    }

    public void addToPlaylist(String name, List<Music> musicList) {
        //repack music list to avoid realm error
        List<Music> newMusicList = new ArrayList<>();
        for (Music m : musicList) {
            newMusicList.add(m);
        }
        Playlist p = findPlaylistByName(name);
        List<Music> musicPlaylist = _JsonPathToMusicList(p.getMusicList());
        List<Music> musicPlaylistShuffle = _JsonPathToMusicList(p.getMusicListShuffle());
        musicPlaylist.addAll(newMusicList);
        Collections.shuffle(newMusicList);
        musicPlaylistShuffle.addAll(newMusicList);
        r.beginTransaction();
        p.setMusicList(_musicListToJsonPath(musicPlaylist));
        p.setMusicListShuffle(_musicListToJsonPath(musicPlaylistShuffle));
        r.commitTransaction();
    }

    private String _musicListToJsonPath(List<Music> musicList) {
        List<String> listPath = new ArrayList<>();
        for (Music m : musicList) {
            listPath.add(m.getPath());
        }
        Gson gson = new Gson();
        return gson.toJson(listPath);
    }

    private List<Music> _JsonPathToMusicList(String jsonPath) {
        List<Music> listMusic = new ArrayList<>();
        Gson gson = new Gson();
        List<String> musicPathList = gson.fromJson(jsonPath, new TypeToken<List<String>>() {
        }.getType());
        for (String path : musicPathList) {
            MusicRepo mr = new MusicRepo(r);
            Music m = mr.findOne(path);
            if (m != null) {
                listMusic.add(m);
            }
        }

        return listMusic;
    }

}
