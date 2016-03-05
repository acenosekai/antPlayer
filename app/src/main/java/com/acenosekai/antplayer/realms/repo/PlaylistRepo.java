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
        List<Music> listMusic = new ArrayList<>();

        if (p.getMusicListShuffle() != null && !p.getMusicListShuffle().equals("") && !reShuffle) {
            listMusic = _generateList(p.getMusicListShuffle());
        } else {
            listMusic = generateMusicList(name);
            Collections.shuffle(listMusic);
            saveMusicListShuffle(name, listMusic);
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
            listMusic = _generateList(p.getMusicList());
        }
        return listMusic;
    }

    private List<Music> _generateList(String musicPathListString) {
        List<Music> listMusic = new ArrayList<>();
        Gson gson = new Gson();
        List<String> musicPathList = gson.fromJson(musicPathListString, new TypeToken<List<String>>() {
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

    public void saveMusicListShuffle(List<Music> musicList) {
        saveMusicListShuffle(DEFAULT_PLAYLIST, musicList);
    }

    public void saveMusicListShuffle(String name, List<Music> musicList) {
        Playlist p = findPlaylistByName(name);
        r.beginTransaction();

        List<String> listPath = new ArrayList<>();
        for (Music m : musicList) {
            listPath.add(m.getPath());
        }
        Gson gson = new Gson();
        String jsonPath = gson.toJson(listPath);
        p.setMusicListShuffle(jsonPath);
        r.commitTransaction();
    }

    public void saveMusicList(List<Music> musicList) {
        saveMusicList(DEFAULT_PLAYLIST, musicList);
    }

    public void saveMusicList(String name, List<Music> musicList) {
        Playlist p = findPlaylistByName(name);
        r.beginTransaction();

        List<String> listPath = new ArrayList<>();
        for (Music m : musicList) {
            listPath.add(m.getPath());
        }
        Gson gson = new Gson();
        String jsonPath = gson.toJson(listPath);
        p.setMusicList(jsonPath);
        r.commitTransaction();
        saveMusicListShuffle(name, musicList);
    }

}
