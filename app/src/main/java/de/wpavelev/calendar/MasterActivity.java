package de.wpavelev.calendar;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MasterActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MasterActivity";

    MainViewModel mViewModel;

    private DrawerLayout mDrawerLayout;


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
    private ViewPager mViewPager;

    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);

        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);



        mViewPager = findViewById(R.id.view_pager);
        mPagerAdapter = new SimplePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);


        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawer);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.navigation_drawer_close);

        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nvView);
        navigationView.setNavigationItemSelectedListener(this);

        if (checkPermissions()) { //hier nur weitermachen, wenn Kalender erlaubt wurde
            readPreferences();


        }




    }

    @Override
    protected void onPause() {
        super.onPause();
        savePreferences();

    }



    private void savePreferences() {
        Log.d(TAG, "savePreferences() called");
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Set<String> visibleCalendars = new HashSet<>();
        for (Integer integer : mViewModel.getVisibleCalendars().getValue()) {
            visibleCalendars.add(String.valueOf(integer));
        }
        editor.putStringSet(getString(R.string.SHARE_PREF_KEY_VISIBLE_CALENDARS), visibleCalendars);
        editor.apply();
    }

    private void readPreferences() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        CalendarUtil util = new CalendarUtil(this);

        Set<String> visibleCalendarDefaults = new HashSet<>();
        for (CalendarUtil.CalInfo calInfo : util.getCalendarList()) {
            visibleCalendarDefaults.add(String.valueOf(calInfo.getId()));
        }

        Set<String> visibleCalendars = sharedPref.getStringSet(
                getString(R.string.SHARE_PREF_KEY_VISIBLE_CALENDARS), visibleCalendarDefaults);

        Set<Integer> visibleCalendarsInt = new HashSet<>();

        for (String visibleCalendar : visibleCalendars) {
            visibleCalendarsInt.add(Integer.parseInt(visibleCalendar));
        }

        mViewModel.setVisibleCalendars(visibleCalendarsInt);
    }

    private void debugEventlist(List<CalendarUtil.EventInfo> list) {
        Log.d(TAG, "debugEventlist() called with: list = [" + list.size() + "]");
        for (CalendarUtil.EventInfo eventInfo : list) {
            Log.d(TAG, "debugEventlist: " + eventInfo.getTitle() + " Cal: " + eventInfo.getCalId());
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }

    }


    private boolean checkPermissions() {
        Log.d(TAG, "checkPermissions() called");

        if (checkCalendarWrite() && checkCalendarRead()) {
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onNavigationItemSelected() called with: item = [" + item + "]");
        switch (item.getItemId()) {
            case R.id.nav_item_calendar:
                Log.d(TAG, "onNavigationItemSelected: " + item.getItemId());
                mViewPager.setCurrentItem(SimplePagerAdapter.CAL_LIST);

                break;
            default:
                Log.d(TAG, "onNavigationItemSelected: default");
                mViewPager.setCurrentItem(SimplePagerAdapter.EVENT_LIST);

        }


        mDrawerLayout.close();
        return true;
    }

    private class SimplePagerAdapter extends FragmentStatePagerAdapter {

        public static final int EVENT_LIST = 0;
        public static final int CAL_LIST = 1;


        public SimplePagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "getItem() called with: position = [" + position + "]");
            Fragment fragment;
            switch (position) {
                case CAL_LIST:
                    fragment = CalendarListFragment.newInstance();
                    break;
                default:
                    fragment = EventListFragment.newInstance();
                    break;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
