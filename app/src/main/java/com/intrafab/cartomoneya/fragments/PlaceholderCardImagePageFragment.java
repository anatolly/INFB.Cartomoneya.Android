package com.intrafab.cartomoneya.fragments;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.intrafab.cartomoneya.CropActivity;
import com.intrafab.cartomoneya.NewBusinessCardActivity;
import com.intrafab.cartomoneya.NewCardActivity;
import com.intrafab.cartomoneya.R;
import com.intrafab.cartomoneya.utils.Logger;
import com.intrafab.cartomoneya.utils.SupportVersion;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artemiy Terekhov on 15.05.2015.
 */
public class PlaceholderCardImagePageFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = PlaceholderCardImagePageFragment.class.getName();

    public static final String ARG_SAVE_IMAGE = "arg_save_image";
    public static final String ARG_SAVE_IS_FRONT = "arg_save_is_front";

    public static final int REQUEST_CODE_PICK_IMAGE = 200;

    private ImageView mCardImageView;
    private ImageView mCardAddImageView;
    private RelativeLayout mLayoutCardFrame;

    public boolean isFront() {
        return mIsFront;
    }

    private boolean mIsFront;

    public Uri getUri() {
        return mUri;
    }

    private Uri mUri;

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//
//        Logger.e(TAG, "PlaceholderCardImagePageFragment onAttach");
//
//        try {
//            mListener = (onClickListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString() + " must implement PlaceholderCardImagePageFragment onClickListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//
//        mListener = null;
//        Logger.e(TAG, "PlaceholderCardImagePageFragment onDetach");
//    }

    public static PlaceholderCardImagePageFragment newInstance(boolean isFront) {
        Logger.e(TAG, "PlaceholderCardImagePageFragment newInstance");
        PlaceholderCardImagePageFragment fragment = new PlaceholderCardImagePageFragment();

        Bundle args = new Bundle();
        args.putBoolean("isFront", isFront);
        fragment.setArguments(args);
        Logger.e(TAG, "PlaceholderCardImagePageFragment newInstance isFront: " + isFront);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.e(TAG, "PlaceholderCardImagePageFragment onCreate");

        Bundle args = getArguments();
        if (args != null) {
            mIsFront = args.getBoolean("isFront");
            Logger.e(TAG, "PlaceholderCardImagePageFragment onCreate mIsFront: " + mIsFront);
        }

        setRetainInstance(true);
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
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Logger.e(TAG, "PlaceholderCardImagePageFragment onActivityCreated");

        if (savedInstanceState != null) {
            String pathImage = savedInstanceState.getString(ARG_SAVE_IMAGE);
            if (!TextUtils.isEmpty(pathImage)) {
                mUri = Uri.parse(pathImage);
                Logger.e(TAG, "PlaceholderCardImagePageFragment onCreate savedInstanceState path: " + pathImage);
            }

            mIsFront = savedInstanceState.getBoolean(ARG_SAVE_IS_FRONT);
        }

        update();
    }

    @Override
    public void onStart() {
        super.onStart();
        Logger.e(TAG, "PlaceholderCardImagePageFragment onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.e(TAG, "PlaceholderCardImagePageFragment onResume");
        update();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivCardImage:
            case R.id.ivIconAddImage:
                startActivityForResult(getPickImageChooserIntent(mIsFront), REQUEST_CODE_PICK_IMAGE);
                break;
        }
    }

    @SuppressWarnings("NewApi")
    public void update() {
        Logger.e(TAG, "PlaceholderCardImagePageFragment update");
        if (mUri == null)
            return;

        mCardImageView.post(new Runnable() {
            @Override
            public void run() {
                Logger.e(TAG, "PlaceholderCardImagePageFragment update start");
                try {
                    String realPath = mUri.getPath();

                    if (new File(realPath).exists()) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        Bitmap bitmap = BitmapFactory.decodeFile(realPath, options);

//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mUri);
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @SuppressWarnings("NewApi")
    public void setUri(Uri imageUri, boolean needUpdate) {
        if (imageUri == null)
            return;
        Logger.e(TAG, "PlaceholderCardImagePageFragment setUri imageUri: " + imageUri.getPath());

        mUri = imageUri;
//        if (needUpdate) {
//            update();
//        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Logger.e(TAG, "PlaceholderCardImagePageFragment onSaveInstanceState");

        if (mUri != null && outState != null) {
            outState.putString(ARG_SAVE_IMAGE, mUri.getPath());
        }

        if (outState != null) {
            outState.putBoolean(ARG_SAVE_IS_FRONT, mIsFront);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.e(TAG, "PlaceholderCardImagePageFragment onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Logger.e(TAG, "PlaceholderCardImagePageFragment onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logger.e(TAG, "PlaceholderCardImagePageFragment onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.e(TAG, "PlaceholderCardImagePageFragment onDestroy");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.e(TAG, "onActivityResult requestCode: " + requestCode);
        Logger.e(TAG, "onActivityResult resultCode: " + resultCode);

        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            Logger.e(TAG, "onActivityResult requestCode REQUEST_CODE_PICK_IMAGE");
            if (resultCode == Activity.RESULT_OK) {
                Logger.e(TAG, "onActivityResult resultCode RESULT_OK");
                Uri imageUri = getPickImageResultUri(data, mIsFront);
                FragmentActivity mActivity =  getActivity();

                // TODO FIX IT!!!! IT Should not be implemented this way !!!!!
                if (mActivity instanceof NewBusinessCardActivity ) {
                    CropActivity.launch((NewBusinessCardActivity) getActivity(), NewBusinessCardActivity.REQUEST_CODE_CROP_IMAGE, imageUri, mIsFront);
                }
                else if (mActivity instanceof NewCardActivity) {
                    CropActivity.launch((NewCardActivity) getActivity(), NewCardActivity.REQUEST_CODE_CROP_IMAGE, imageUri, mIsFront);
                }
                else
                {
                    Logger.e(TAG, "We  should never get here!!!");
                }
            }
        }
    }

    /**
     * Create a chooser intent to select the source to get image from.<br/>
     * The source can be camera's (ACTION_IMAGE_CAPTURE) or gallery's (ACTION_GET_CONTENT).<br/>
     * All possible sources are added to the intent chooser.
     */
    public Intent getPickImageChooserIntent(boolean isFront) {

        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri(isFront);

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getActivity().getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // the main intent is the last in the list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    /**
     * Get URI to image received from capture by camera.
     */
    private Uri getCaptureImageOutputUri(boolean isFront) {
        Logger.e(TAG, "getCaptureImageOutputUri start");
        Uri outputFileUri = null;
        File getImage = getActivity().getExternalCacheDir();

        if (getImage != null) {
            if (!getImage.exists()) {
                getImage.mkdirs();
            }
            File imageFile = new File(getImage.getPath(), isFront ? "pickFrontImageResult.jpeg" : "pickBackImageResult.jpeg");
            if (!imageFile.exists()) {
                try {
                    imageFile.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Logger.e(TAG, "getCaptureImageOutputUri imageFile: " + imageFile.getPath());
            outputFileUri = Uri.fromFile(imageFile);
            Logger.e(TAG, "getCaptureImageOutputUri outputFileUri: " + outputFileUri.getPath());
        }
        return outputFileUri;
    }

    public Uri getPickImageResultUri(Intent data, boolean isFront) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? getCaptureImageOutputUri(isFront) : data.getData();
    }
}
