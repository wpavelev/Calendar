package de.wpavelev.calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int REQUEST_CODE_PERMISSION_CALENDAR_READ = 0;
    private static final int REQUEST_CODE_PERMISSION_CALENDAR_WRITE = 1;

    private static final String[] DIALOG_TITLES = {
            "Read",
            "Write"
    };

    private static final String[] DIALOG_MSG = {
            "We need some read permissions.",
            "We need some write permission."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);


        checkPermissions();

    }

    private boolean checkPermissions() {
        Log.d(TAG, "checkPermissions() called");

        if (checkCalendarWrite() && checkCalendarRead()) {
            Intent intent = new Intent(this, MasterActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    private boolean checkCalendarRead() {
        Log.d(TAG, "checkCalendarRead() called");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) ==
                PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkCalendarRead: granted!");
            checkCalendarWrite();
            return true;
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_CALENDAR},
                    REQUEST_CODE_PERMISSION_CALENDAR_READ);
        }
        return false;
    }

    private boolean checkCalendarWrite() {
        Log.d(TAG, "checkCalendarWrite() called");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) ==
                PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkCalendarWrite: granted!");
            return true;
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR},
                    REQUEST_CODE_PERMISSION_CALENDAR_WRITE);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult() called with: requestCode = [" + requestCode + "], permissions = [" + permissions + "], grantResults = [" + grantResults + "]");
        if (grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //Permission granted
            //Check if other permissions are outstanding
            checkPermissions();
        } else {
            //Need Permission -> show user some information and try again
            if (requestCode >= DIALOG_TITLES.length) {
                Log.e(TAG, "onRequestPermissionsResult: REQUEST CODE IS FAULT");
                return;
            }
            showPermissionDialog(DIALOG_TITLES[requestCode],
                    DIALOG_MSG[requestCode],
                    requestCode);
        }


       /* switch (requestCode) {
            case REQUEST_CODE_PERMISSION_CALENDAR_READ:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.

                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.

                    showPermissionDialog("Read Calendar",
                            "We Want to Read your calendar",
                            REQUEST_CODE_PERMISSION_CALENDAR_READ);

                }
                return;

            case 1:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.

                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.

                }
                return;
                // Other 'case' lines to check for other
                // permissions this app might request.
        }*/
    }


    private void showPermissionDialog(String title, String msg, int permission) {
        Log.d(TAG, "showPermissionDialog() called with: title = [" + title + "], msg = [" + msg + "], permission = [" + permission + "]");
        AlertDialog.Builder permissionInfo = new AlertDialog.Builder(this);
        permissionInfo
                .setMessage(msg)
                .setTitle(title)
                .setPositiveButton("Try Again", (dialog, which) -> {
                    if (permission == REQUEST_CODE_PERMISSION_CALENDAR_READ) {
                        checkCalendarRead();
                    } else if (permission == REQUEST_CODE_PERMISSION_CALENDAR_WRITE) {
                        checkCalendarWrite();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.cancel();
                });

        permissionInfo.create();

    }
}