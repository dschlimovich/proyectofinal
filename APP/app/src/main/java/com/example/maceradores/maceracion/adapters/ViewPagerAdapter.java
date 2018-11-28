package com.example.maceradores.maceracion.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.maceradores.maceracion.Fragments.LapFragment;
import com.example.maceradores.maceracion.Fragments.MeasureFragment;

public class ViewPagerAdapter  extends FragmentPagerAdapter {
    private int numberOfTabs;

    public ViewPagerAdapter(FragmentManager fm, Context context, int numberOfTabs) {
        super(fm);
        this.numberOfTabs=numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MeasureFragment();
            case 1:
                return new LapFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
