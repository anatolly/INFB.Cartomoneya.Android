package com.intrafab.cartomoneya;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;

import com.intrafab.cartomoneya.utils.Logger;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

/**
 * Created by Artemiy Terekhov on 15.05.2015.
 */
public class CropActivity extends BaseActivity {

    public static final String TAG = CropActivity.class.getName();

    public static final String EXTRA_ARG_URI = "extrz_arg_uri";

    private Uri mImageUri;
    private CropImageView mImageView;
    private DiscreteSeekBar mSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().getThemedContext();

        getSupportActionBar().setTitle("Crop Image");
        showActionBar();

        Intent intent = getIntent();
        String path = intent.getStringExtra(EXTRA_ARG_URI);
        mImageUri = Uri.parse(path);
        Logger.e(TAG, "Start CropActivity path: " + mImageUri.getPath());

        mImageView = (CropImageView) findViewById(R.id.ivCropImage);
        mImageView.setImageUri(mImageUri);

        mSeekBar = (DiscreteSeekBar) findViewById(R.id.dSeekbar);
        mSeekBar.setProgress(0);
        mSeekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
//                mImageView.setAspectRatio( value, value);
//                mImageView.setScaleX(value);
//                mImageView.setScaleY(value);

                mImageView.scaleImage(value, value);

//                Matrix mDisplayMatrix = new Matrix();
//                mDisplayMatrix.postScale(value, value);
//                mImageView.setImageM
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_crop;
    }

    public static void launch(BaseActivity activity, Uri imageUri) {
        Intent intent = new Intent(activity, CropActivity.class);
        //intent.putExtra(EXTRA_ARG_URI, imageUri.toString());
        String path = imageUri.toString();
        Logger.e(TAG, "Launch CropActivity path: " + imageUri.getPath());
        intent.putExtra(EXTRA_ARG_URI, path);

        Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle();

        ActivityCompat.startActivity(activity, intent, options);
    }
}