package com.bakrxc.audiorecorder;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.util.Locale;

public class AudioNote {
    public  String title;
    public  Uri audioUri;
    public  String duration;
    public  String artist;

    public AudioNote(Context context, Uri uri) {
        Log.e("SEX", "good! a");
        try (MediaMetadataRetriever retriever = new MediaMetadataRetriever()) {
            retriever.setDataSource(context, uri);

            this.audioUri = uri;

            this.title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            if (this.title == null) this.title = "Unknown Title";

            this.artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            if (this.artist == null) this.artist = "Unknown Artist";

            String durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            if (durationStr != null) {
                long durationMs = Long.parseLong(durationStr);
                long minutes = (durationMs / 1000) / 60;
                long seconds = (durationMs / 1000) % 60;
                duration = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
            } else {
                duration = "0";
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        Log.e("SEX", "good! z");
    }
}
