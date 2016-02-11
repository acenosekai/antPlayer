package com.acenosekai.antplayer.ant;

import android.util.Log;

import com.acenosekai.antplayer.App;
import com.un4seen.bass.BASS;

import java.io.File;

/**
 * Created by Acenosekai on 1/17/2016.
 * Rock On
 */
public class BassInit {

    private static final String TAG = "antPlay";
    private static BassInit instance;


    private BassInit() {

    }

    public static void destroy() {
        BASS.BASS_Free();
        instance = null;
    }

    public static void reset() {
        instance = null;
    }

    public static void configure(App app) {
        BASS.BASS_SetConfig(BASS.BASS_CONFIG_FLOATDSP, 1);
        BASS.BASS_SetConfig(BASS.BASS_CONFIG_DEV_BUFFER, 10);
        BASS.BASS_SetConfig(BASS.BASS_CONFIG_SRC, 3);
        BASS.BASS_SetConfig(BASS.BASS_CONFIG_SRC_SAMPLE, 3);
    }

    public static synchronized BassInit getInstance(App app) {
        if (instance == null) {
            BASS.BASS_Free();
            instance = new BassInit();
            if (!BASS.BASS_Init(-1, 48000, BASS.BASS_DEVICE_FREQ)) {
                Log.i(TAG, "Can't initialize device");
            }
            Log.i(TAG, "Can't initialize device");
//            }
            BASS.BASS_INFO info = new BASS.BASS_INFO();
            if (BASS.BASS_GetInfo(info)) {
                Log.i(TAG, "Min Buffer :" + info.minbuf);
                Log.i(TAG, "Direct Sound Ver :" + info.dsver);
                Log.i(TAG, "Latency :" + info.latency);
                Log.i(TAG, "speakers :" + info.speakers);
                Log.i(TAG, "freq :" + info.freq);
            }
            configure(app);
            String[] list = new File(app.getApplicationInfo().nativeLibraryDir).list();
            for (String s : list) {
                BASS.BASS_PluginLoad(app.getApplicationInfo().nativeLibraryDir + "/" + s, 0);
            }
        }

        return instance;
    }
}
