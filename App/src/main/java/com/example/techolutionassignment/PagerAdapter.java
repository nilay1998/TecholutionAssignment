package com.example.techolutionassignment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    public PagerAdapter(FragmentManager fm){
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        if(position==0)
        {
            Tab1 tab1=new Tab1();
            return tab1;
        }
        else if(position==1)
        {
            Tab2 tab2=new Tab2();
            return tab2;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0)
            return "To Do";
        return "Settings";
    }
}
