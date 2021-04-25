package team.whatever.sms_forwarder_app;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

public class PermissionHandler {
    private Activity activity;

    private final int PHONE_REQUEST_CODE = 17;

    public static final String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String PROCESS_OUTGOING_CALLS = Manifest.permission.PROCESS_OUTGOING_CALLS;
    public static final String READ_SMS = Manifest.permission.READ_SMS;
    public static final String SEND_SMS = Manifest.permission.SEND_SMS;
    public static final String RECEIVE_SMS = Manifest.permission.RECEIVE_SMS;

    public PermissionHandler(Activity activity) {
        this.activity = activity;
    }

    @RequiresApi(Build.VERSION_CODES.M)
    public void checkOrRequestPermission(String[] permissions) {
        if (!hasRequiredPermissions(permissions)) {
            activity.requestPermissions(permissions, PHONE_REQUEST_CODE);
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    public boolean hasRequiredPermissions(String[] permissions) {
        boolean hasPermissions = true;
        for (int i = 0; i < permissions.length; i++) {
            hasPermissions &= checkPermission(permissions[i]);
        }
        return hasPermissions;
    }

    @RequiresApi(Build.VERSION_CODES.M)
    public boolean checkPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                ContextCompat.checkSelfPermission(activity, permission) ==
                        PackageManager.PERMISSION_GRANTED;
    }
}
