package com.acenosekai.antplayer.fragments.files;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acenosekai.antplayer.R;
import com.acenosekai.antplayer.adapters.AlbumAdapter;
import com.acenosekai.antplayer.fragments.BaseStandAloneFragment;
import com.acenosekai.antplayer.fragments.FilesFragment;
import com.acenosekai.antplayer.realms.Music;

/**
 * Created by Acenosekai on 1/16/2016.
 * Rock On
 */
public class AlbumFragment extends BaseStandAloneFragment {

    private AlbumAdapter adapter;
    private RecyclerView contentList;
    private FilesFragment parent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_recycle, container, false);

        contentList = (RecyclerView) fragmentView.findViewById(R.id.content_list);
        LinearLayoutManager llm = new LinearLayoutManager(getMainActivity());
        contentList.setLayoutManager(llm);
        contentList.setHasFixedSize(true);
        parent = (FilesFragment) getParentFragment();
        parent.getSearchText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyAdapter();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        applyAdapter();

        return fragmentView;
    }

    private void applyAdapter() {
        this.adapter = new AlbumAdapter(getMainActivity(), getApp().getRealm().where(Music.class).contains("album", parent.getSearchText().getText().toString(), false).findAll());
        contentList.setAdapter(adapter);
    }
}
