package com.bakrxc.audiorecorder;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Toast;

import java.io.IOException;

public class AudioNotePlayer {
    private MediaPlayer mediaPlayer;
    private final Context context;

    public AudioNotePlayer(Context context) {
        this.context = context;
    }

    public void play(Uri uri) {
        if (uri != null) {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(context, uri); // Use the URI of the last recorded audio
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.fillInStackTrace();
            }
        } else {
            Toast.makeText(context, "No recording found", Toast.LENGTH_SHORT).show();
        }
    }
}
