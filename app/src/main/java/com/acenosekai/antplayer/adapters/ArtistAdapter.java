package com.acenosekai.antplayer.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acenosekai.antplayer.App;
import com.acenosekai.antplayer.MainActivity;
import com.acenosekai.antplayer.R;
import com.acenosekai.antplayer.fragments.StandAloneMusicFragment;
import com.acenosekai.antplayer.holders.ArtistItemHolder;
import com.acenosekai.antplayer.models.Artist;
import com.acenosekai.antplayer.realms.Music;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

/**
 * Created by Acenosekai on 1/17/2016.
 * Rock On
 */
public class ArtistAdapter extends RecyclerView.Adapter<ArtistItemHolder> {
    MainActivity mainActivity;
    private App app;
    //    private RealmResults<Music> musics;
    private List<Artist> artists;

    public ArtistAdapter(MainActivity mainActivity, RealmResults<Music> musics) {
        this.mainActivity = mainActivity;
        this.app = (App) mainActivity.getApplication();
        this.artists = musicToArtist(musics);
    }

    private List<Artist> musicToArtist(RealmResults<Music> musics) {
        List<Artist> artistList = new ArrayList<>();
        musics.sort("artist");
        String lastArtist = "";
        int index = 0;
        for (Music m : musics) {
            Log.i("antPlay", "artist :" + m.getArtist());
            Log.i("antPlay", "path :" + m.getPath());
            if (!m.getArtist().equals(lastArtist)) {
                Artist art = new Artist();
                art.setName(m.getArtist());
                art.setTotalSong(1);
                artistList.add(art);
                lastArtist = m.getArtist();
                index++;
            } else {
                Artist art = artistList.get(index - 1);
                art.setTotalSong(art.getTotalSong() + 1);
            }
        }

        return artistList;
    }

    @Override
    public ArtistItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_artist, parent, false);
        return new ArtistItemHolder(v);
    }

    @Override
    public void onBindViewHolder(ArtistItemHolder holder, int position) {
        final Artist artist = artists.get(position);
        holder.getListText().setText(artist.getName());
        holder.getListText().setSelected(true);
        holder.getListDesc().setSelected(true);
        holder.getListDesc().setText(artist.getTotalSong() + " " + app.getString(R.string.songs));

        holder.getListMenu().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RealmResults<Music> musics = app.getRealm().where(Music.class).equalTo("artist", artist.getName()).findAll();
                mainActivity.filesDialogMenu(artist.getName(), musics).show();
            }
        });

        holder.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StandAloneMusicFragment musicFragment = new StandAloneMusicFragment();
                musicFragment.setTitle(artist.getName());
                musicFragment.setMusicList(app.getRealm().where(Music.class).equalTo("artist", artist.getName()).findAll());
                mainActivity.changePage(musicFragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }
}
