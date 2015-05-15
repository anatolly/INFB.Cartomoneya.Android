package com.intrafab.cartomoneya.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.intrafab.cartomoneya.fragments.PlaceholderCardImagePageFragment;

/**
 * Created by Artemiy Terekhov on 15.05.2015.
 */
public class CardImagePagerAdapter extends FragmentStatePagerAdapter {
    public static final String TAG = CardImagePagerAdapter.class.getName();

    private SparseArray<Fragment> mRegisteredFragments = new SparseArray<Fragment>();

    public CardImagePagerAdapter(FragmentManager fm) {
        super(fm);
    }
    public Fragment getItem(int number) {
        if (number == 0) {
            return new PlaceholderCardImagePageFragment();
        } else {
            return new PlaceholderCardImagePageFragment();
        }
    }
    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        mRegisteredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        mRegisteredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public PlaceholderCardImagePageFragment getFragment(int fragmentPosition) {

        return (PlaceholderCardImagePageFragment) mRegisteredFragments.get(fragmentPosition);
    }

}
