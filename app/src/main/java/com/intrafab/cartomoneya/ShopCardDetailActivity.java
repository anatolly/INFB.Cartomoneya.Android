package com.intrafab.cartomoneya;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.intrafab.cartomoneya.actions.ActionRequestShopBrand;
import com.intrafab.cartomoneya.adapters.CardPageAdapter;
import com.intrafab.cartomoneya.data.ShopBrand;
import com.intrafab.cartomoneya.data.ShopCard;
import com.intrafab.cartomoneya.fragments.PlaceholderCardPageFragment;
import com.intrafab.cartomoneya.loaders.ShopBrandListLoader;
import com.intrafab.cartomoneya.utils.Images;
import com.intrafab.cartomoneya.utils.Logger;
import com.telly.groundy.CallbacksManager;
import com.telly.groundy.Groundy;

import java.util.List;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Artemiy Terekhov on 07.05.2015.
 */
public class ShopCardDetailActivity extends BaseActivity {

    public static final String TAG = ShopCardDetailActivity.class.getName();

    public static final String EXTRA_PARAM_SHOP_CARD = "param_shop_card";
    private static final int NUM_PAGES = 2;

    private static final int LOADER_SHOP_BRAND_ID = 10;

    private ShopCard mShopCard;
    private ShopBrand mShopBrand;

    private CallbacksManager mCallbacksManager;

    private ViewPager mViewpager;
    private CircleIndicator mIndicator;
    private CardPageAdapter mPagerAdapter;
    private ImageView mBarcodeImage;
    private TextView mBarcodeNumber;
    private TextView mNotesText;
    private TextView mShopBrandView;

    private android.app.LoaderManager.LoaderCallbacks<List<ShopBrand>> mLoaderCallback = new android.app.LoaderManager.LoaderCallbacks<List<ShopBrand>>() {
        @Override
        public android.content.Loader<List<ShopBrand>> onCreateLoader(int id, Bundle args) {
            switch (id) {
                case LOADER_SHOP_BRAND_ID:
                    return createShopBrandLoader();
                default:
                    return null;
            }
        }

        @Override
        public void onLoadFinished(android.content.Loader<List<ShopBrand>> loader, List<ShopBrand> data) {
            int id = loader.getId();
            switch (id) {
                case LOADER_SHOP_BRAND_ID:
                    finishedShopBrandLoader(data);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onLoaderReset(android.content.Loader<List<ShopBrand>> loader) {
            int id = loader.getId();
            switch (id) {
                case LOADER_SHOP_BRAND_ID:
                    resetShopBrandLoader();
                    break;
                default:
                    break;
            }
        }
    };

    private void startShopCardsLoading() {
        getLoaderManager().initLoader(LOADER_SHOP_BRAND_ID, null, mLoaderCallback);
    }

    private android.content.Loader<List<ShopBrand>> createShopBrandLoader() {
        Logger.d(TAG, "createShopBrandLoader");
        return new ShopBrandListLoader(ShopCardDetailActivity.this);
    }

    private void finishedShopBrandLoader(List<ShopBrand> data) {
        if (data == null) {
            Logger.d(TAG, "finishedShopBrandLoader start ActionRequestShopBrand");
            Groundy.create(ActionRequestShopBrand.class)
                    .callback(ShopCardDetailActivity.this)
                    .callbackManager(mCallbacksManager)
                    .queueUsing(ShopCardDetailActivity.this);
        } else {
            Logger.d(TAG, "finishedShopBrandLoader setData size = " + data.size());
            for (ShopBrand brand : data) {
                if (brand.getId() == mShopCard.getShopBrand()) {
                    mShopBrand = brand;
                    break;
                }
            }

            if (mShopBrand != null) {
                mShopBrandView.setText(mShopBrand.getName());
            } else {
                mShopBrandView.setText("");
            }
        }
    }

    private void resetShopBrandLoader() {
        Logger.d(TAG, "resetShopBrandLoader");
    }

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

        mViewpager = (ViewPager) findViewById(R.id.pager);
        mIndicator = (CircleIndicator) findViewById(R.id.indicator_default);

        mShopBrandView = (TextView) findViewById(R.id.tvShopBrand);
        mShopBrandView.setText("");

        mBarcodeImage = (ImageView) findViewById(R.id.ivBarcodeImage);
        mBarcodeNumber = (TextView) findViewById(R.id.tvBarcodeNumber);
        mNotesText = (TextView) findViewById(R.id.tvNotesText);

        mPagerAdapter = new CardPageAdapter(getSupportFragmentManager());

        mPagerAdapter.add(PlaceholderCardPageFragment.create(1, mShopCard.getFrontImageFile()));
        mPagerAdapter.add(PlaceholderCardPageFragment.create(2, mShopCard.getBackImageFile()));

        mViewpager.setOffscreenPageLimit(NUM_PAGES);
        mViewpager.setAdapter(mPagerAdapter);

        mIndicator.setViewPager(mViewpager);

        mBarcodeImage.post(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                try {

                    bitmap = Images.encodeAsBitmap(mShopCard.getBarcode(), BarcodeFormat.CODE_128, mBarcodeImage.getWidth(), mBarcodeImage.getHeight());
                    mBarcodeImage.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                    mBarcodeImage.setImageResource(R.mipmap.ic_barcode);
                } catch (Exception e) {
                    e.printStackTrace();
                    mBarcodeImage.setImageResource(R.mipmap.ic_barcode);
                } catch (Throwable e) {
                    e.printStackTrace();
                    mBarcodeImage.setImageResource(R.mipmap.ic_barcode);
                } finally {
                    //bitmap.recycle();
                }

                if (!TextUtils.isEmpty(mShopCard.getBarcode())) {
                    mBarcodeNumber.setText(mShopCard.getBarcode());
                    Paint textPaint = mBarcodeNumber.getPaint();
                    float width = textPaint.measureText(mShopCard.getBarcode());
                    mBarcodeNumber.setTextScaleX(((float) mBarcodeNumber.getWidth() / width) - 0.2f);
                } else {
                    mBarcodeNumber.setText("");
                }

                startShopCardsLoading();
            }
        });

        if (!TextUtils.isEmpty(mShopCard.getNotes())) {
            mNotesText.setVisibility(View.VISIBLE);
            mNotesText.setText(mShopCard.getNotes());
        } else {
            mNotesText.setVisibility(View.INVISIBLE);
        }

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
