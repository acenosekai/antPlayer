package com.acenosekai.antplayer.fragments;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;

/**
 * Created by Acenosekai on 1/16/2016.
 * Rock On
 */
public class BaseStandAloneFragment extends BaseFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMainActivity().getDrawerResult().getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }
}
