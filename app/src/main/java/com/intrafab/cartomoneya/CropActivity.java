package com.intrafab.cartomoneya;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;

/**
 * Created by Artemiy Terekhov on 15.05.2015.
 */
public class CropActivity extends BaseActivity {

    public static final String TAG = CropActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().getThemedContext();

        getSupportActionBar().setTitle("Crop Image");
        showActionBar();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_crop;
    }

    public static void launch(BaseActivity activity, Uri imageUri) {
        Intent intent = new Intent(activity, OffersActivity.class);

        Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle();

        ActivityCompat.startActivity(activity, intent, options);
    }
}