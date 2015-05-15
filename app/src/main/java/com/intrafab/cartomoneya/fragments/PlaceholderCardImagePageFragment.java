package com.intrafab.cartomoneya.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.intrafab.cartomoneya.R;
import com.theartofdev.edmodo.cropper.CropImageView;

/**
 * Created by Artemiy Terekhov on 15.05.2015.
 */
public class PlaceholderCardImagePageFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = PlaceholderCardImagePageFragment.class.getName();

    public interface onClickListener {
        public void onCardImageClick();
    }

    private onClickListener mListener;
    private CropImageView mCardImageView;
    private ImageView mCardAddImageView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (onClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement PlaceholderCardImagePageFragment onClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_card, container, false);

        mCardImageView = (CropImageView) rootView.findViewById(R.id.ivCardImage);
        mCardAddImageView = (ImageView) rootView.findViewById(R.id.ivIconAddImage);

        mCardImageView.setOnClickListener(this);
        mCardAddImageView.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCardAddImageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivCardImage:
            case R.id.ivIconAddImage:
                if (mListener != null)
                    mListener.onCardImageClick();
                break;
        }
    }

    public void setUri(Uri imageUri) {
        mCardImageView.setImageUri(imageUri);
        mCardAddImageView.setVisibility(View.GONE);
    }
}
