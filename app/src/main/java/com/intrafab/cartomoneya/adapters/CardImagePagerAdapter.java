package com.intrafab.cartomoneya.adapters;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.intrafab.cartomoneya.fragments.PlaceholderCardImagePageFragment;
import com.intrafab.cartomoneya.utils.Logger;

/**
 * Created by Artemiy Terekhov on 15.05.2015.
 */
public class CardImagePagerAdapter extends FragmentStatePagerAdapter {
    public static final String TAG = CardImagePagerAdapter.class.getName();

    private SparseArray<PlaceholderCardImagePageFragment> mRegisteredFragments = new SparseArray<PlaceholderCardImagePageFragment>();

    private Uri mFrontImageUri;
    private Uri mBackImageUri;

    public void setFrontUri(Uri imageUri) {
        mFrontImageUri = imageUri;
    }

    public void setBackUri(Uri imageUri) {
        mBackImageUri = imageUri;
    }

    public void update(int position) {
        int count = mRegisteredFragments.size();
        for (int i = 0; i < count; ++i) {
            PlaceholderCardImagePageFragment fragment = mRegisteredFragments.get(i);
            fragment.setUri(mFrontImageUri, position == i);
        }
    }

    public CardImagePagerAdapter(FragmentManager fm, Uri frontImageUri, Uri backImageUri) {
        super(fm);
        mFrontImageUri = frontImageUri;
        mBackImageUri = backImageUri;
    }
    public Fragment getItem(int number) {
        if (number == 0) {
            return PlaceholderCardImagePageFragment.newInstance(mFrontImageUri);
        } else {
            return PlaceholderCardImagePageFragment.newInstance(mBackImageUri);
        }
    }
    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PlaceholderCardImagePageFragment fragment = (PlaceholderCardImagePageFragment) super.instantiateItem(container, position);
        fragment.setUri(position == 0 ? mFrontImageUri : mBackImageUri, false);
        mRegisteredFragments.put(position, fragment);
        Logger.e(TAG, "CardImagePagerAdapter instantiateItem position: " + position);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        mRegisteredFragments.remove(position);
        Logger.e(TAG, "CardImagePagerAdapter destroyItem position: " + position);
        super.destroyItem(container, position, object);
    }

    public PlaceholderCardImagePageFragment getFragment(int fragmentPosition) {
        Logger.e(TAG, "CardImagePagerAdapter getFragment position: " + fragmentPosition);
        Logger.e(TAG, "CardImagePagerAdapter getFragment size: " + mRegisteredFragments.size());
        return (PlaceholderCardImagePageFragment) mRegisteredFragments.get(fragmentPosition);
    }

}
