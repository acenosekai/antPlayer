package com.acenosekai.antplayer.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.acenosekai.antplayer.App;
import com.acenosekai.antplayer.MainActivity;
import com.acenosekai.antplayer.ant.BassInit;
import com.acenosekai.antplayer.realms.Music;
import com.acenosekai.antplayer.realms.Playlist;
import com.un4seen.bass.BASS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Acenosekai on 1/17/2016.
 * Rock On
 */
public class PlaybackService extends Service {

    public static final String BIND_PLAYBACK = "bind_playback";
    public static final String PREV_ACTION = "playback_prev";
    public static final String PLAY_ACTION = "playback_play";
    public static final String NEXT_ACTION = "playback_next";
    public static final String PAUSE_ACTION = "playback_pause";
    public static final String REPEAT_ONE = "repeat_one";
    public static final String NO_REPEAT = "no_repeat";
    public static final String REPEAT_ALL = "repeat_all";
    public static final String SHUFFLE_ON = "shuffle_on";
    public static final String SHUFFLE_OFF = "shuffle_off";
    public static final String CROSSFADE_ON = "crossFade_on";
    public static final String CROSSFADE_OFF = "crossFade_off";
    public static final String APPLY_EFFECT = "apply_effect";
    public static final String REINIT_PLAYLIST = "reinit_playlist";
    public static final int SERVICE_ID = 134;
    private static final String TAG = "antPlay";
    boolean playing = false;
    private List<Music> musicList = new ArrayList<>();
    private Music music;
    private int chan1;
    private int chan2;
    private int chanActive = 0;
    private OnShuffleChange onShuffleChange;
    private OnRepeatChange onRepeatChange;
    private long position = 0;
    private OnInit onInit;
    private OnPlayingStatusChange onPlayingStatusChange;
    private OnPlayingRun onPlayingRun;
    private Handler handler = new Handler();
    private Runnable playingRun = new Runnable() {
        @Override
        public void run() {
            if (isPlaying()) {
                if (onPlayingRun != null) {
                    onPlayingRun.onPlayingRun(music);
                }
                if (getBytesPosition() >= getBytesTotal()) {
                    if (Integer.parseInt(getRepeat()) != App.REPEAT.NO_REPEAT || musicList.size() - 1 != getIndex()) {
                        if (Integer.parseInt(getRepeat()) == App.REPEAT.REPEAT_ONE) {
                            play();
                        } else {
                            next();
                        }
                    }
                }
                handler.postDelayed(this, 100);
            }
        }
    };

    //----------------------------------------------
    //Music List And Shuffle related END HERE
    //----------------------------------------------

    //----------------------------------------------
    //Music List And Shuffle related
    //----------------------------------------------
    public boolean isShuffle() {
        return ((App) getApplication()).getRegistry(App.REGISTRY.SHUFFLE).equals("1");
    }

    public void setShuffle(boolean shuffle) {
        if (shuffle) {
            ((App) getApplication()).saveRegistry(App.REGISTRY.SHUFFLE, String.valueOf(1));
            //save shuffle here
            generateShuffleList();
        } else {
            ((App) getApplication()).saveRegistry(App.REGISTRY.SHUFFLE, String.valueOf(0));
            generateMusicList();
        }
        if (onShuffleChange != null) {
            onShuffleChange.onShuffleChange(shuffle);
        }

    }

    public void setOnShuffleChange(OnShuffleChange onShuffleChange) {
        this.onShuffleChange = onShuffleChange;
    }

    public void generateList() {
        if (isShuffle()) {
            getShuffleList();
        } else {
            generateMusicList();
        }
    }

    public void generateMusicList() {
        Playlist p = ((App) getApplication()).getRealm().where(Playlist.class).equalTo("name", "Current Playlist").findFirst();
        musicList.clear();
        for (Music m : p.getMusicFileList()) {
            musicList.add(m);
        }
        Music mu = musicList.get(getIndex());
        try {
            ((App) getApplication()).saveRegistry(App.REGISTRY.SONG_POSITION, String.valueOf(musicList.indexOf(mu)));
        } catch (Exception e) {
            ((App) getApplication()).saveRegistry(App.REGISTRY.SONG_POSITION, "0");
        }
    }

    public void getShuffleList() {
        Playlist p = ((App) getApplication()).getRealm().where(Playlist.class).equalTo("name", "Current Playlist").findFirst();
        if (p.getMusicFileListShuffle().isEmpty()) {
            generateShuffleList();
        } else {
            musicList.clear();
            for (Music m : p.getMusicFileListShuffle()) {
                musicList.add(m);
            }
        }
    }

    private void generateShuffleList() {
        generateMusicList();
        Music m;
        try {
            m = musicList.get(getIndex());
        } catch (Exception ex) {
            m = musicList.get(0);
        }
        Collections.shuffle(musicList);
        ((App) getApplication()).saveRegistry(App.REGISTRY.SONG_POSITION, String.valueOf(musicList.indexOf(m)));

        ((App) getApplication()).getRealm().beginTransaction();
        Playlist p = ((App) getApplication()).getRealm().where(Playlist.class).equalTo("name", "Current Playlist").findFirst();
        p.getMusicFileListShuffle().clear();
        p.getMusicFileListShuffle().addAll(musicList);
        ((App) getApplication()).getRealm().copyToRealmOrUpdate(p);
        ((App) getApplication()).getRealm().commitTransaction();
    }

