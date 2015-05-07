package com.intrafab.cartomoneya;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;

import com.intrafab.cartomoneya.data.ShopCard;
import com.telly.groundy.CallbacksManager;

/**
 * Created by Artemiy Terekhov on 07.05.2015.
 */
public class ShopCardDetailActivity extends BaseActivity {

    public static final String TAG = ShopCardDetailActivity.class.getName();

    public static final String EXTRA_PARAM_SHOP_CARD = "param_shop_card";

    private ShopCard mShopCard;

    private CallbacksManager mCallbacksManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().getThemedContext();

        Intent intent = getIntent();
        if (intent != null) {
            mShopCard = getIntent().getParcelableExtra(EXTRA_PARAM_SHOP_CARD);
        }

        getSupportActionBar().setTitle(mShopCard.getName());
        showActionBar();

        mCallbacksManager = CallbacksManager.init(savedInstanceState);
        mCallbacksManager.linkCallbacks(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mCallbacksManager.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCallbacksManager.onDestroy();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_shopping_card_detail;
    }

    public static void launch(BaseActivity activity, ShopCard item) {
        Intent intent = new Intent(activity, ShopCardDetailActivity.class);
        intent.putExtra(EXTRA_PARAM_SHOP_CARD, item);

        Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle();

        ActivityCompat.startActivity(activity, intent, options);
    }
}
