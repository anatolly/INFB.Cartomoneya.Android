package com.intrafab.cartomoneya.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Artemiy Terekhov on 08.05.2015.
 */
public class CardPageAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mFragments;
    private FragmentManager mFragmentManager;

    public CardPageAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);

        mFragments = new ArrayList<Fragment>();
        mFragmentManager = fragmentManager;
    }

    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    public void add(Fragment fragment) {
        mFragments.add(fragment);
    }
}
