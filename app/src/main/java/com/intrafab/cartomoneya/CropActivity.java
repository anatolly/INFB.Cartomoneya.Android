package com.intrafab.cartomoneya;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.intrafab.cartomoneya.utils.Logger;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Artemiy Terekhov on 15.05.2015.
 */
public class CropActivity extends BaseActivity {

    public static final String TAG = CropActivity.class.getName();

    public static final String EXTRA_ARG_URI = "extra_arg_uri";
    public static final String EXTRA_ARG_IS_FRONT = "extra_arg_is_front";

    private Uri mImageUri;
    private boolean mIsFront;
    private CropImageView mImageView;

    private ProgressWheel mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().getThemedContext();

        getSupportActionBar().setTitle("");
        showTransparentActionBar();

        Intent intent = getIntent();
        String path = intent.getStringExtra(EXTRA_ARG_URI);
        mImageUri = Uri.parse(path);
        mIsFront = intent.getBooleanExtra(EXTRA_ARG_IS_FRONT, true);
        Logger.e(TAG, "Start CropActivity path: " + mImageUri.getPath());

//        int angle = Images.getExifOrientation(mImageUri);
//        if (angle == 0 || angle == 180)
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
//        else if (angle == 90 || angle == -90)
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

        mImageView = (CropImageView) findViewById(R.id.ivCropImage);
        mProgress = (ProgressWheel) findViewById(R.id.progress_wheel);

        mImageView.post(new Runnable() {
            @Override
            public void run() {
                mImageView.setImageUri(mImageUri);
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_crop, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_crop) {
            new AsyncTask<Void, Void, Uri>() {

                @Override
                protected Uri doInBackground(Void... params) {
                    return saveCropBitmap(mImageView.getCroppedImage());
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    showProgress();
                }

                @Override
                protected void onPostExecute(Uri uri) {
                    super.onPostExecute(uri);
                    if (uri != null) {
                        Logger.e(TAG, "CropActivity save path: " + uri.getPath());
                        hideProgress();
                        Intent intent = new Intent();
                        String path = uri.toString();
                        intent.putExtra(EXTRA_ARG_URI, path);
                        setResult(RESULT_OK, intent);
                    } else {
                        setResult(RESULT_CANCELED);
                    }
                    finish();
                }
            }.execute();
            return true;
        } else if (id == R.id.action_cancel) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_crop;
    }

    public void showProgress() {
        mProgress.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        mProgress.setVisibility(View.GONE);
    }

    public static void launch(BaseActivity activity, int requestCode, Uri imageUri, boolean isFront) {
        if (imageUri == null)
            return;
        Intent intent = new Intent(activity, CropActivity.class);
        String path = imageUri.toString();
        Logger.e(TAG, "Launch CropActivity path: " + imageUri.getPath());
        intent.putExtra(EXTRA_ARG_URI, path);
        intent.putExtra(EXTRA_ARG_IS_FRONT, isFront);

        Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle();

        ActivityCompat.startActivityForResult(activity, intent, requestCode, options);
    }

    private Uri saveCropBitmap(Bitmap bitmap) {
        if (bitmap == null)
            return null;

        Uri result = null;
        FileOutputStream out = null;
        try {
            result = getCaptureImageOutputUri(mIsFront);
            out = new FileOutputStream(result.getPath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private Uri getCaptureImageOutputUri(boolean isFront) {
        Uri outputFileUri = null;
        String fileName = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            if (!getImage.exists()) {
                getImage.mkdirs();
            }
            getImage.setWritable(true);
            getImage.setReadable(true);
            String url =  mImageUri.getPath();
            fileName = "" + url.substring( url.lastIndexOf('/')+1, url.length() );
            fileName = isFront ? "pickFrontImg" +  fileName + ".jpeg": "pickBackImg" + fileName + ".jpeg";
                    File newFile = new File(getImage.getPath(), fileName);
            newFile.setWritable(true);
            newFile.setReadable(true);
            outputFileUri = Uri.fromFile(newFile);
        }
        return outputFileUri;
    }
}