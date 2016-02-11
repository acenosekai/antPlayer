package com.acenosekai.antplayer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acenosekai.antplayer.App;
import com.acenosekai.antplayer.MainActivity;
import com.acenosekai.antplayer.R;
import com.acenosekai.antplayer.ant.Utility;
import com.acenosekai.antplayer.fragments.NowPlayingFragment;
import com.acenosekai.antplayer.holders.MusicItemHolder;
import com.acenosekai.antplayer.realms.Music;
import com.acenosekai.antplayer.realms.Playlist;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by Acenosekai on 1/17/2016.
 * Rock On
 */
public class MusicAdapter extends RecyclerView.Adapter<MusicItemHolder> {
    private MainActivity mainActivity;
    private App app;
    //    private RealmResults<Music> musics;
    private List<Music> musics;

    public MusicAdapter(MainActivity mainActivity, RealmResults<Music> musics) {
        this.mainActivity = mainActivity;
        this.app = (App) mainActivity.getApplication();
        this.musics = musics;
    }


    @Override
    public MusicItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_music, parent, false);
        return new MusicItemHolder(v);
    }

    @Override
    public void onBindViewHolder(MusicItemHolder holder, int position) {
        final Music music = musics.get(position);
        holder.getListText().setText(music.getTitle());
        holder.getListText().setSelected(true);
        holder.getListDesc().setSelected(true);
        holder.getListDesc().setText(music.getArtist() + " - " + music.getAlbum());
        String timeStr = Utility.secondsToString(music.getLength());
        if (timeStr.startsWith("00:")) {
            timeStr = timeStr.substring(3, timeStr.length());
        }
        holder.getListLength().setText(timeStr);

        holder.getListMenu().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.getRealm().beginTransaction();

                List<Music> musicList = musics.subList(0, musics.size());
                Playlist p = app.getRealm().where(Playlist.class).equalTo("name", "Current Playlist").findFirst();
                p.getMusicFileList().clear();
                p.getMusicFileListShuffle().clear();
                app.setRegistry(App.REGISTRY.SONG_POSITION, String.valueOf(musicList.indexOf(music)));
                p.getMusicFileList().addAll(musicList);
                app.getRealm().copyToRealmOrUpdate(p);

                NowPlayingFragment npf = new NowPlayingFragment();
                npf.setShowAndPlay(true);
                mainActivity.changePage(npf);

                app.getRealm().commitTransaction();

            }
        });
    }

    @Override
    public int getItemCount() {
        return musics.size();
    }
}
