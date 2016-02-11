package com.acenosekai.antplayer.ant;

import android.widget.SeekBar;

/**
 * Created by Acenosekai on 1/17/2016.
 * Rock On
 */
public class ExtendedSeekBar {
    private SeekBar seekBar;
    private float value;
    private float min = 0;
    private float max;
    private int step;
    private float dif = 0;
    private float nMax;
    private OnExtendedSeekBarChangeListener onExtendedSeekBarChangeListener;

    public ExtendedSeekBar(SeekBar seekBar, float min, float max, int step) {
        this.seekBar = seekBar;
        setMin(min);
        setMax(max);
        setStep(step);
    }

    private void populate() {
        this.dif = 0 - min;
        this.nMax = max + dif;
    }

    public SeekBar getSeekBar() {
        return seekBar;
    }

    public void setSeekBar(SeekBar seekBar) {
        this.seekBar = seekBar;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {

        this.min = min;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        seekBar.setMax(step);
        this.step = step;
    }

    public void setOnExtendedSeekBarChangeListener(final OnExtendedSeekBarChangeListener onExtendedSeekBarChangeListener) {
        this.onExtendedSeekBarChangeListener = onExtendedSeekBarChangeListener;
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                onExtendedSeekBarChangeListener.onProgressChanged(seekBar, progress, valueFromProgress(progress), fromUser);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

//    public OnExtendedSeekBarChangeListener getOnExtendedSeekBarChangeListener() {
//        return onExtendedSeekBarChangeListener;
//    }

    public float getValue() {
        return valueFromProgress(seekBar.getProgress());
    }

    public void setValue(float value) {
        populate();
        float stepValue = ((value + dif) * (float) step) / nMax;
        seekBar.setProgress((int) stepValue);
        this.value = value;
    }

    private float valueFromProgress(int progress) {
        populate();
        float floatProgress = ((float) progress) / ((float) step);
        return (nMax * floatProgress) - dif;
    }

    public interface OnExtendedSeekBarChangeListener {
        void onProgressChanged(SeekBar seekBar, int progress, float value, boolean fromUser);
    }
}
