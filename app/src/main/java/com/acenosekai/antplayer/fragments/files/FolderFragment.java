package com.acenosekai.antplayer.fragments.files;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acenosekai.antplayer.R;
import com.acenosekai.antplayer.adapters.FolderFileAdapter;
import com.acenosekai.antplayer.fragments.BaseStandAloneFragment;
import com.acenosekai.antplayer.models.Folder;
import com.acenosekai.antplayer.realms.Library;
import com.acenosekai.antplayer.realms.Music;
import com.acenosekai.antplayer.realms.repo.LibraryRepo;
import com.acenosekai.antplayer.realms.repo.MusicRepo;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by Acenosekai on 1/16/2016.
 * Rock On
 */
public class FolderFragment extends BaseStandAloneFragment {
    private FolderFileAdapter adapter;
    private Folder curentPath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_files_folder, container, false);
        MusicRepo musicRepo = new MusicRepo(getApp().getRealm());
        LibraryRepo libraryRepo = new LibraryRepo(getApp().getRealm());
        Folder rootTree = new Folder();
        curentPath = rootTree;
        rootTree.setPath("/");
        rootTree.setName(getMainActivity().getString(R.string.root_folder));
        rootTree.setDirectory(true);
        RealmResults<Library> libraries = libraryRepo.findAll();
        for (Library lib : libraries) {
            final Folder ft = new Folder();
            ft.setPath(lib.getPath());
            ft.setName(lib.getPath());
            ft.setDirectory(true);
            ft.setParent(rootTree);
            RealmResults<Music> musicFiles = musicRepo.findMusicInDirectory(lib.getPath());
            for (Music mf : musicFiles) {
                generateFileTree(ft, mf);
            }
            rootTree.getChilds().add(ft);
        }


        RecyclerView contentList = (RecyclerView) fragmentView.findViewById(R.id.content_list);
        LinearLayoutManager llm = new LinearLayoutManager(getMainActivity());
        contentList.setLayoutManager(llm);
        contentList.setHasFixedSize(true);
        ((TextView) fragmentView.findViewById(R.id.selected_folder)).setText(curentPath.getPath());
        fragmentView.findViewById(R.id.selected_folder).setSelected(true);

        this.adapter = new FolderFileAdapter(getMainActivity(), rootTree, new FolderFileAdapter.OnSelect() {
            @Override
            public void onSelect(Folder parent) {
                ((TextView) fragmentView.findViewById(R.id.selected_folder)).setText(parent.getPath());
                curentPath = parent;
            }
        });
        contentList.setAdapter(adapter);

        fragmentView.findViewById(R.id.folder_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curentPath.getParent() != null) {
                    adapter.setRootFolder(curentPath.getParent());
                    adapter.notifyDataSetChanged();
                    curentPath = curentPath.getParent();
                }
            }
        });

        return fragmentView;
    }


    public void generateFileTree(Folder ft, Music mf) {
        String[] arrParent = StringUtils.split(ft.getPath(), "\\/");
        String[] arrStr = StringUtils.split(mf.getPath(), "\\/");
        if (arrStr.length > arrParent.length) {
            boolean needAdd = false;
            Folder chTree = isChildExist(ft, ft.getPath() + "/" + arrStr[arrParent.length]);
            if (chTree == null) {
                needAdd = true;
                chTree = new Folder();
                chTree.setName(arrStr[arrParent.length]);
                chTree.setPath(ft.getPath() + "/" + arrStr[arrParent.length]);
                chTree.setParent(ft);
            }
            if (arrStr.length - arrParent.length > 1) {
                chTree.setDirectory(true);
                generateFileTree(chTree, mf);
            } else {
                chTree.setDirectory(false);
                chTree.setName(mf.getTitle());
            }
            if (needAdd) {
                ft.getChilds().add(chTree);
            }
        }
    }

    private Folder isChildExist(Folder ft, String path) {
        List<Folder> chList = ft.getChilds();
        for (Folder ftc : chList) {
            if (ftc.getPath().equals(path)) {
                return ftc;
            }
        }

        return null;
    }

}
