package com.bakrxc.audiorecorder;

import android.content.ContentValues;
import android.content.Context;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class AudioNoteRecorder {
    public Uri recordingUri; // Store the URI for the recorded audio
    private MediaRecorder recorder;
    private final Context context;

    public AudioNoteRecorder(Context context) {
        this.context = context;
    }

    public void startRecording() {
        // Define the relative path where audio should be saved
        String relativePath = Environment.DIRECTORY_MUSIC + "/" + AudioNotesManager.RELATIVE_PATH;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // For Android Q and above, save via MediaStore
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, "recorded_audio.aac"); // File name
            values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/aac"); // File type
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath); // Relative path

            Uri uri = context.getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
            recordingUri = uri; // Store the URI for the recorded file

            try {
                // Open an output stream for the file
                assert uri != null;
                OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
                if (outputStream != null) {
                    recorder = new MediaRecorder();
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

                    // Write audio data to the output stream
                    if (outputStream instanceof FileOutputStream) {
                        recorder.setOutputFile(((FileOutputStream) outputStream).getFD());
                    }

                    recorder.prepare();
                    recorder.start();
                    outputStream.close(); // Close the output stream
                    Log.d("AudioRecorder", "Recording started (Q+). File saved to: " + uri);
                }
            } catch (IOException e) {
                Log.e("AudioRecorder", "Error during recording (Q+)", e);
            }
        } else {
            // For Android versions below Q
            // Save to the external Music directory under "MyAppAudios"
            File musicDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), AudioNotesManager.RELATIVE_PATH);
            if (!musicDir.exists() && !musicDir.mkdirs()) {
                Log.e("AudioRecorder", "Failed to create directory: " + musicDir.getAbsolutePath());
                return;
            }

            // Define the output file
            File outputFile = new File(musicDir, "recorded_audio.aac");
            recordingUri = Uri.fromFile(outputFile); // Store the file path in recordingUri

            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            recorder.setOutputFile(outputFile.getAbsolutePath());

            try {
                recorder.prepare();
                recorder.start();
                Log.d("AudioRecorder", "Recording started (pre-Q). File saved to: " + outputFile.getAbsolutePath());
            } catch (IOException e) {
                Log.e("AudioRecorder", "Error during recording (pre-Q)", e);
            }
        }
    }

    public void stopRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }
    }
}
