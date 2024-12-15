package com.bakrxc.utils.permissions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class AllPermissionsHelper {

    // General method to check if all permissions in a given list are granted
    public static boolean hasPermissions(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (!isPermissionGranted(context, permission)) {
                return false;
            }
        }
        return true;
    }

    // General method to request any array of permissions
    public static void requestPermissions(Activity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    // Requests media permissions for both audio and image, based on the device's SDK version
    public static void requestMediaPermissionsIfNeeded(Activity activity, int requestCode) {
        // Permissions for media files (audio and images)
        String[] permissions = {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                        ? android.Manifest.permission.READ_MEDIA_AUDIO
                        : android.Manifest.permission.READ_EXTERNAL_STORAGE,

                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                        ? android.Manifest.permission.READ_MEDIA_IMAGES
                        : android.Manifest.permission.READ_EXTERNAL_STORAGE
        };

        // Check if any permission is not granted, and request if needed
        if (!hasPermissions(activity, permissions)) {
            requestPermissions(activity, permissions, requestCode);
        }
    }

    // General permission check for any single permission
    public static boolean isPermissionGranted(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    // Checks if all permissions in a given array are granted
    public static boolean arePermissionsGranted(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (!isPermissionGranted(context, permission)) {
                return false;
            }
        }
        return true;
    }

    // Handle "Manage Storage" permission (API 30+)
    public static boolean hasManageStoragePermission(Context context) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && android.os.Environment.isExternalStorageManager();
    }

    // Request "Manage Storage" permission (API 30+)
    public static void requestManageStoragePermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            activity.startActivity(intent);
        }
    }

    // Request permission to read audio files, handling API differences (21–34)
    public static void requestReadAudioPermission(Activity activity, int requestCode) {
        String permission = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                ? android.Manifest.permission.READ_MEDIA_AUDIO
                : android.Manifest.permission.READ_EXTERNAL_STORAGE;
        requestPermissions(activity, new String[]{permission}, requestCode);
    }

    // Request permission to read image files, handling API differences (21–34)
    public static void requestReadImagePermission(Activity activity, int requestCode) {
        String permission = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                ? android.Manifest.permission.READ_MEDIA_IMAGES
                : android.Manifest.permission.READ_EXTERNAL_STORAGE;
        requestPermissions(activity, new String[]{permission}, requestCode);
    }

    // Check if camera permission is granted
    public static boolean hasCameraPermission(Context context) {
        return isPermissionGranted(context, android.Manifest.permission.CAMERA);
    }

    // Request camera permission
    public static void requestCameraPermission(Activity activity, int requestCode) {
        requestPermissions(activity, new String[]{android.Manifest.permission.CAMERA}, requestCode);
    }

    // Check if microphone permission is granted
    public static boolean hasMicrophonePermission(Context context) {
        return isPermissionGranted(context, android.Manifest.permission.RECORD_AUDIO);
    }

    // Request microphone permission
    public static void requestMicrophonePermission(Activity activity, int requestCode) {
        requestPermissions(activity, new String[]{android.Manifest.permission.RECORD_AUDIO}, requestCode);
    }

    // Check if location permissions are granted (Fine and Coarse Location)
    public static boolean hasLocationPermission(Context context) {
        return isPermissionGranted(context, android.Manifest.permission.ACCESS_FINE_LOCATION) &&
                isPermissionGranted(context, android.Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    // Request location permissions
    public static void requestLocationPermissions(Activity activity, int requestCode) {
        requestPermissions(activity, new String[]{
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
        }, requestCode);
    }

    // Check if storage permissions (audio, images, etc.) are granted
    private static boolean hasStoragePermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return isPermissionGranted(context, permission);
        } else {
            return isPermissionGranted(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    // Request storage permissions (audio, images, etc.)
    private static void requestStoragePermission(Activity activity, String permission, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(activity, new String[]{permission}, requestCode);
        } else {
            requestPermissions(activity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
        }
    }

    // Check if permission to save image is granted
    public static boolean hasSaveImagePermission(Context context) {
        return hasStoragePermission(context, android.Manifest.permission.READ_MEDIA_IMAGES);
    }

    // Request permission to save image
    public static void requestSaveImagePermission(Activity activity, int requestCode) {
        requestStoragePermission(activity, android.Manifest.permission.READ_MEDIA_IMAGES, requestCode);
    }

    // Check if permission to save audio is granted
    public static boolean hasSaveAudioPermission(Context context) {
        return hasStoragePermission(context, android.Manifest.permission.READ_MEDIA_AUDIO);
    }

    // Request permission to save audio
    public static void requestSaveAudioPermission(Activity activity, int requestCode) {
        requestStoragePermission(activity, android.Manifest.permission.READ_MEDIA_AUDIO, requestCode);
    }

    // Handle the result of permission requests
    public static void handlePermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults, PermissionResultCallback callback) {
        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i]);
                // Show rationale if permission is denied and should be explained
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i])) {
                    // Optionally, show a rationale dialog to the user
                } else {
                    // Handle permanently denied permissions (redirect to settings if necessary)
                }
            }
        }

        callback.onPermissionResult(deniedPermissions.isEmpty(), deniedPermissions);
    }

    // Interface for handling permission result callbacks
    public interface PermissionResultCallback {
        void onPermissionResult(boolean granted, List<String> deniedPermissions);
    }
}
