package com.intrafab.cartomoneya;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.intrafab.cartomoneya.actions.ActionRequestShoppingCardsTask;
import com.intrafab.cartomoneya.adapters.ShopCardAdapter;
import com.intrafab.cartomoneya.data.ShopCard;
import com.intrafab.cartomoneya.fragments.PlaceholderShoppingCardsFragment;
import com.intrafab.cartomoneya.loaders.ShopCardListLoader;
import com.intrafab.cartomoneya.utils.Logger;
import com.telly.groundy.CallbacksManager;
import com.telly.groundy.Groundy;

import java.util.List;

/**
 * Created by Artemiy Terekhov on 06.05.2015.
 */
public class ShoppingCardsActivity extends BaseActivity
        implements ShopCardAdapter.OnClickListener,
        View.OnClickListener{

    public static final String TAG = ShoppingCardsActivity.class.getName();

    private static final int LOADER_SHOP_CARD_ID = 10;

    private CallbacksManager mCallbacksManager;
    private TextView mBtnOffers;
    private TextView mBtnShoppingList;

    private android.app.LoaderManager.LoaderCallbacks<List<ShopCard>> mLoaderCallback = new android.app.LoaderManager.LoaderCallbacks<List<ShopCard>>() {
        @Override
        public android.content.Loader<List<ShopCard>> onCreateLoader(int id, Bundle args) {
            switch (id) {
                case LOADER_SHOP_CARD_ID:
                    return createShopCardLoader();
                default:
                    return null;
            }
        }

        @Override
        public void onLoadFinished(android.content.Loader<List<ShopCard>> loader, List<ShopCard> data) {
            int id = loader.getId();
            switch (id) {
                case LOADER_SHOP_CARD_ID:
                    finishedShopCardLoader(data);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onLoaderReset(android.content.Loader<List<ShopCard>> loader) {
            int id = loader.getId();
            switch (id) {
                case LOADER_SHOP_CARD_ID:
                    resetShopCardLoader();
                    break;
                default:
                    break;
            }
        }
    };

    private android.content.Loader<List<ShopCard>> createShopCardLoader() {
        Logger.d(TAG, "createShopCardLoader");
        return new ShopCardListLoader(ShoppingCardsActivity.this);
    }

    private void finishedShopCardLoader(List<ShopCard> data) {
        PlaceholderShoppingCardsFragment fragment = getFragment();
        if (data == null) {
            Logger.d(TAG, "finishedShopCardLoader start ActionRequestShoppingCardsTask");
            if (fragment != null)
                fragment.showProgress();
            Groundy.create(ActionRequestShoppingCardsTask.class)
                    .callback(ShoppingCardsActivity.this)
                    .callbackManager(mCallbacksManager)
                    .queueUsing(ShoppingCardsActivity.this);
        } else {
            Logger.d(TAG, "finishedShopCardLoader setData size = " + data.size());
            if (fragment != null) {
                fragment.hideProgress();
                fragment.setData(data);
            }
        }
    }

    private void resetShopCardLoader() {
        Logger.d(TAG, "resetShopCardLoader");

        PlaceholderShoppingCardsFragment fragment = getFragment();
        if (fragment != null) {
            fragment.hideProgress();
            fragment.setData(null);
        }
    }

    public PlaceholderShoppingCardsFragment getFragment() {
        Fragment fragment = getFragmentManager().findFragmentByTag(PlaceholderShoppingCardsFragment.TAG);
        if (fragment != null && fragment instanceof PlaceholderShoppingCardsFragment)
            return (PlaceholderShoppingCardsFragment) fragment;

        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().getThemedContext();

        getSupportActionBar().setTitle(R.string.shopping_card_screen_header);
        showActionBar();

        mCallbacksManager = CallbacksManager.init(savedInstanceState);
        mCallbacksManager.linkCallbacks(this);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderShoppingCardsFragment(), PlaceholderShoppingCardsFragment.TAG)
                    .commit();
        }
        mBtnOffers = (TextView) this.findViewById(R.id.btnOffers);
        mBtnShoppingList = (TextView) this.findViewById(R.id.btnShoppingList);

        int rippleColor = getResources().getColor(R.color.colorLightEditTextHint);
        float rippleAlpha = 0.5f;

        mBtnOffers.setOnClickListener(this);

        MaterialRippleLayout.on(mBtnOffers)
                .rippleColor(rippleColor)
                .rippleAlpha(rippleAlpha)
                .rippleHover(true)
                .create();

        mBtnShoppingList.setOnClickListener(this);

        MaterialRippleLayout.on(mBtnShoppingList)
                .rippleColor(rippleColor)
                .rippleAlpha(rippleAlpha)
                .rippleHover(true)
                .create();

        toolbar.post(new Runnable() {
            @Override
            public void run() {
                startShopCardsLoading();
            }
        });
    }

    private void startShopCardsLoading() {
        PlaceholderShoppingCardsFragment fragment = getFragment();
        if (fragment == null)
            return;

        if (fragment.isProgress())
            return;

        fragment.showProgress();
        getLoaderManager().initLoader(LOADER_SHOP_CARD_ID, null, mLoaderCallback);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_shopping_cards;
    }

    public static void launch(BaseActivity activity) {
        Intent intent = new Intent(activity, ShoppingCardsActivity.class);

        Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle();

        ActivityCompat.startActivity(activity, intent, options);
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
    public void onClick(View v) {

    }

    @Override
    public void onClickItem(ShopCard itemShopCard) {
        ShopCardDetailActivity.launch(this, itemShopCard);
    }
}
