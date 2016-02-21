package com.acenosekai.antplayer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acenosekai.antplayer.App;
import com.acenosekai.antplayer.MainActivity;
import com.acenosekai.antplayer.R;
import com.acenosekai.antplayer.holders.FolderItemHolder;
import com.acenosekai.antplayer.models.Folder;
import com.acenosekai.antplayer.realms.Music;
import com.acenosekai.antplayer.realms.repo.MusicRepo;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by Acenosekai on 1/17/2016.
 * Rock On
 */
public class FolderFileAdapter extends RecyclerView.Adapter<FolderItemHolder> {
    private App app;
    private MainActivity mainActivity;
    private Folder rootFolder;

    private List<Folder> folders;
    private OnSelect onSelect;

    public FolderFileAdapter(MainActivity mainActivity, Folder rootFolder, OnSelect onSelect) {
        this.mainActivity = mainActivity;
        this.app = (App) mainActivity.getApplication();
        this.rootFolder = rootFolder;
        this.folders = rootFolder.getChilds();
        this.onSelect = onSelect;
    }

    public void setRootFolder(Folder rootFolder) {
        this.rootFolder = rootFolder;
        this.folders = rootFolder.getChilds();
    }

    @Override
    public FolderItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_folder, parent, false);
        return new FolderItemHolder(v);
    }

    @Override
    public void onBindViewHolder(FolderItemHolder holder, int position) {
        final Folder f = folders.get(position);
        holder.getListText().setText(f.getName());
        holder.getListText().setSelected(true);

        final MusicRepo musicRepo = new MusicRepo(app.getRealm());
        if (f.isDirectory()) {
            holder.getListIcon().setIcon(CommunityMaterial.Icon.cmd_folder_outline);
        } else {
            holder.getListIcon().setIcon(CommunityMaterial.Icon.cmd_music_note);
        }

        holder.getListMenu().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RealmResults<Music> musics = musicRepo.findMusicInDirectory(f.getPath());
                mainActivity.filesDialogMenu(f.getName(), musics).show();
            }
        });
//
        holder.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (f.isDirectory()) {
                    onSelect.onSelect(f);
                    folders = f.getChilds();

                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }


    public interface OnSelect {
        void onSelect(Folder parent);
    }
}
