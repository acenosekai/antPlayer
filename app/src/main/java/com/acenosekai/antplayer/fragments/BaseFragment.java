package com.acenosekai.antplayer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;

import com.acenosekai.antplayer.App;
import com.acenosekai.antplayer.MainActivity;
import com.acenosekai.antplayer.realms.Music;

/**
 * Created by Acenosekai on 1/16/2016.
 * Rock On
 */
public class BaseFragment extends Fragment {
    private MainActivity mainActivity;
    private App app;

    private BaseFragment backFragment;

    public void onPlaybackInit(Music music) {
    }

    public void onPlaybackPlayingRun(Music music) {
    }

    public void onPlaybackShuffleChange(boolean shuffle) {
    }

    public void onPlaybackPlayingStatusChange(boolean playing) {
    }

    public void onPlaybackRepeatChange(String repeat) {
    }

    public BaseFragment getBackFragment() {
        if (backFragment == null) {
            return mainActivity.getInitialFragment();
        }
        return backFragment;
    }

    public void setBackFragment(BaseFragment backFragment) {
        this.backFragment = backFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        app = (App) getActivity().getApplication();
        mainActivity.getDrawerResult().getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public App getApp() {
        return app;
    }


}
