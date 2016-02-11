package com.acenosekai.antplayer.fragments.files;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acenosekai.antplayer.R;
import com.acenosekai.antplayer.fragments.BaseStandAloneFragment;

/**
 * Created by Acenosekai on 1/16/2016.
 * Rock On
 */
public class PlaylistFragment extends BaseStandAloneFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_recycle, container, false);
        return fragmentView;
    }
}
