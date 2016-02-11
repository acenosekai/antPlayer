package com.acenosekai.antplayer.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acenosekai.antplayer.R;

/**
 * Created by Acenosekai on 1/16/2016.
 * Rock On
 */
public class LoadingFragment extends BaseStandAloneFragment {
    private Process process;

    public void setProcess(Process process) {
        this.process = process;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                process.process();
                getMainActivity().changePage(getBackFragment());
            }
        }, 200);
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    public interface Process {
        void process();
    }
}
