package de.wpavelev.calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    private boolean checkCalendarRead() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) ==
                PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkCalendarRead: granted!");
            checkCalendarWrite();
            return true;
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_CALENDAR}, 0);
        }
        return false;
    }

    private boolean checkCalendarWrite() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) ==
                PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkCalendarWrite: granted!");
            return true;
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR}, 1);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0:
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
                    return;

                }
                // Other 'case' lines to check for other
                // permissions this app might request.
        }
    }
}