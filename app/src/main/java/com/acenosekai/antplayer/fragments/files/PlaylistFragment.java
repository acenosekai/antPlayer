package com.acenosekai.antplayer.fragments.files;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acenosekai.antplayer.R;
import com.acenosekai.antplayer.adapters.PlaylistAdapter;
import com.acenosekai.antplayer.fragments.BaseStandAloneFragment;
import com.acenosekai.antplayer.fragments.FilesFragment;
import com.acenosekai.antplayer.realms.repo.PlaylistRepo;

/**
 * Created by Acenosekai on 1/16/2016.
 * Rock On
 */
public class PlaylistFragment extends BaseStandAloneFragment {
    private FilesFragment parent;
    private RecyclerView contentList;
    private PlaylistAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_recycle, container, false);
        contentList = (RecyclerView) fragmentView.findViewById(R.id.content_list);
        LinearLayoutManager llm = new LinearLayoutManager(getMainActivity());
        contentList.setLayoutManager(llm);
        contentList.setHasFixedSize(true);


        PlaylistRepo playistRepo = new PlaylistRepo(getApp().getRealm());
        this.adapter = new PlaylistAdapter(getMainActivity(), playistRepo.findUserPlaylist());
        contentList.setAdapter(adapter);

        return fragmentView;
    }
}
