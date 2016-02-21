package com.acenosekai.antplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.acenosekai.antplayer.fragments.BaseFragment;
import com.acenosekai.antplayer.fragments.FilesFragment;
import com.acenosekai.antplayer.fragments.LibraryFragment;
import com.acenosekai.antplayer.fragments.NowPlayingFragment;
import com.acenosekai.antplayer.realms.Music;
import com.acenosekai.antplayer.realms.Playlist;
import com.acenosekai.antplayer.realms.repo.MusicRepo;
import com.acenosekai.antplayer.services.PlaybackService;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    BaseFragment currentFragment;
    private Drawer drawerResult;
    private Class<? extends BaseFragment> initialFragmentClass;
    private PlaybackService playbackService;
    protected ServiceConnection mServerConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            playbackService = ((PlaybackService.PlaybackServiceBinder) binder).getService();
            PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName(R.string.fragment_files_title);
            SecondaryDrawerItem item2 = new SecondaryDrawerItem().withName(R.string.fragment_library_title);
            SecondaryDrawerItem item3 = new SecondaryDrawerItem().withName(R.string.fragment_now_playing_title);
            drawerResult = new DrawerBuilder()
                    .withActivity(MainActivity.this)
                    .addDrawerItems(
                            item1,
                            new DividerDrawerItem(),
                            item2,
                            item3
                    )
                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                            // do something with the clicked item :D
                            switch (position) {
                                case 0:
                                    changePage(new FilesFragment());
                                    break;
                                case 2:
                                    changePage(new LibraryFragment());
                                    break;
                                case 3:
                                    changePage(new NowPlayingFragment());
                                    break;
                            }
                            return false;
                        }
                    })
                    .build();

            playbackService.setOnInit(new PlaybackService.OnInit() {
                @Override
                public void onInit(Music music) {
                    currentFragment.onPlaybackInit(music);
                }
            });

            playbackService.setOnPlayingRun(new PlaybackService.OnPlayingRun() {
                @Override
                public void onPlayingRun(Music music) {
                    currentFragment.onPlaybackPlayingRun(music);
                }
            });

            playbackService.setOnShuffleChange(new PlaybackService.OnShuffleChange() {
                @Override
                public void onShuffleChange(boolean shuffle) {
                    currentFragment.onPlaybackShuffleChange(shuffle);

                }
            });

            playbackService.setOnPlayingStatusChange(new PlaybackService.OnPlayingStatusChange() {
                @Override
                public void onPlayingStatusChanged(boolean playing) {
                    currentFragment.onPlaybackPlayingStatusChange(playing);
                }
            });

            playbackService.setOnRepeatChange(new PlaybackService.OnRepeatChange() {
                @Override
                public void onRepeatChange(String repeat) {
                    currentFragment.onPlaybackRepeatChange(repeat);
                }
            });

            if (new MusicRepo(((App) getApplication()).getRealm()).countMusic() > 0) {
                changePage(getInitialFragment());

                playbackService.generateList();
                playbackService.init();
            } else {
                changePage(new LibraryFragment());
            }


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private boolean doubleBackToExitPressedOnce = false;

    public PlaybackService getPlaybackService() {
        return playbackService;
    }

    public BaseFragment getInitialFragment() {
        try {
            return initialFragmentClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialFragmentClass = FilesFragment.class;

        if (playbackService == null) {
            bindServices();
        }
    }

    public Drawer getDrawerResult() {
        return drawerResult;
    }

    public void changePage(BaseFragment fragment) {
        if (currentFragment == null || !currentFragment.getClass().getName().equals(fragment.getClass().getName())) {
            forceChangePage(fragment);
        }
    }

    public void forceChangePage(BaseFragment fragment) {
        //LoadingFragment
        if (new MusicRepo(((App) getApplication()).getRealm()).countMusic() == 0
                && !fragment.getClass().getSimpleName().equals("FolderSelectFragment")
                && !fragment.getClass().getSimpleName().equals("LoadingFragment")
                ) {
            Toast.makeText(this, "No music file registered. Please Add files to library", Toast.LENGTH_SHORT).show();
            fragment = new LibraryFragment();
        }
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.base_container, fragment);
        transaction.commit();
        currentFragment = fragment;
    }

    public void showDrawer(View view) {
        drawerResult.openDrawer();
    }

    private void back() {
        if (currentFragment.getBackFragment() != null) {
            changePage(currentFragment.getBackFragment());
        }
    }

    public void backPage(View view) {
        back();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            if (currentFragment.getClass().getName().equals(initialFragmentClass.getName())) {
                super.onBackPressed();
                return;
            }
        }

        this.doubleBackToExitPressedOnce = true;
        back();

        if (currentFragment.getClass().getName().equals(initialFragmentClass.getName())) {
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        }

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 500);
    }

    public void bindServices() {
        try {
            unbindService(mServerConn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent playIntent = new Intent(MainActivity.this, PlaybackService.class);
        bindService(playIntent, mServerConn, BIND_AUTO_CREATE);
        playIntent.setAction(PlaybackService.BIND_PLAYBACK);
        startService(playIntent);
    }

    public MaterialDialog.Builder filesDialogMenu(String title, final Collection<? extends Music> musics) {
        return new MaterialDialog.Builder(this)
                .title(title)
                .items(R.array.files_menu)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                ((App) getApplication()).getRealm().beginTransaction();
                                Playlist p = ((App) getApplication()).getRealm().where(Playlist.class).equalTo("name", "Current Playlist").findFirst();
                                p.getMusicFileList().clear();
                                p.getMusicFileListShuffle().clear();
                                ((App) getApplication()).setRegistry(App.REGISTRY.SONG_POSITION, "0");
                                p.getMusicFileList().addAll(musics);
                                ((App) getApplication()).getRealm().copyToRealmOrUpdate(p);
                                ((App) getApplication()).getRealm().commitTransaction();
                                NowPlayingFragment npf = new NowPlayingFragment();
//                                npf.setShowAndPlay(true);
                                //                npf.setShowAndPlay(true);


                                if (getPlaybackService().isPlaying()) {
                                    getPlaybackService().stop();
                                }
                                getPlaybackService().generateList();
                                getPlaybackService().init();

                                getPlaybackService().play(0);
                                changePage(npf);


                                break;
                            case 1:
                                break;
                        }
                    }
                });
    }


}