    public String getRepeat() {
        return ((App) getApplication()).getRegistry(App.REGISTRY.REPEAT);
    }

    public void setRepeat(String repeat) {
        ((App) getApplication()).saveRegistry(App.REGISTRY.REPEAT, repeat);
        if (onRepeatChange != null) {
            onRepeatChange.onRepeatChange(repeat);
        }
    }

    public void setOnRepeatChange(OnRepeatChange onRepeatChange) {
        this.onRepeatChange = onRepeatChange;
    }

    public int getIndex() {
        return Integer.parseInt(((App) getApplication()).getRegistry(App.REGISTRY.SONG_POSITION));
    }

    private int getActiveChan() {
        if (chanActive == 0) {
            return chan1;
        }
        return chan2;
    }

    public Music getMusic() {
        return music;
    }

    private void buildForeground() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent previousIntent = new Intent(this, PlaybackService.class);
        previousIntent.setAction(PlaybackService.PREV_ACTION);
        PendingIntent pPreviousIntent = PendingIntent.getService(this, 0, previousIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent pauseIntent = new Intent(this, PlaybackService.class);
        pauseIntent.setAction(PlaybackService.PAUSE_ACTION);
        PendingIntent pPauseIntent = PendingIntent.getService(this, 0, pauseIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent nextIntent = new Intent(this, PlaybackService.class);
        nextIntent.setAction(PlaybackService.NEXT_ACTION);
        PendingIntent pNextIntent = PendingIntent.getService(this, 0, nextIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder buildNotif = new NotificationCompat.Builder(this)
                .setContentTitle(music.getTitle())
                .setTicker(music.getTitle())
                .setContentText(music.getArtist())
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .addAction(android.R.drawable.ic_media_previous, "Previous", pPreviousIntent)
                .addAction(android.R.drawable.ic_media_pause, "Pause", pPauseIntent)
                .addAction(android.R.drawable.ic_media_next, "Next", pNextIntent);


        Notification notification = buildNotif.build();
        startForeground(PlaybackService.SERVICE_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals(PlaybackService.PLAY_ACTION)) {
                //start service and build notification
                Log.i(TAG, "Start forground app and play the music");
                play();
            } else if (intent.getAction().equals(PlaybackService.PREV_ACTION)) {
                Log.i(TAG, "Clicked Previous");
                previous();
            } else if (intent.getAction().equals(PlaybackService.NEXT_ACTION)) {
                Log.i(TAG, "Clicked Next");
                next();
            }
//            else if (intent.getAction().equals(PlaybackService.NO_REPEAT)) {
//                //TODO no repeat action
//            } else if (intent.getAction().equals(PlaybackService.REPEAT_ALL)) {
//                //TODO repeat all action
//            } else if (intent.getAction().equals(PlaybackService.REPEAT_ONE)) {
//                //TODO repeat one action
//            }
            else if (intent.getAction().equals(PlaybackService.SHUFFLE_OFF)) {
                setShuffle(false);
            } else if (intent.getAction().equals(PlaybackService.SHUFFLE_ON)) {
                setShuffle(true);
            }
//            else if (intent.getAction().equals(PlaybackService.CROSSFADE_OFF)) {
//                //TODO crossfade off
//            } else if (intent.getAction().equals(PlaybackService.CROSSFADE_ON)) {
//                //TODO crossfade on
//            }
            else if (intent.getAction().equals(PlaybackService.PAUSE_ACTION)) {
                Log.i(TAG, "Received Stop Foreground Intent");
                pause();
            }
//            else if (intent.getAction().equals(PlaybackService.BIND_PLAYBACK)) {
//                //TODO will drop this bind
//            } else if (intent.getAction().equals(PlaybackService.APPLY_EFFECT)) {
//                //TODO apply effect
//            }
            else if (intent.getAction().equals(PlaybackService.REINIT_PLAYLIST)) {
                generateList();
                init(0);
            }

        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new PlaybackServiceBinder();
    }

    public void setOnInit(OnInit onInit) {
        this.onInit = onInit;
    }

    public void init() {
        init(getIndex());
    }

    public void init(int mIndex) {
        if (musicList.isEmpty()) {
            generateList();
        }

        try {
            music = musicList.get(mIndex);
        } catch (Exception e) {
            music = musicList.get(0);
        }

        ((App) getApplication()).saveRegistry(App.REGISTRY.SONG_POSITION, String.valueOf(mIndex));

        if (onInit != null) {
            onInit.onInit(music);
        }

    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
        if (onPlayingStatusChange != null) {
            onPlayingStatusChange.onPlayingStatusChanged(playing);
        }
        if (playing) {
            handler.postDelayed(playingRun, 100);
        }
    }

    public void setOnPlayingStatusChange(OnPlayingStatusChange onPlayingStatusChange) {
        this.onPlayingStatusChange = onPlayingStatusChange;
    }

    public void setOnPlayingRun(OnPlayingRun onPlayingRun) {
        this.onPlayingRun = onPlayingRun;
    }

    public void play() {
        play(position);
    }

    public void play(long position) {
        BassInit.getInstance((App) getApplication());
        if (chanActive == 0) {
            chanActive = 1;
        } else {
            chanActive = 0;
        }
        if (chanActive == 0) {
            BASS.BASS_StreamFree(chan1);
            chan1 = 0;
            if ((chan1 = BASS.BASS_StreamCreateFile(music.getPath(), 0, 0, BASS.BASS_SAMPLE_FLOAT)) == 0) {
                // whatever it is, it ain't playable
                Log.i(TAG, "Can't play the file1");
                return;
            }
        } else {
            BASS.BASS_StreamFree(chan2);
            chan2 = 0;
            if ((chan2 = BASS.BASS_StreamCreateFile(music.getPath(), 0, 0, BASS.BASS_SAMPLE_FLOAT)) == 0) {
                // whatever it is, it ain't playable
                Log.i(TAG, "Can't play the file2");
                return;
            }
        }
        BASS.BASS_ChannelSetAttribute(getActiveChan(), BASS.BASS_ATTRIB_VOL, 1);
        BASS.BASS_ChannelSetPosition(getActiveChan(), position, BASS.BASS_POS_BYTE);
        BASS.BASS_ChannelPlay(getActiveChan(), false);

        Log.i("antPlay", String.valueOf(chanActive));
        buildForeground();
        setPlaying(true);
    }

    public void release() {
        BASS.BASS_StreamFree(getActiveChan());
        position = 0;
    }

    public void next() {
        int secIdx;
        secIdx = getIndex() + 1;
        if (secIdx >= musicList.size()) {
            secIdx = 0;
        }
        release();
        init(secIdx);
        if (playing) {
            play();
        }
    }

    public void previous() {
        if (playing && getSecondsPosition() > 3) {
            setSecondsPosition(0);
        } else {
            int secIdx;
            secIdx = getIndex() - 1;
            if (secIdx < 0) {
                secIdx = musicList.size() - 1;
            }
            release();
            init(secIdx);
            if (playing) {
                play();
            }
        }
    }

    public long pause() {
        setPlaying(false);
        long pos = getBytesPosition();
        BASS.BASS_ChannelPause(getActiveChan());
        stopForeground(true);
        position = pos;
        return pos;
    }

    public void stop() {
        setPlaying(false);
        BASS.BASS_ChannelPause(getActiveChan());
        stopForeground(true);
        position = 0;
//        return pos;
    }

    public int getSecondsPosition() {
        return (int) BASS.BASS_ChannelBytes2Seconds(getActiveChan(), getBytesPosition());
    }

    public void setSecondsPosition(int position) {
        setBytesPosition(BASS.BASS_ChannelSeconds2Bytes(getActiveChan(), position));
    }

    public long getBytesPosition() {
        return BASS.BASS_ChannelGetPosition(getActiveChan(), BASS.BASS_POS_BYTE);
    }

    public void setBytesPosition(long position) {
        BASS.BASS_ChannelSetPosition(getActiveChan(), position, BASS.BASS_POS_BYTE);
    }

    public int getSecondsTotal() {
        return (int) BASS.BASS_ChannelBytes2Seconds(getActiveChan(), getBytesTotal());
    }

    public long getBytesTotal() {
        return BASS.BASS_ChannelGetLength(getActiveChan(), BASS.BASS_POS_BYTE);
    }

    public String getPositionTime() {
        String totalRes = secondsToString(getSecondsTotal());
        if (totalRes.startsWith("00:")) {
            return secondsToString(getSecondsPosition()).substring(3);
        }
        return secondsToString(getSecondsPosition());
    }

    public String getTotalTime() {
        String res = secondsToString(getSecondsTotal());
        if (res.startsWith("00:")) {
            return res.substring(3);
        }
        return secondsToString(getSecondsTotal());
    }

    private String secondsToString(Integer sec) {
        int h = 0;
        int m = 0;
        if (sec >= 3600) {
            h = (sec - (sec % 3600)) / 3600;
        }
        int hDev = sec - (h * 3600);
        if (hDev >= 60) {
            m = (hDev - (hDev % 60)) / 60;
        }
        int s = hDev - (m * 60);
        return repairDigit(h) + ":" + repairDigit(m) + ":" + repairDigit(s);

    }

    private String repairDigit(Integer dg) {
        if (dg < 10) {
            return "0" + dg;
        }
        return dg.toString();
    }

    public interface OnShuffleChange {
        void onShuffleChange(boolean shuffle);
    }

    public interface OnRepeatChange {
        void onRepeatChange(String repeat);
    }

    public interface OnInit {
        void onInit(Music music);
    }

    public interface OnPlayingStatusChange {
        void onPlayingStatusChanged(boolean playing);
    }

    public interface OnPlayingRun {
        void onPlayingRun(Music music);
    }

    public class PlaybackServiceBinder extends Binder {
        public PlaybackService getService() {
            return PlaybackService.this;
        }
    }

}
