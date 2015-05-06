package com.intrafab.cartomoneya;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;

/**
 * Created by Artemiy Terekhov on 06.05.2015.
 */
public class BusinessCardsActivity extends BaseActivity {

    public static final String TAG = BusinessCardsActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().getThemedContext();

        getSupportActionBar().setTitle("Business Cards");
        showActionBar();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_business_cards;
    }

    public static void launch(BaseActivity activity) {
        Intent intent = new Intent(activity, BusinessCardsActivity.class);

        Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle();

        ActivityCompat.startActivity(activity, intent, options);
    }
}
