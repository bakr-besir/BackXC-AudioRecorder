
package com.bakrxc.audiorecorder;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bakrxc.utils.ArrayAdapterUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    AudioNoteRecorder audioNoteRecorder;
    AudioNotePlayer audioNotePlayer;
    private Button startButton, stopButton; // Play button added
    private ArrayAdapterUtil<AudioNote> adapter;
    List<AudioNote> audioNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listView);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        stopButton.setEnabled(false);

        audioNoteRecorder = new AudioNoteRecorder(this);
        audioNotePlayer = new AudioNotePlayer(this);

        // Initialize the adapter with an empty list
        audioNotes = new ArrayList<>();
        adapter = new ArrayAdapterUtil<>(this, R.layout.audio_item, audioNotes, (holder, item, position) -> {
            if (item != null) {
                try {
                    holder.<TextView>findView(R.id.audioNameTextView).setText(item.title != null ? item.title : "Unknown Title");
                    holder.<TextView>findView(R.id.audioDurationTextView).setText(item.duration != null ? item.duration : "00:00");
                    holder.<TextView>findView(R.id.audioArtistTextView).setText(item.artist != null ? item.artist : "Unknown Artist");
                    holder.<TextView>findView(R.id.audioUriTextView).setText(item.audioUri != null ? item.audioUri.getPath() : "No Path");
                } catch (Exception e) {
                    Log.e("AudioAdapter", "Error binding data to view", e);
                }
            } else {
                Log.e("AudioAdapter", "AudioNote is null at position: " + position);
            }
        });


        listView.setAdapter(adapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.READ_MEDIA_AUDIO,
                        Manifest.permission.RECORD_AUDIO
                }, 1);
            } else {

                List<AudioNote> loadedNotes = new AudioNotesManager().loadAudioNotes(this);
                if (loadedNotes != null) {
                    audioNotes.clear();
                    audioNotes.addAll(loadedNotes);
                    adapter.notifyDataSetChanged(); // Refresh the ListView
                } else {
                    Toast.makeText(this, "No audio notes found.", Toast.LENGTH_SHORT).show();
                }
            }
        } else { // Android 12 and below
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO
                }, 1);
            } else {
                List<AudioNote> loadedNotes = new AudioNotesManager().loadAudioNotes(this);
                if (loadedNotes != null) {
                    audioNotes.clear();
                    audioNotes.addAll(loadedNotes);
                    adapter.notifyDataSetChanged(); // Refresh the ListView
                } else {
                    Toast.makeText(this, "No audio notes found.", Toast.LENGTH_SHORT).show();
                }
            }
        }

        Toast.makeText(this, "READ_MEDIA_AUDIO:" +
                (checkSelfPermission(Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED) + "RECORD_AUDIO: " +
                (checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED), Toast.LENGTH_SHORT).show();

        listView.setOnItemClickListener((parent, view, position, id) -> audioNotePlayer.play(audioNotes.get(position).audioUri));
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            showConfirmDialog((dialog, which) -> {
                if (deleteAudioFile(audioNotes.get(position).audioUri)) {
                    Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                    audioNotes.remove(position);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, "Failed to delete!", Toast.LENGTH_SHORT).show();
                }

            });
            return false;
        });
        // Set up recording buttons
        startButton.setOnClickListener(v -> {
            audioNoteRecorder.startRecording();
            Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show();
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
        });

        stopButton.setOnClickListener(v -> {
            audioNoteRecorder.stopRecording();
            startButton.setEnabled(true);
            stopButton.setEnabled(false);

            Uri recordedUri = audioNoteRecorder.recordingUri;
            if (recordedUri != null) {
                audioNotes.add(new AudioNote(MainActivity.this, recordedUri));
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Recording failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, load the audio notes
                List<AudioNote> loadedNotes = new AudioNotesManager().loadAudioNotes(this);
                if (loadedNotes != null) {
                    audioNotes.clear();
                    audioNotes.addAll(loadedNotes);
                    adapter.notifyDataSetChanged(); // Refresh the ListView
                } else {
                    Toast.makeText(this, "No audio notes found.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Permission denied. Cannot load audio notes.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showConfirmDialog(DialogInterface.OnClickListener confirmListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Action");
        builder.setMessage("Are you sure to delete?");

        // Positive Button
        builder.setPositiveButton("Delete", confirmListener);

        // Negative Button
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        // Make the dialog cancelable
        builder.setCancelable(true);

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean deleteAudioFile(Uri audioUri) {
        if (!isUriValid(audioUri)) {
            Toast.makeText(this, "Error: Invalid URI.", Toast.LENGTH_SHORT).show();
            return false;
        }

        ContentResolver contentResolver = getContentResolver();
        try {
            // Attempt to delete the file
            int rowsDeleted = contentResolver.delete(audioUri, null, null);

            // Check if the file was deleted successfully
            return rowsDeleted > 0;
        } catch (SecurityException e) {
            // Handle permission issues or other exceptions
            Toast.makeText(this, "Error: Unable to delete file. Check permissions." + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private boolean isUriValid(Uri audioUri) {
        if (audioUri == null) {
            return false;
        }
        try (Cursor cursor = getContentResolver().query(audioUri, null, null, null, null)) {
            return cursor != null && cursor.moveToFirst(); // If a valid cursor is returned, the URI exists
        }
    }

}
