package com.intrafab.cartomoneya;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;

/**
 * Created by Artemiy Terekhov on 07.05.2015.
 */
public class NewCardActivity extends BaseActivity {

    public static final String TAG = NewCardActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().getThemedContext();

        getSupportActionBar().setTitle("New Card");
        showActionBar();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_new_card;
    }

    public static void launch(BaseActivity activity) {
        Intent intent = new Intent(activity, NewCardActivity.class);

        Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle();

        ActivityCompat.startActivity(activity, intent, options);
    }
}
