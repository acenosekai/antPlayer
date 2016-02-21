package com.acenosekai.antplayer.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.acenosekai.antplayer.App;
import com.acenosekai.antplayer.R;
import com.acenosekai.antplayer.ant.ExtendedSeekBar;
import com.acenosekai.antplayer.ant.SquareImageView;
import com.acenosekai.antplayer.ant.Utility;
import com.acenosekai.antplayer.realms.Cover;
import com.acenosekai.antplayer.realms.Music;
import com.acenosekai.antplayer.realms.repo.CoverRepo;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.view.IconicsImageView;

/**
 * Created by Acenosekai on 1/17/2016.
 * Rock On
 */
public class NowPlayingFragment extends BaseFragment {
    private long total;
    private ExtendedSeekBar extSeekBar;
    private SeekBar playbackSeekBar;
    private View fragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_now_playing, container, false);
        playbackSeekBar = (SeekBar) fragmentView.findViewById(R.id.playback_seek_bar);



        fragmentView.findViewById(R.id.playback_shuffle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getMainActivity().getPlaybackService().isShuffle()) {
                    getMainActivity().getPlaybackService().setShuffle(false);
                } else {
                    getMainActivity().getPlaybackService().setShuffle(true);
                }
            }
        });

        fragmentView.findViewById(R.id.playback_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getMainActivity().getPlaybackService().isPlaying()) {
                    getMainActivity().getPlaybackService().pause();
                } else {
                    getMainActivity().getPlaybackService().play();
                }
            }
        });

        fragmentView.findViewById(R.id.playback_repeat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (Integer.parseInt(getMainActivity().getPlaybackService().getRepeat())) {
                    case App.REPEAT.NO_REPEAT:
                        getMainActivity().getPlaybackService().setRepeat(String.valueOf(App.REPEAT.REPEAT_ALL));
                        break;
                    case App.REPEAT.REPEAT_ALL:
                        getMainActivity().getPlaybackService().setRepeat(String.valueOf(App.REPEAT.REPEAT_ONE));
                        break;
                    case App.REPEAT.REPEAT_ONE:
                        getMainActivity().getPlaybackService().setRepeat(String.valueOf(App.REPEAT.NO_REPEAT));
                        break;
                }
            }

        });

        fragmentView.findViewById(R.id.playback_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().getPlaybackService().next();
            }
        });

        fragmentView.findViewById(R.id.playback_prev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().getPlaybackService().previous();
            }
        });
        onPlaybackInit(getMainActivity().getPlaybackService().getMusic());

        return fragmentView;
    }

    private void generateSeekBar() {
        if(fragmentView !=null) {
            total = getMainActivity().getPlaybackService().getBytesTotal();
            extSeekBar = new ExtendedSeekBar(playbackSeekBar, 0, total, (int) total);

            extSeekBar.setOnExtendedSeekBarChangeListener(new ExtendedSeekBar.OnExtendedSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, float value, boolean fromUser) {
                    if (fromUser) {
                        getMainActivity().getPlaybackService().setBytesPosition((long) value);
                    }
                }
            });
        }
    }

    private void refreshShuffleButton(boolean shuffle) {
        if(fragmentView !=null) {
            if (shuffle) {
                ((IconicsImageView) fragmentView.findViewById(R.id.playback_shuffle)).setColor(ContextCompat.getColor(getMainActivity(), R.color.colorText));
            } else {
                ((IconicsImageView) fragmentView.findViewById(R.id.playback_shuffle)).setColor(ContextCompat.getColor(getMainActivity(), R.color.colorSemiTransparentWhite));
            }
        }
    }

    private void refreshPlayButton(boolean playing) {
        if(fragmentView !=null) {
            if (playing) {
                ((IconicsImageView) fragmentView.findViewById(R.id.playback_play)).setIcon(CommunityMaterial.Icon.cmd_pause_circle);
            } else {
                ((IconicsImageView) fragmentView.findViewById(R.id.playback_play)).setIcon(CommunityMaterial.Icon.cmd_play_circle);
            }
        }
    }

    private void refreshRepeatButton(int repeat) {
        if(fragmentView !=null) {
            switch (repeat) {
                case App.REPEAT.NO_REPEAT:
                    ((IconicsImageView) fragmentView.findViewById(R.id.playback_repeat)).setIcon(CommunityMaterial.Icon.cmd_repeat);
                    ((IconicsImageView) fragmentView.findViewById(R.id.playback_repeat)).setColor(ContextCompat.getColor(getMainActivity(), R.color.colorSemiTransparentWhite));
                    break;
                case App.REPEAT.REPEAT_ALL:
                    ((IconicsImageView) fragmentView.findViewById(R.id.playback_repeat)).setIcon(CommunityMaterial.Icon.cmd_repeat);
                    ((IconicsImageView) fragmentView.findViewById(R.id.playback_repeat)).setColor(ContextCompat.getColor(getMainActivity(), R.color.colorText));
                    break;
                case App.REPEAT.REPEAT_ONE:
                    ((IconicsImageView) fragmentView.findViewById(R.id.playback_repeat)).setIcon(CommunityMaterial.Icon.cmd_repeat_once);
                    ((IconicsImageView) fragmentView.findViewById(R.id.playback_repeat)).setColor(ContextCompat.getColor(getMainActivity(), R.color.colorText));
                    break;
            }
        }
    }


    @Override
    public void onPlaybackInit(Music music) {
        super.onPlaybackInit(music);
        if(fragmentView !=null) {
            ((TextView) fragmentView.findViewById(R.id.now_playing_music_title)).setText(music.getTitle());
            ((TextView) fragmentView.findViewById(R.id.now_playing_music_desc)).setText(music.getArtist() + "-" + music.getAlbum());

            SquareImageView musicCover = ((SquareImageView) fragmentView.findViewById(R.id.music_cover));

            Cover c = new CoverRepo(getApp().getRealm()).findOneCoverByAlbumKey(music.getAlbumKey());
            if (c.getCover() != null) {
                Bitmap bitMapCover = BitmapFactory.decodeByteArray(c.getCover(), 0, c.getCover().length);
                musicCover.setImageBitmap(bitMapCover);
            } else {
                musicCover.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.free_cover));
            }
            String timeStr = Utility.secondsToString(music.getLength());
            if (timeStr.startsWith("00:")) {
                timeStr = timeStr.substring(3, timeStr.length());
            }
            ((TextView) fragmentView.findViewById(R.id.position_total_text)).setText(timeStr);

            refreshPlayButton(getMainActivity().getPlaybackService().isPlaying());
            refreshShuffleButton(getMainActivity().getPlaybackService().isShuffle());
            refreshRepeatButton(Integer.parseInt(getMainActivity().getPlaybackService().getRepeat()));
        }
    }

    @Override
    public void onPlaybackPlayingRun(Music music) {
        super.onPlaybackPlayingRun(music);
        if(fragmentView !=null) {
            ((TextView) fragmentView.findViewById(R.id.position_text)).setText(getMainActivity().getPlaybackService().getPositionTime());
            if (extSeekBar == null) {
                generateSeekBar();
            }
            if (!extSeekBar.getSeekBar().isFocused()) {
                extSeekBar.setValue(getMainActivity().getPlaybackService().getBytesPosition());
            }
        }
    }

    @Override
    public void onPlaybackShuffleChange(boolean shuffle) {
        super.onPlaybackShuffleChange(shuffle);
        refreshShuffleButton(shuffle);
    }

    @Override
    public void onPlaybackPlayingStatusChange(boolean playing) {
        super.onPlaybackPlayingStatusChange(playing);
        refreshPlayButton(playing);
        if (playing) {
            generateSeekBar();
        }
    }

    @Override
    public void onPlaybackRepeatChange(String repeat) {
        super.onPlaybackRepeatChange(repeat);
        refreshRepeatButton(Integer.parseInt(repeat));
    }

}
