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
import com.acenosekai.antplayer.models.Album;
import com.acenosekai.antplayer.realms.Music;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

/**
 * Created by Acenosekai on 1/17/2016.
 * Rock On
 */
public class AlbumAdapter extends RecyclerView.Adapter<ArtistItemHolder> {
    private MainActivity mainActivity;
    private App app;
    //    private RealmResults<Music> musics;
    private List<Album> albums;

    public AlbumAdapter(MainActivity mainActivity, RealmResults<Music> musics) {
        this.mainActivity = mainActivity;
        this.app = (App) mainActivity.getApplication();
        this.albums = musicToArtist(musics);
    }

    private List<Album> musicToArtist(RealmResults<Music> musics) {
        List<Album> albumList = new ArrayList<>();
        musics.sort("albumKey");
        String lastAlbumKey = "";
        int index = 0;
        for (Music m : musics) {
            Log.i("antPlay", "artist :" + m.getArtist());
            Log.i("antPlay", "path :" + m.getPath());
            if (!m.getAlbumKey().equals(lastAlbumKey)) {
                Album art = new Album();
                art.setName(m.getAlbum());
                art.setPath(m.getPath());
                art.setAlbumKey(m.getAlbumKey());
                art.setArtist(m.getAlbumArtist());
                art.setTotalSong(1);
                albumList.add(art);
                lastAlbumKey = m.getAlbumKey();
                index++;
            } else {
                Album art = albumList.get(index - 1);
                art.setTotalSong(art.getTotalSong() + 1);
            }
        }

        return albumList;
    }

    @Override
    public ArtistItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_artist, parent, false);
        return new ArtistItemHolder(v);
    }

    @Override
    public void onBindViewHolder(ArtistItemHolder holder, int position) {
        final Album album = albums.get(position);
        holder.getListText().setText(album.getName());
        holder.getListText().setSelected(true);
        holder.getListDesc().setSelected(true);
        holder.getListDesc().setText(album.getArtist() + " - " + album.getTotalSong() + " " + app.getString(R.string.songs));

        holder.getListMenu().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RealmResults<Music> musics = app.getRealm().where(Music.class).equalTo("albumKey", album.getAlbumKey()).findAll();
                mainActivity.filesDialogMenu(album.getName(), musics).show();
            }
        });

        holder.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StandAloneMusicFragment musicFragment = new StandAloneMusicFragment();
                musicFragment.setTitle(album.getName());
                musicFragment.setMusicList(app.getRealm().where(Music.class).equalTo("albumKey", album.getAlbumKey()).findAll());
                mainActivity.changePage(musicFragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }
}
