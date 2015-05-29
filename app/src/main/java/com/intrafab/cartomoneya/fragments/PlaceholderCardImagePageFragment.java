package com.intrafab.cartomoneya.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.intrafab.cartomoneya.R;
import com.intrafab.cartomoneya.utils.Logger;
import com.intrafab.cartomoneya.utils.SupportVersion;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

/**
 * Created by Artemiy Terekhov on 15.05.2015.
 */
public class PlaceholderCardImagePageFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = PlaceholderCardImagePageFragment.class.getName();

    public interface onClickListener {
        public void onCardImageClick();
    }

    private onClickListener mListener;
    private ImageView mCardImageView;
    private ImageView mCardAddImageView;
    private RelativeLayout mLayoutCardFrame;

    public Uri getUri() {
        return mUri;
    }

    private Uri mUri;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Logger.e(TAG, "PlaceholderCardImagePageFragment onAttach");

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
        Logger.e(TAG, "PlaceholderCardImagePageFragment onDetach");
    }

    public static PlaceholderCardImagePageFragment newInstance(Uri imageUri) {
        PlaceholderCardImagePageFragment fragment = new PlaceholderCardImagePageFragment();

        if (imageUri != null) {
            Bundle args = new Bundle();
            args.putString("uri", imageUri.getPath());
            fragment.setArguments(args);
            Logger.e(TAG, "PlaceholderCardImagePageFragment newInstance uri: " + imageUri.getPath());
        }

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.e(TAG, "PlaceholderCardImagePageFragment onCreate");

        Bundle args = getArguments();
        if (args != null) {
            String path = args.getString("uri");
            if (!TextUtils.isEmpty(path)) {
                mUri = Uri.parse(path);
                Logger.e(TAG, "PlaceholderCardImagePageFragment onCreate path: " + path);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_card, container, false);
        Logger.e(TAG, "PlaceholderCardImagePageFragment onCreateView");

        mCardImageView = (ImageView) rootView.findViewById(R.id.ivCardImage);
        mCardAddImageView = (ImageView) rootView.findViewById(R.id.ivIconAddImage);
        mLayoutCardFrame = (RelativeLayout) rootView.findViewById(R.id.layoutCardFrame);

        mCardImageView.setOnClickListener(this);
        mCardAddImageView.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Logger.e(TAG, "PlaceholderCardImagePageFragment onViewCreated");

        mCardAddImageView.setVisibility(View.VISIBLE);
        update();
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

    @SuppressWarnings("NewApi")
    private void update() {
        Logger.e(TAG, "PlaceholderCardImagePageFragment update");
        if (mUri == null)
            return;

        Logger.e(TAG, "PlaceholderCardImagePageFragment update start");
        try {
            if (new File(mUri.getPath()).exists()) {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mUri);
                mCardImageView.setImageBitmap(bitmap);
                if (SupportVersion.JellyBean())
                    mLayoutCardFrame.setBackground(null);
                else
                    mLayoutCardFrame.setBackgroundDrawable(null);
                mCardAddImageView.setVisibility(View.GONE);
            } else {
                Picasso.with(getActivity())
                        .load(mUri)
                        .into(mCardImageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                if (SupportVersion.JellyBean())
                                    mLayoutCardFrame.setBackground(null);
                                else
                                    mLayoutCardFrame.setBackgroundDrawable(null);
                                mCardAddImageView.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                try {
                                    if (SupportVersion.LMR1())
                                        mLayoutCardFrame.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_card_frame, null));
                                    else
                                        mLayoutCardFrame.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_card_frame));
                                    mCardAddImageView.setVisibility(View.VISIBLE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("NewApi")
    public void setUri(Uri imageUri, boolean needUpdate) {
        if (imageUri == null)
            return;
        Logger.e(TAG, "PlaceholderCardImagePageFragment setUri imageUri: " + imageUri.getPath());

        mUri = imageUri;
        if (needUpdate) {
            update();
        }
    }
}
