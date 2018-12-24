package com.example.maceradores.maceracion.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.maceradores.maceracion.Fragments.ChartGeneralFragment;
import com.example.maceradores.maceracion.Fragments.ChartExperimentsFragment;

public class ViewPagerAdapterStatistics extends FragmentPagerAdapter {
    private int numberOfTabs;
    private Fragment GeneralFragment = null;
    private Fragment ExperimentsFragment = null;
    private int idMash;


    public ViewPagerAdapterStatistics(FragmentManager fm, Context context, int numberOfTabs, int idMash) {
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
                if(GeneralFragment == null){ // Si el fragment no esta seteado
                    GeneralFragment = new ChartGeneralFragment();
                    GeneralFragment.setArguments(bundle);
                }
                return GeneralFragment;
            case 1:
                if(ExperimentsFragment == null){
                    ExperimentsFragment = new ChartExperimentsFragment();
                    ExperimentsFragment.setArguments(bundle);
                }
                return ExperimentsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
//    public UUID getMeasurId(){
//
//
//
//    }
}
