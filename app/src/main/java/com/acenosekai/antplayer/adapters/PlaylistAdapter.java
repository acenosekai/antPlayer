package com.acenosekai.antplayer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acenosekai.antplayer.App;
import com.acenosekai.antplayer.MainActivity;
import com.acenosekai.antplayer.R;
import com.acenosekai.antplayer.holders.FolderItemHolder;
import com.acenosekai.antplayer.realms.Playlist;

import java.util.List;

/**
 * Created by Acenosekai on 3/6/2016.
 * Rock On
 */
public class PlaylistAdapter extends RecyclerView.Adapter<FolderItemHolder> {
    private MainActivity mainActivity;
    private App app;
    //    private RealmResults<Music> musics;
    private List<Playlist> playlistList;

    public PlaylistAdapter(MainActivity mainActivity, List<Playlist> playlistList) {
        this.mainActivity = mainActivity;
        this.app = (App) mainActivity.getApplication();
        this.playlistList = playlistList;
    }

    @Override
    public FolderItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_folder, parent, false);
        return new FolderItemHolder(v);
    }

    @Override
    public void onBindViewHolder(FolderItemHolder holder, int position) {
        final Playlist playlist = playlistList.get(position);
        holder.getListText().setText(playlist.getName());
        holder.getListText().setSelected(true);

//        final MusicRepo musicRepo = new MusicRepo(app.getRealm());
//        if (f.isDirectory()) {
//            holder.getListIcon().setIcon(CommunityMaterial.Icon.cmd_folder_outline);
//        } else {
//            holder.getListIcon().setIcon(CommunityMaterial.Icon.cmd_music_note);
//        }

//        holder.getListMenu().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                RealmResults<Music> musics = musicRepo.findMusicInDirectory(f.getPath());
//                mainActivity.filesDialogMenu(f.getName(), musics).show();
//            }
//        });
////
//        holder.getItemView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (f.isDirectory()) {
//                    onSelect.onSelect(f);
//                    folders = f.getChilds();
//
//                    notifyDataSetChanged();
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return playlistList.size();
    }
}
