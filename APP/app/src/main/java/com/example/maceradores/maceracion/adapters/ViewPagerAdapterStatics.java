package com.example.maceradores.maceracion.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.maceradores.maceracion.Fragments.GraficGeneralFragment;
import com.example.maceradores.maceracion.Fragments.GraficExperimentsFragment;

public class ViewPagerAdapterStatics extends FragmentPagerAdapter {
    private int numberOfTabs;
    private Fragment GeneralFragment = null;
    private Fragment ExperimentsFragment = null;


    public ViewPagerAdapterStatics(FragmentManager fm, Context context, int numberOfTabs) {
        super(fm);
        this.numberOfTabs=numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
//        Bundle bundle = new Bundle();
//        bundle.putInt("idMash",idMash);
//        bundle.putInt("idExp", idExperiment);
        switch (position) {
            case 0:
                if(GeneralFragment == null){ // Si el fragment no esta seteado
                    GeneralFragment = new GraficGeneralFragment();
                    //measureFragment.setArguments(bundle);
                }
                return GeneralFragment;
            case 1:
                if(ExperimentsFragment == null){
                    ExperimentsFragment = new GraficExperimentsFragment();
                    //stageFragment.setArguments(bundle);
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
