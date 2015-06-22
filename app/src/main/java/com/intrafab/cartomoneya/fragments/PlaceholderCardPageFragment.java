package com.intrafab.cartomoneya.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.intrafab.cartomoneya.R;
import com.intrafab.cartomoneya.utils.Logger;
import com.intrafab.cartomoneya.utils.RoundedTransformation;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by Artemiy Terekhov on 08.05.2015.
 */
public class PlaceholderCardPageFragment extends Fragment {
    public static final String TAG = PlaceholderCardPageFragment.class.getName();

    public static final String ARG_PAGE = "arg_page";
    public static final String ARG_RES_URI = "arg_res_iri";

    private int mCurrentPageNumber;
    private String mCurrentResUri;

    private ImageView mCardImageView;

    public PlaceholderCardPageFragment() {
    }

    public static PlaceholderCardPageFragment create(int pageNumber, String resUri) {
        PlaceholderCardPageFragment fragment = new PlaceholderCardPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        args.putString(ARG_RES_URI, resUri);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentPageNumber = getArguments().getInt(ARG_PAGE);
        mCurrentResUri = getArguments().getString(ARG_RES_URI);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_card_page, container, false);

        mCardImageView = (ImageView) rootView.findViewById(R.id.ivCardImage);
//        if (!TextUtils.isEmpty(mCurrentResUri)) {
//            Picasso.with(getActivity())
//                    .load(mCurrentResUri)
//                    .placeholder(R.mipmap.ic_default_card)
//                    .error(R.mipmap.ic_default_card)
//                    .into(mCardImageView);
//        } else {
//            mCardImageView.setImageResource(R.mipmap.ic_default_card);
//        }

        return rootView;
    }

    public int getPageNumber() {
        return mCurrentPageNumber;
    }

    public void setUri(String imageUri) {
        try {
            mCurrentResUri = imageUri;
            Logger.e(TAG, "setUri: " + mCurrentResUri);
            if (!TextUtils.isEmpty(mCurrentResUri)) {
                Logger.e(TAG, "setUri Picasso load: " + mCurrentResUri);
                mCardImageView.setImageDrawable(null);
                Picasso.with(getActivity())
                        .load(mCurrentResUri)
                        .transform(new RoundedTransformation(6, 0))
                        .placeholder(R.mipmap.ic_default_card)
                        .error(R.mipmap.ic_default_card)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(mCardImageView);


            } else {
                Logger.e(TAG, "setUri set: ic_default_card");
                Picasso.with(getActivity())
                        .load(R.mipmap.ic_default_card)
                        .transform(new RoundedTransformation(6, 0))
                        .into(mCardImageView);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
