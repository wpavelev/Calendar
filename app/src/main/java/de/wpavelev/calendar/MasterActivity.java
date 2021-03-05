package de.wpavelev.calendar;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.ActionBarContextView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.internal.NavigationMenu;

public class MasterActivity extends FragmentActivity {

    private ViewPager mViewPager;

    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);

        mViewPager = findViewById(R.id.view_pager);
        mPagerAdapter = new SimplePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);



    }




    private class SimplePagerAdapter extends FragmentStatePagerAdapter {


        public SimplePagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return new CalEventFragment();
        }

        @Override
        public int getCount() {
            return 1;
        }
    }
}
