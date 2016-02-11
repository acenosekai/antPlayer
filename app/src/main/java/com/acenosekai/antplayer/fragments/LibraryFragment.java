package com.acenosekai.antplayer.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acenosekai.antplayer.R;
import com.acenosekai.antplayer.adapters.LibraryAdapter;
import com.acenosekai.antplayer.ant.FileCrawler;
import com.acenosekai.antplayer.realms.Library;

import io.realm.RealmResults;

/**
 * Created by Acenosekai on 1/16/2016.
 * Rock On
 */
public class LibraryFragment extends BaseFragment {

    private RealmResults<Library> libraries;
    private LibraryAdapter adapter;
    private boolean needReload = false;

    public RealmResults<Library> getLibraries() {
        return libraries;
    }

    public void setNeedReload(boolean needReload) {
        this.needReload = needReload;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_library, container, false);

        final RealmResults<Library> libraries = getApp().getRealm().where(Library.class).findAll();

        RecyclerView contentList = (RecyclerView) fragmentView.findViewById(R.id.content_list);
        LinearLayoutManager llm = new LinearLayoutManager(getMainActivity());
        contentList.setLayoutManager(llm);
        contentList.setHasFixedSize(true);

        this.adapter = new LibraryAdapter(getApp(), libraries);
        contentList.setAdapter(adapter);

        if (needReload) {
            LoadingFragment lf = new LoadingFragment();
            lf.setProcess(new LoadingFragment.Process() {
                @Override
                public void process() {
                    FileCrawler fc = new FileCrawler(getApp());
                    fc.reload(libraries);
                }
            });
            lf.setBackFragment(new LibraryFragment());
            getMainActivity().changePage(lf);
        }

        fragmentView.findViewById(R.id.reload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingFragment lf = new LoadingFragment();
                lf.setProcess(new LoadingFragment.Process() {
                    @Override
                    public void process() {
                        FileCrawler fc = new FileCrawler(getApp());
                        fc.reload(libraries);
                    }
                });
                lf.setBackFragment(new LibraryFragment());
                getMainActivity().changePage(lf);
            }
        });

        fragmentView.findViewById(R.id.add_folder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().changePage(new FolderSelectFragment());
            }
        });
        return fragmentView;
    }
}
