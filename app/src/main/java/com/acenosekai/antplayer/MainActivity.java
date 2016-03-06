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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.acenosekai.antplayer.fragments.BaseFragment;
import com.acenosekai.antplayer.fragments.FilesFragment;
import com.acenosekai.antplayer.fragments.LibraryFragment;
import com.acenosekai.antplayer.fragments.NowPlayingFragment;
import com.acenosekai.antplayer.realms.Music;
import com.acenosekai.antplayer.realms.Playlist;
import com.acenosekai.antplayer.realms.repo.MusicRepo;
import com.acenosekai.antplayer.realms.repo.PlaylistRepo;
import com.acenosekai.antplayer.services.PlaybackService;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BaseFragment currentFragment;
    private Drawer drawerResult;
    private Class<? extends BaseFragment> initialFragmentClass;
    private PlaybackService playbackService;
    private PlaylistRepo playlistRepo;
    protected ServiceConnection mServerConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            playlistRepo = new PlaylistRepo(((App) getApplication()).getRealm());
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
                    try {
                        currentFragment.onPlaybackInit(music);
                    } catch (Exception e) {
                        //prevent error on fragment already destroyed
                    }
                }
            });

            playbackService.setOnPlayingRun(new PlaybackService.OnPlayingRun() {
                @Override
                public void onPlayingRun(Music music) {
                    try {
                        currentFragment.onPlaybackPlayingRun(music);
                    } catch (Exception e) {
                        //prevent error on fragment already destroyed
                    }
                }
            });

            playbackService.setOnShuffleChange(new PlaybackService.OnShuffleChange() {
                @Override
                public void onShuffleChange(boolean shuffle) {
                    try {
                        currentFragment.onPlaybackShuffleChange(shuffle);
                    } catch (Exception e) {
                        //prevent error on fragment already destroyed
                    }

                }
            });

            playbackService.setOnPlayingStatusChange(new PlaybackService.OnPlayingStatusChange() {
                @Override
                public void onPlayingStatusChanged(boolean playing) {
                    try {
                        currentFragment.onPlaybackPlayingStatusChange(playing);
                    } catch (Exception e) {
                        //prevent error on fragment already destroyed
                    }
                }
            });

            playbackService.setOnRepeatChange(new PlaybackService.OnRepeatChange() {
                @Override
                public void onRepeatChange(String repeat) {
                    try {
                        currentFragment.onPlaybackRepeatChange(repeat);
                    } catch (Exception e) {
                        //prevent error on fragment already destroyed
                    }
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

    public MaterialDialog newPlaylistDialog(String title, final Collection<? extends Music> musics) {
        View nPlaylistView = getLayoutInflater().inflate(R.layout.dialog_new_playlist, null);
        final EditText newPlaylistText = (EditText) nPlaylistView.findViewById(R.id.new_playlist_text);

        final MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(this)
                .title(title)
                .customView(nPlaylistView, true)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        _saveNewPlaylist(newPlaylistText.getText().toString(), (List<Music>) musics);
                    }
                })
                .positiveText(R.string.OK);

        final MaterialDialog md = dialogBuilder.build();

        newPlaylistText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() != KeyEvent.ACTION_DOWN) {
                    if (_saveNewPlaylist(newPlaylistText.getText().toString(), (List<Music>) musics)) {
                        InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        md.dismiss();
                    }
                    return true;
                } else {
                    return false;
                }

            }
        });

        return md;
    }

    private boolean _saveNewPlaylist(String name, List<Music> musics) {
        switch (playlistRepo.addToNewPlaylist(name, musics)) {
            case PlaylistRepo.ADDNEWPLALIST_NULL:
                Toast.makeText(MainActivity.this, "Playlist name is required.", Toast.LENGTH_SHORT).show();
                break;
            case PlaylistRepo.ADDNEWPLALIST_EXIST:
                Toast.makeText(MainActivity.this, "Playlist already exist.", Toast.LENGTH_SHORT).show();
                break;
            case PlaylistRepo.ADDNEWPLALIST_SUCCESS:
                return true;
        }
        return false;
    }

    public MaterialDialog.Builder playlistSelectDialogMenu(final String title, final Collection<? extends Music> musics) {
        List<Playlist> playlistList = playlistRepo.findUserPlaylist();
        List<String> nameList = new ArrayList<>();
        for (Playlist p : playlistList) {
            nameList.add(p.getName());
        }
        CharSequence[] charSequenceItems = nameList.toArray(new CharSequence[nameList.size()]);
        return new MaterialDialog.Builder(this)
                .title(title)
                .items(charSequenceItems)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        playlistRepo.addToPlaylist(text.toString(), (List<Music>) musics);
                    }
                });
    }

    public MaterialDialog.Builder playlistDialogMenu(final String title, final Collection<? extends Music> musics) {
        int menuId = R.array.files_menu_playlist;
        if (playlistRepo.findUserPlaylist().isEmpty()) {
            menuId = R.array.files_menu_new_playlist;
        }
        MaterialDialog.Builder dBuilder = new MaterialDialog.Builder(this)
                .title(title)
                .items(menuId)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                playlistRepo.addToPlaylist((List<Music>) musics);
                                playbackService.generateList();
                                break;
                            case 1:
                                newPlaylistDialog(title, musics).show();
                                break;
                            case 2:
                                playlistSelectDialogMenu(title, musics).show();
                                break;
                        }
                    }
                });

        return dBuilder;
    }

    public MaterialDialog.Builder filesDialogMenu(final String title, final Collection<? extends Music> musics) {
        return new MaterialDialog.Builder(this)
                .title(title)
                .items(R.array.files_menu)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                ((App) getApplication()).saveRegistry(App.REGISTRY.SONG_POSITION, "0");
                                playlistRepo.saveMusicList((List<Music>) musics);
                                NowPlayingFragment npf = new NowPlayingFragment();

                                if (getPlaybackService().isPlaying()) {
                                    getPlaybackService().stop();
                                }
                                getPlaybackService().generateList();
                                getPlaybackService().init();

                                getPlaybackService().play(0);
                                changePage(npf);


                                break;
                            case 1:
                                playlistDialogMenu(title, musics).show();
                                break;
                        }
                    }
                });
    }


}
