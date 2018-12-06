package com.example.maceradores.maceracion.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.maceradores.maceracion.Fragments.StageFragment;
import com.example.maceradores.maceracion.Fragments.MeasureFragment;

public class ViewPagerAdapter  extends FragmentPagerAdapter {
    private int numberOfTabs;
    private int idMash;
    private Fragment fragment = null;

    public ViewPagerAdapter(FragmentManager fm, Context context, int numberOfTabs, int idMash) {
        super(fm);
        this.numberOfTabs=numberOfTabs;
        this.idMash=idMash;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("idMash",idMash);
        switch (position) {
            case 0:
                fragment = new MeasureFragment();
                fragment.setArguments(bundle);
                return fragment;
            case 1:
                return new StageFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
