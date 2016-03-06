package com.acenosekai.antplayer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.acenosekai.antplayer.R;
import com.acenosekai.antplayer.adapters.FilesPagerAdapter;
import com.acenosekai.antplayer.fragments.files.AlbumFragment;
import com.acenosekai.antplayer.fragments.files.ArtistFragment;
import com.acenosekai.antplayer.fragments.files.FolderFragment;
import com.acenosekai.antplayer.fragments.files.MusicFragment;
import com.acenosekai.antplayer.fragments.files.PlaylistFragment;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.view.IconicsImageView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Acenosekai on 1/16/2016.
 * Rock On
 */
public class FilesFragment extends BaseFragment {
    private View fragmentView;
    private EditText searchText;

    public EditText getSearchText() {
        return searchText;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.fragmentView = inflater.inflate(R.layout.fragment_files, container, false);
        final ViewPager pager = (ViewPager) fragmentView.findViewById(R.id.files_pager);

        List<BaseStandAloneFragment> fl = new ArrayList<>();
        BaseStandAloneFragment artist = new ArtistFragment();
        BaseStandAloneFragment album = new AlbumFragment();
        BaseStandAloneFragment music = new MusicFragment();
        BaseStandAloneFragment folder = new FolderFragment();
        BaseStandAloneFragment playlist = new PlaylistFragment();

        fl.add(artist);
        fl.add(album);
        fl.add(music);
        fl.add(folder);
        fl.add(playlist);

        FilesPagerAdapter mAdapter = new FilesPagerAdapter(getChildFragmentManager(), fl);

        pager.setAdapter(mAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                hideAllBot();
                switch (position) {
                    case 0:
                        fragmentView.findViewById(R.id.fragment_files_artists_bot).setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        fragmentView.findViewById(R.id.fragment_files_albums_bot).setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        fragmentView.findViewById(R.id.fragment_files_musics_bot).setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        fragmentView.findViewById(R.id.fragment_files_folders_bot).setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        fragmentView.findViewById(R.id.fragment_files_playlist_bot).setVisibility(View.VISIBLE);
                        break;
                }
            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        fragmentView.findViewById(R.id.fragment_files_artists).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(0, true);
                hideAllBot();
                fragmentView.findViewById(R.id.fragment_files_artists_bot).setVisibility(View.VISIBLE);
            }
        });
        fragmentView.findViewById(R.id.fragment_files_albums).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(1, true);
                hideAllBot();
                fragmentView.findViewById(R.id.fragment_files_albums_bot).setVisibility(View.VISIBLE);
            }
        });
        fragmentView.findViewById(R.id.fragment_files_musics).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(2, true);
                hideAllBot();
                fragmentView.findViewById(R.id.fragment_files_musics_bot).setVisibility(View.VISIBLE);
            }
        });
        fragmentView.findViewById(R.id.fragment_files_folders).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(3, true);
                hideAllBot();
                fragmentView.findViewById(R.id.fragment_files_folders_bot).setVisibility(View.VISIBLE);
            }
        });
        fragmentView.findViewById(R.id.fragment_files_playlist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(4, true);
                hideAllBot();
                fragmentView.findViewById(R.id.fragment_files_playlist_bot).setVisibility(View.VISIBLE);
            }
        });


        final TextView title = (TextView) fragmentView.findViewById(R.id.title);
        searchText = (EditText) fragmentView.findViewById(R.id.search_text);

        final IconicsImageView searchToggle = (IconicsImageView) fragmentView.findViewById(R.id.search_toggle);
        searchToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.getVisibility() == View.VISIBLE) {
                    title.setVisibility(View.GONE);
                    searchText.setVisibility(View.VISIBLE);
                    searchText.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getMainActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(searchText, InputMethodManager.SHOW_IMPLICIT);
                    searchToggle.setIcon(CommunityMaterial.Icon.cmd_close);
                } else {
                    title.setVisibility(View.VISIBLE);
                    searchText.setText("");
                    searchText.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager) getMainActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    searchToggle.setIcon(CommunityMaterial.Icon.cmd_magnify);
                }
            }
        });

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //count search here
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() != KeyEvent.ACTION_DOWN) {
                    InputMethodManager imm = (InputMethodManager) getMainActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                } else {
                    return false;
                }

            }
        });

        return this.fragmentView;
    }

    private void hideAllBot() {
        fragmentView.findViewById(R.id.fragment_files_artists_bot).setVisibility(View.INVISIBLE);
        fragmentView.findViewById(R.id.fragment_files_albums_bot).setVisibility(View.INVISIBLE);
        fragmentView.findViewById(R.id.fragment_files_musics_bot).setVisibility(View.INVISIBLE);
        fragmentView.findViewById(R.id.fragment_files_folders_bot).setVisibility(View.INVISIBLE);
        fragmentView.findViewById(R.id.fragment_files_playlist_bot).setVisibility(View.INVISIBLE);

    }
}
