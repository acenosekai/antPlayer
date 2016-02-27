package com.un4seen.bass;

import java.nio.ByteBuffer;

public class BASS_TTA {
	// BASS_CHANNELINFO type
	public static final int BASS_CTYPE_STREAM_TTA = 0x10f00;

    static {
        System.loadLibrary("bass_tta");
    }

	public static native int BASS_TTA_StreamCreateFile(String file, long offset, long length, int flags);

	public static native int BASS_TTA_StreamCreateFile(ByteBuffer file, long offset, long length, int flags);

	public static native int BASS_TTA_StreamCreateFileUser(int system, int flags, BASS.BASS_FILEPROCS procs, Object user);
}
