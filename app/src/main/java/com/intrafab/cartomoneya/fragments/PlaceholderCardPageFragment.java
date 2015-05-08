package com.intrafab.cartomoneya.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.intrafab.cartomoneya.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Artemiy Terekhov on 08.05.2015.
 */
public class PlaceholderCardPageFragment extends Fragment {

    public static final String ARG_PAGE = "arg_page";
    public static final String ARG_RES_URI = "arg_res_iri";

    private int mCurrentPageNumber;
    private String mCurrentResUri;

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

        final ImageView image = (ImageView) rootView.findViewById(R.id.ivCardImage);
        if (!TextUtils.isEmpty(mCurrentResUri)) {
            Picasso.with(getActivity())
                    .load(mCurrentResUri)
                    .placeholder(R.mipmap.ic_default_card)
                    .error(R.mipmap.ic_default_card)
                    .into(image);
        } else {
            image.setImageResource(R.mipmap.ic_default_card);
        }

        return rootView;
    }

    public int getPageNumber() {
        return mCurrentPageNumber;
    }

}
