package com.naver.mycnex.viewpageapplication.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.naver.mycnex.viewpageapplication.fragment.ViewP1Fragment;
import com.naver.mycnex.viewpageapplication.fragment.ViewP2Fragment;


public class ViewPagerAdapter extends FragmentStatePagerAdapter{
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new ViewP1Fragment();//ViewP1Fragment.getInstance();
        } else if (position == 1){
            return new ViewP2Fragment();//ViewP2Fragment.getInstance();
        }
        return null;
    }
    @Override
    public int getCount() {
        return 2;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
    }
}
