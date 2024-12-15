package com.bakrxc.utils.permissions;

import android.app.Activity;
import android.widget.Toast;

public class Helper {

    public static void checkAndRequestPermissions(Activity activity, String[] permissions, int requestCode) {
        // Check if permissions are granted, if not, request them
        for (String permission : permissions) {
            if (!PermissionsUtil.isPermissionGranted(activity, permission)) {
                PermissionsUtil.requestPermissions(activity, permissions, requestCode);
            }
        }
    }

    public static void showToast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }
}
