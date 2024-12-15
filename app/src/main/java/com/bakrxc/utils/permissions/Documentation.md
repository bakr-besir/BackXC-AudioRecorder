# `AllPermissionsHelper` Class Documentation

The `AllPermissionsHelper` class provides a set of utility methods for managing runtime permissions in Android applications. It handles various Android permissions, including media permissions (audio, images), storage, camera, microphone, location, and "Manage Storage" permissions (API 30+). The class offers methods for checking permissions, requesting permissions, and handling the results of permission requests.

## Methods Overview:

### 1. **`hasPermissions(Context context, String[] permissions)`**
- **Description**: Checks whether all permissions in the provided array are granted.
- **Parameters**:
    - `context`: The application context.
    - `permissions`: An array of permission strings to check.
- **Returns**: `true` if all permissions are granted; otherwise, `false`.

### 2. **`requestPermissions(Activity activity, String[] permissions, int requestCode)`**
- **Description**: Requests the specified permissions from the user.
- **Parameters**:
    - `activity`: The activity from which the request is made.
    - `permissions`: An array of permission strings to request.
    - `requestCode`: The request code used to identify the request.
- **Returns**: None.

### 3. **`requestMediaPermissionsIfNeeded(Activity activity, int requestCode)`**
- **Description**: Requests permissions to read audio and image files. It checks the Android version and adapts to the appropriate permissions (`READ_MEDIA_AUDIO` and `READ_MEDIA_IMAGES` for API 33+, or `READ_EXTERNAL_STORAGE` for earlier versions).
- **Parameters**:
    - `activity`: The activity from which the request is made.
    - `requestCode`: The request code used to identify the request.
- **Returns**: None.

### 4. **`isPermissionGranted(Context context, String permission)`**
- **Description**: Checks if a single permission is granted.
- **Parameters**:
    - `context`: The application context.
    - `permission`: The permission string to check.
- **Returns**: `true` if the permission is granted, otherwise `false`.

### 5. **`arePermissionsGranted(Context context, String[] permissions)`**
- **Description**: Checks if all permissions in the provided array are granted.
- **Parameters**:
    - `context`: The application context.
    - `permissions`: An array of permission strings to check.
- **Returns**: `true` if all permissions are granted, otherwise `false`.

### 6. **`hasManageStoragePermission(Context context)`**
- **Description**: Checks if the "Manage Storage" permission (API 30+) is granted.
- **Parameters**:
    - `context`: The application context.
- **Returns**: `true` if the "Manage Storage" permission is granted; otherwise, `false`.

### 7. **`requestManageStoragePermission(Activity activity)`**
- **Description**: Requests the "Manage Storage" permission (API 30+).
- **Parameters**:
    - `activity`: The activity from which the request is made.
- **Returns**: None.

### 8. **`requestReadAudioPermission(Activity activity, int requestCode)`**
- **Description**: Requests permission to read audio files, adapting to API differences for Android versions before and after API 33.
- **Parameters**:
    - `activity`: The activity from which the request is made.
    - `requestCode`: The request code used to identify the request.
- **Returns**: None.

### 9. **`requestReadImagePermission(Activity activity, int requestCode)`**
- **Description**: Requests permission to read image files, adapting to API differences for Android versions before and after API 33.
- **Parameters**:
    - `activity`: The activity from which the request is made.
    - `requestCode`: The request code used to identify the request.
- **Returns**: None.

### 10. **`hasCameraPermission(Context context)`**
- **Description**: Checks if the camera permission is granted.
- **Parameters**:
    - `context`: The application context.
- **Returns**: `true` if the camera permission is granted; otherwise, `false`.

### 11. **`requestCameraPermission(Activity activity, int requestCode)`**
- **Description**: Requests camera permission.
- **Parameters**:
    - `activity`: The activity from which the request is made.
    - `requestCode`: The request code used to identify the request.
- **Returns**: None.

### 12. **`hasMicrophonePermission(Context context)`**
- **Description**: Checks if the microphone permission is granted.
- **Parameters**:
    - `context`: The application context.
- **Returns**: `true` if the microphone permission is granted; otherwise, `false`.

### 13. **`requestMicrophonePermission(Activity activity, int requestCode)`**
- **Description**: Requests microphone permission.
- **Parameters**:
    - `activity`: The activity from which the request is made.
    - `requestCode`: The request code used to identify the request.
- **Returns**: None.

### 14. **`hasLocationPermission(Context context)`**
- **Description**: Checks if both fine and coarse location permissions are granted.
- **Parameters**:
    - `context`: The application context.
- **Returns**: `true` if both fine and coarse location permissions are granted; otherwise, `false`.

### 15. **`requestLocationPermissions(Activity activity, int requestCode)`**
- **Description**: Requests both fine and coarse location permissions.
- **Parameters**:
    - `activity`: The activity from which the request is made.
    - `requestCode`: The request code used to identify the request.
- **Returns**: None.

### 16. **`hasStoragePermission(Context context, String permission)`**
- **Description**: Checks if a specific storage permission (either `READ_MEDIA_*` or `WRITE_EXTERNAL_STORAGE`) is granted.
- **Parameters**:
    - `context`: The application context.
    - `permission`: The storage permission string to check.
- **Returns**: `true` if the storage permission is granted; otherwise, `false`.

### 17. **`requestStoragePermission(Activity activity, String permission, int requestCode)`**
- **Description**: Requests a specific storage permission (either `READ_MEDIA_*` or `WRITE_EXTERNAL_STORAGE`), handling API differences for Android versions.
- **Parameters**:
    - `activity`: The activity from which the request is made.
    - `permission`: The storage permission string to request.
    - `requestCode`: The request code used to identify the request.
- **Returns**: None.

### 18. **`hasSaveImagePermission(Context context)`**
- **Description**: Checks if the permission to save images is granted.
- **Parameters**:
    - `context`: The application context.
- **Returns**: `true` if the permission to save images is granted; otherwise, `false`.

### 19. **`requestSaveImagePermission(Activity activity, int requestCode)`**
- **Description**: Requests permission to save images.
- **Parameters**:
    - `activity`: The activity from which the request is made.
    - `requestCode`: The request code used to identify the request.
- **Returns**: None.

### 20. **`hasSaveAudioPermission(Context context)`**
- **Description**: Checks if the permission to save audio is granted.
- **Parameters**:
    - `context`: The application context.
- **Returns**: `true` if the permission to save audio is granted; otherwise, `false`.

### 21. **`requestSaveAudioPermission(Activity activity, int requestCode)`**
- **Description**: Requests permission to save audio.
- **Parameters**:
    - `activity`: The activity from which the request is made.
    - `requestCode`: The request code used to identify the request.
- **Returns**: None.

### 22. **`handlePermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults, PermissionResultCallback callback)`**
- **Description**: Handles the result of permission requests. If any permissions are denied, the callback will be invoked with the denied permissions.
- **Parameters**:
    - `activity`: The activity from which the request was made.
    - `requestCode`: The request code identifying the request.
    - `permissions`: An array of permissions that were requested.
    - `grantResults`: An array of corresponding results (granted or denied).
    - `callback`: The callback interface to handle the result.
- **Returns**: None.
