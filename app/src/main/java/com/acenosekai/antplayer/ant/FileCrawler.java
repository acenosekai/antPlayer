package com.acenosekai.antplayer.ant;

import android.media.MediaMetadataRetriever;
import android.util.Log;

import com.acenosekai.antplayer.App;
import com.acenosekai.antplayer.MainActivity;
import com.acenosekai.antplayer.R;
import com.acenosekai.antplayer.realms.Cover;
import com.acenosekai.antplayer.realms.Library;
import com.acenosekai.antplayer.realms.Music;
import com.acenosekai.antplayer.realms.repo.CoverRepo;
import com.acenosekai.antplayer.realms.repo.MusicRepo;
import com.acenosekai.antplayer.realms.repo.PlaylistRepo;
import com.un4seen.bass.BASS;

import java.io.File;

import io.realm.RealmResults;

/**
 * Created by Acenosekai on 1/16/2016.
 * Rock On
 */
public class FileCrawler {
    private App app;
    private MainActivity mainActivity;

    public FileCrawler(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.app = (App) mainActivity.getApplication();
    }

    public void reload(RealmResults<Library> libraries) {
        if (mainActivity.getPlaybackService().isPlaying()) {
            mainActivity.getPlaybackService().stop();
        }
        RealmResults<Music> allMusic = new MusicRepo(app.getRealm()).findAll();
        app.getRealm().beginTransaction();
        allMusic.clear();
        for (Library lib : libraries) {
            craw(lib.getPath());
        }
        app.getRealm().commitTransaction();
        PlaylistRepo playlistRepo = new PlaylistRepo(app.getRealm());
        app.saveRegistry(App.REGISTRY.SONG_POSITION, "0");
        playlistRepo.saveMusicList(new MusicRepo(app.getRealm()).findAll());


        if (new MusicRepo(app.getRealm()).countMusic() > 0) {
            mainActivity.getPlaybackService().generateList();
            mainActivity.getPlaybackService().init();
        }
    }


    private void initializeMusicFile(Music m) {
        File fch = new File(m.getPath());
        if (fch.getPath().endsWith(".mp4")
                || fch.getPath().endsWith(".m4a")
                || fch.getPath().endsWith(".aac")
                //flac format
                || fch.getPath().endsWith(".flac")
                //mp3 format
                || fch.getPath().endsWith(".mp3")
                //vorbis format
                || fch.getPath().endsWith(".ogg")
                //PCM wave format
                || fch.getPath().endsWith(".wav")) {
            try {
                MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
                metaRetriever.setDataSource(fch.getPath());
                m.setTitle(defaultIfNotNull(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE), fch.getName()));
                m.setArtist(defaultIfNotNull(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST), app.getString(R.string.unknown_artist)));
                m.setGenre(defaultIfNotNull(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE), app.getString(R.string.unknown)));
                m.setAlbum(defaultIfNotNull(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM), app.getString(R.string.unknown_album)));
                m.setAlbumArtist(defaultIfNotNull(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST), m.getArtist()));
                m.setAlbumKey(m.getAlbum() + "-" + m.getAlbumArtist());

                Cover c = new CoverRepo(app.getRealm()).findOneCoverByAlbumKey(m.getAlbumKey());
                if (c == null) {
                    Cover nc = new Cover();
                    nc.setAlbumKey(m.getAlbumKey());
                    nc.setChecked(true);
                    nc.setCover(metaRetriever.getEmbeddedPicture());
                    app.getRealm().copyToRealmOrUpdate(nc);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private String defaultIfNotNull(String val, String def) {
        if (val != null && !val.isEmpty()) {
            return val;
        }

        return def;
    }

    private void craw(String path) {
        File f = new File(path);
        if (f.listFiles() != null) {
            for (File fch : f.listFiles()) {
                if (//mp4 format
                        fch.getPath().toLowerCase().endsWith(".mp4")
                                || fch.getPath().toLowerCase().endsWith(".m4a")
                                || fch.getPath().toLowerCase().endsWith(".aac")
                                //flac format
                                || fch.getPath().toLowerCase().endsWith(".flac")
                                //mp3 format
                                || fch.getPath().toLowerCase().endsWith(".mp3")
                                //vorbis format
                                || fch.getPath().toLowerCase().endsWith(".ogg")
                                //PCM wave format
                                || fch.getPath().toLowerCase().endsWith(".wav")
                                //other
                                || fch.getPath().toLowerCase().endsWith(".aiff")
                                || fch.getPath().toLowerCase().endsWith(".ape")
                                || fch.getPath().toLowerCase().endsWith(".dff")
                                || fch.getPath().toLowerCase().endsWith(".dsf")
                                || fch.getPath().toLowerCase().endsWith(".opus")
                                || fch.getPath().toLowerCase().endsWith(".sf2")
                                || fch.getPath().toLowerCase().endsWith(".sfz")
                                || fch.getPath().toLowerCase().endsWith(".mpc")
                                || fch.getPath().toLowerCase().endsWith(".mpp")
                                || fch.getPath().toLowerCase().endsWith(".mp+")
                                || fch.getPath().toLowerCase().endsWith(".tta")
                        ) {

                    Music m = new Music();
                    m.setPath(fch.getPath());
                    initializeMusicFile(m);

                    m.setTitle(defaultIfNotNull(m.getTitle(), fch.getName()));
                    m.setArtist(defaultIfNotNull(m.getArtist(), app.getString(R.string.unknown_artist)));
                    m.setGenre(defaultIfNotNull(m.getGenre(), app.getString(R.string.unknown)));
                    m.setAlbum(defaultIfNotNull(m.getAlbum(), app.getString(R.string.unknown_album)));
                    m.setAlbumArtist(defaultIfNotNull(m.getAlbumArtist(), m.getArtist()));
                    m.setAlbumKey(m.getAlbum() + "-" + m.getAlbumArtist());
//count length
                    int chan;
                    BassInit.getInstance(app);
                    if ((chan = BASS.BASS_StreamCreateFile(m.getPath(), 0, 0, BASS.BASS_SAMPLE_FLOAT)) == 0) {
                        // whatever it is, it ain't playable
                        Log.i("antPlay", "Can't play the file2");
                        return;
                    }
                    m.setLength((int) BASS.BASS_ChannelBytes2Seconds(chan, BASS.BASS_ChannelGetLength(chan, BASS.BASS_POS_BYTE)));
                    BASS.BASS_StreamFree(chan);
                    app.getRealm().copyToRealmOrUpdate(m);
                }
                if (fch.isDirectory()) {
                    craw(fch.getPath());
                }
            }
        }
    }
}
