package com.example.kousuke.stamps;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * Created by kousuke nezu on 2017/11/19.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return MapFragment.newInstance();
            case 1:
                return StampListFragment.newInstance();
            case 2:
                return ConfigFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "MAP";
            case 1: return "STAMP";
            case 2: return "CONFIG";
            default: return "";
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
