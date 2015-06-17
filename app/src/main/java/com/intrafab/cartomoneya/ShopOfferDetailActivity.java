package com.intrafab.cartomoneya;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.intrafab.cartomoneya.data.ShopOffer;
import com.squareup.picasso.Picasso;

/**
 * Created by Vasily Laushkin <vaslinux@gmail.com> on 10/06/15.
 */
public class ShopOfferDetailActivity extends BaseActivity {
    public static final String TAG = ShopOfferDetailActivity.class.getName();
    public static final String EXTRA_PARAM_SHOP_OFFER = "param_biz_card";

    private ShopOffer mShopOffer;
    private ImageView mIvShopOfferMainImage;
    private TextView mTvShopOfferText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().getThemedContext();

        Intent intent = getIntent();
        if (intent != null) {
            mShopOffer = getIntent().getParcelableExtra(EXTRA_PARAM_SHOP_OFFER);
        }

        getSupportActionBar().setTitle(mShopOffer.getName());
        showActionBar();

        mIvShopOfferMainImage = (ImageView) findViewById(R.id.ivShopOfferMainImage);
        mTvShopOfferText = (TextView) findViewById(R.id.tvShopOfferText);

        mTvShopOfferText.post(new Runnable() {
            @Override
            public void run() {
                fillData();
            }
        });
    }

    private void fillData() {
        if (!TextUtils.isEmpty(mShopOffer.getText())) {
            mTvShopOfferText.setVisibility(View.VISIBLE);
            mTvShopOfferText.setText(mShopOffer.getText());
        } else {
            mTvShopOfferText.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mShopOffer.getMainImagePath())) {
            if (!TextUtils.isEmpty(mShopOffer.getMainImagePath())) {
                Picasso.with(this)
                        .load(mShopOffer.getMainImagePath())
                        .placeholder(R.mipmap.ic_default_card)
                        .error(R.mipmap.ic_default_card)
                        .into(mIvShopOfferMainImage);
            } else {
                mIvShopOfferMainImage.setImageResource(R.mipmap.ic_default_card);
            }
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_shop_offer_detail;
    }

    public static void launch(BaseActivity activity, ShopOffer item) {
        Intent intent = new Intent(activity, ShopOfferDetailActivity.class);
        intent.putExtra(EXTRA_PARAM_SHOP_OFFER, item);

        Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle();

        ActivityCompat.startActivity(activity, intent, options);
    }

}
