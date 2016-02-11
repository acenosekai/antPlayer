package com.acenosekai.antplayer.fragments;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.acenosekai.antplayer.R;
import com.acenosekai.antplayer.adapters.FolderAdapter;
import com.acenosekai.antplayer.realms.Library;

import io.realm.RealmResults;

/**
 * Created by Acenosekai on 1/16/2016.
 * Rock On
 */
public class FolderSelectFragment extends BaseStandAloneFragment {


    private String selectedPath = Environment.getExternalStorageDirectory().getPath();
    private FolderAdapter adapter;
    private View fragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getMainActivity().getDrawerResult().getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        fragmentView = inflater.inflate(R.layout.fragment_folder_select, container, false);
        setBackFragment(new LibraryFragment());


        RecyclerView contentList = (RecyclerView) fragmentView.findViewById(R.id.content_list);
        LinearLayoutManager llm = new LinearLayoutManager(getMainActivity());
        contentList.setLayoutManager(llm);
        contentList.setHasFixedSize(true);

        this.adapter = new FolderAdapter(selectedPath, new FolderAdapter.OnClickFolder() {
            @Override
            public void onClickFolder(String path) {
                selectedPath = path;
                setSelectedFolderText(selectedPath);
            }
        });

        contentList.setAdapter(adapter);

        fragmentView.findViewById(R.id.folder_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.up();
            }
        });

        fragmentView.findViewById(R.id.select_folder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RealmResults<Library> libraries = getApp().getRealm().where(Library.class).findAll();
                boolean duplicate = false;
                boolean parentRegistered = false;
                for (Library l : libraries) {
                    if (parentRegistered = selectedPath.equals(l.getPath())) {
                        break;
                    } else if (duplicate = selectedPath.startsWith(l.getPath())) {
                        break;
                    }
                }

                if (parentRegistered) {
                    Toast.makeText(getActivity(), "Parent Folder already registered.", Toast.LENGTH_SHORT).show();
                } else if (duplicate) {
                    Toast.makeText(getActivity(), "This Folder already registered.", Toast.LENGTH_SHORT).show();
                } else {
                    Library lib = new Library();
                    lib.setPath(selectedPath);
                    getApp().getRealm().beginTransaction();
                    getApp().getRealm().where(Library.class).beginsWith("path", selectedPath).findAll().clear();
                    getApp().getRealm().copyToRealmOrUpdate(lib);
                    getApp().getRealm().commitTransaction();

                    LibraryFragment lf = new LibraryFragment();
                    lf.setNeedReload(true);

                    getMainActivity().changePage(lf);
                }
            }
        });

        setSelectedFolderText(selectedPath);

        return fragmentView;
    }

    public void setSelectedFolderText(String path) {
        fragmentView.findViewById(R.id.selected_folder).setSelected(true);
        ((TextView) fragmentView.findViewById(R.id.selected_folder)).setText(path);
    }
}
