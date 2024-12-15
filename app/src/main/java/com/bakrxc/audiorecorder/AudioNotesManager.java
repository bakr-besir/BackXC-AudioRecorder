package com.bakrxc.audiorecorder;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AudioNotesManager {

    public static final String RELATIVE_PATH = "my-audios"; // Subfolder for your app's audios

    public List<AudioNote> loadAudioNotes(Context context) {
        List<AudioNote> audios = new ArrayList<>();

        // Define the projection (columns you want to retrieve)
        String[] projection = {
                MediaStore.Audio.Media._ID,           // Unique ID for each media file
                MediaStore.Audio.Media.DISPLAY_NAME,  // Name of the audio file
                MediaStore.Audio.Media.RELATIVE_PATH  // Path of the audio file
        };

        // Define the selection (filter only files from the "my-audios" folder)
        String selection = MediaStore.Audio.Media.RELATIVE_PATH + " LIKE ?";

        String[] selectionArgs = new String[]{Environment.DIRECTORY_MUSIC + "/" + RELATIVE_PATH + "/%"}; // Only files in "my-audios" folder

        // Query the MediaStore for audio files in the "my-audios" folder
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,  // The URI for audio files
                projection,                                    // The columns we want to retrieve
                selection,                                     // The selection (filter by folder)
                selectionArgs,                                 // The selection arguments
                null                                           // Sort order
        );

        // If the cursor contains data
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Log.e("SEX", "good! 0");
                // Get the unique ID for each file (this will be used to create the URI)
                long audioId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                Log.e("SEX", "good! 1");
                // Build the URI for the audio file
                Uri audioUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, audioId);
                Log.e("SEX", "good! 2");
                // Add the URI to the list
                audios.add(new AudioNote(context, audioUri));
                Log.e("SEX", "good! 3");

            }
            Log.e("SEX", "good!");
            cursor.close();
        }

        // Now you have a list of URIs, you can use them as needed
        return audios;
    }
}
