package com.acenosekai.antplayer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acenosekai.antplayer.App;
import com.acenosekai.antplayer.MainActivity;
import com.acenosekai.antplayer.R;
import com.acenosekai.antplayer.holders.StringRemovableItemHolder;
import com.acenosekai.antplayer.realms.Library;
import com.acenosekai.antplayer.realms.Music;
import com.acenosekai.antplayer.realms.repo.MusicRepo;

import io.realm.RealmResults;

/**
 * Created by Acenosekai on 1/16/2016.
 * Rock On
 */
public class LibraryAdapter extends RecyclerView.Adapter<StringRemovableItemHolder> {

    private RealmResults<Library> libraries;
    private MainActivity activity;
    private App app;

    public LibraryAdapter(MainActivity activity, RealmResults<Library> libraries) {
        this.activity = activity;
        this.app = (App) activity.getApplication();
        this.libraries = libraries;
    }

    @Override
    public StringRemovableItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_string_removable, parent, false);
        return new StringRemovableItemHolder(v);
    }

    @Override
    public void onBindViewHolder(StringRemovableItemHolder holder, int position) {
        final MusicRepo musicRepo = new MusicRepo(app.getRealm());
        final Library lib = libraries.get(position);
        if (activity.getPlaybackService().isPlaying()) {
            activity.getPlaybackService().stop();
        }
        holder.getListText().setText(lib.getPath());
        holder.getListText().setSelected(true);
        holder.getRemoveItem().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.getRealm().beginTransaction();
                RealmResults<Music> m = musicRepo.findMusicInDirectory(lib.getPath());
                m.clear();
                lib.removeFromRealm();
                app.getRealm().commitTransaction();
                notifyDataSetChanged();
            }
        });

        if (new MusicRepo(app.getRealm()).countMusic() > 0) {
            activity.getPlaybackService().generateList();
            activity.getPlaybackService().init();
        }
    }

    @Override
    public int getItemCount() {
        return libraries.size();
    }
}
