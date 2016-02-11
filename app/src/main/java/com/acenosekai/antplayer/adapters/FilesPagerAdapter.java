package com.acenosekai.antplayer.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.acenosekai.antplayer.fragments.BaseStandAloneFragment;

import java.util.List;

/**
 * Created by Acenosekai on 1/16/2016.
 * Rock On
 */
public class FilesPagerAdapter extends FragmentPagerAdapter {
    List<BaseStandAloneFragment> fragments;

    public FilesPagerAdapter(FragmentManager fm, List<BaseStandAloneFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int index) {
        return fragments.get(index);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
