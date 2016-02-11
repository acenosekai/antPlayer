package com.acenosekai.antplayer.fragments;

import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acenosekai.antplayer.R;
import com.acenosekai.antplayer.adapters.MusicAdapter;
import com.acenosekai.antplayer.realms.Music;

import io.realm.RealmResults;


/**
 * Created by Acenosekai on 2/10/2016.
 * Rock On
 */
public class StandAloneMusicFragment extends BaseStandAloneFragment {
    private View fragmentView;
    private MusicAdapter adapter;

    private String title;

    private RealmResults<Music> musicList;

    public void setMusicList(RealmResults<Music> musicList) {
        this.musicList = musicList;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_music_stand_alone, container, false);
        if (title != null) {
            TextView titleView = (TextView) fragmentView.findViewById(R.id.title);
            titleView.setSelected(true);
            titleView.setText(title);
        }
        RecyclerView contentList = (RecyclerView) fragmentView.findViewById(R.id.content_list);
        LinearLayoutManager llm = new LinearLayoutManager(getMainActivity());
        contentList.setLayoutManager(llm);
        contentList.setHasFixedSize(true);

        this.adapter = new MusicAdapter(getMainActivity(), musicList);
        contentList.setAdapter(adapter);
        return fragmentView;
    }
}
