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
    private int idExperiment;
    private Fragment measureFragment = null;
    private Fragment stageFragment = null;


    public ViewPagerAdapter(FragmentManager fm, Context context, int numberOfTabs, int idMash, int idExp) {
        super(fm);
        this.numberOfTabs=numberOfTabs;
        this.idMash=idMash;
        this.idExperiment = idExp;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("idMash",idMash);
        bundle.putInt("idExp", idExperiment);
        switch (position) {
            case 0:
                if(measureFragment == null){ // Si el fragment no esta seteado
                    measureFragment = new MeasureFragment();
                    measureFragment.setArguments(bundle);
                }
                return measureFragment;
            case 1:
                if(stageFragment == null){
                    stageFragment = new StageFragment();
                    stageFragment.setArguments(bundle);
                }
                return stageFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
