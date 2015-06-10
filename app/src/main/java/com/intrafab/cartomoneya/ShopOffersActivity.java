package com.intrafab.cartomoneya;

import android.app.Fragment;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.intrafab.cartomoneya.actions.ActionRequestShopOffersTask;
import com.intrafab.cartomoneya.adapters.ShopOfferAdapter;
import com.intrafab.cartomoneya.data.ShopOffer;
import com.intrafab.cartomoneya.db.DBManager;
import com.intrafab.cartomoneya.fragments.PlaceholderShopOffersFragment;
import com.intrafab.cartomoneya.loaders.ShopOfferListLoader;
import com.intrafab.cartomoneya.utils.Logger;
import com.telly.groundy.CallbacksManager;
import com.telly.groundy.Groundy;

import java.util.List;

/**
 * Created by Vasily Laushkin <vaslinux@gmail.com> on 30/05/15.
 */
public class ShopOffersActivity extends BaseActivity implements ShopOfferAdapter.OnClickListener {

    public static final String TAG = ShopOffersActivity.class.getName();

    private static final int LOADER_SHOP_OFFER_ID = 10;

    private CallbacksManager mCallbacksManager;

    private android.app.LoaderManager.LoaderCallbacks<List<ShopOffer>> mLoaderCallback = new android.app.LoaderManager.LoaderCallbacks<List<ShopOffer>>() {
        @Override
        public android.content.Loader<List<ShopOffer>> onCreateLoader(int id, Bundle args) {
            switch (id) {
                case LOADER_SHOP_OFFER_ID:
                    return createShopOfferLoader();
                default:
                    return null;
            }
        }

        @Override
        public void onLoadFinished(android.content.Loader<List<ShopOffer>> loader, List<ShopOffer> data) {
            int id = loader.getId();
            switch (id) {
                case LOADER_SHOP_OFFER_ID:
                    finishedShopOfferLoader(data);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onLoaderReset(android.content.Loader<List<ShopOffer>> loader) {
            int id = loader.getId();
            switch (id) {
                case LOADER_SHOP_OFFER_ID:
                    resetShopOfferLoader();
                    break;
                default:
                    break;
            }
        }
    };

    private Loader<List<ShopOffer>> createShopOfferLoader() {
        Logger.d(TAG, "createShopOfferLoader");
        return new ShopOfferListLoader(ShopOffersActivity.this);
    }

    private void finishedShopOfferLoader(List<ShopOffer> data) {
        Logger.d(TAG, "finishedShopOfferLoader");

        PlaceholderShopOffersFragment fragment = getFragment();
        if (data == null) {
            Logger.d(TAG, "finishedShopOfferLoader start ActionRequestShopOffersTask");
            if (fragment != null)
                fragment.showProgress();
            Groundy.create(ActionRequestShopOffersTask.class)
                    .callback(ShopOffersActivity.this)
                    .callbackManager(mCallbacksManager)
                    .queueUsing(ShopOffersActivity.this);
        } else {
            Logger.d(TAG, "finishedShopOfferLoader setData size = " + data.size());
            if (fragment != null) {
                fragment.hideProgress();
                fragment.setData(data);
            }
        }
    }

    private void resetShopOfferLoader() {
        Logger.d(TAG, "resetShopOfferLoader");

        PlaceholderShopOffersFragment fragment = getFragment();
        if (fragment != null) {
            fragment.hideProgress();
            fragment.setData(null);
        }
    }

    public PlaceholderShopOffersFragment getFragment() {
        Fragment fragment = getFragmentManager()
                .findFragmentByTag(PlaceholderShopOffersFragment.TAG);

        if (fragment != null && fragment instanceof PlaceholderShopOffersFragment)
            return (PlaceholderShopOffersFragment) fragment;

        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().getThemedContext();

        getSupportActionBar().setTitle(R.string.shop_offer_screen_header);
        showActionBar();

        mCallbacksManager = CallbacksManager.init(savedInstanceState);
        mCallbacksManager.linkCallbacks(this);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderShopOffersFragment(), PlaceholderShopOffersFragment.TAG)
                    .commit();
        }

        toolbar.post(new Runnable() {
            @Override
            public void run() {
                startShopCardsLoading();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shop_offer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search_card) {
            Toast.makeText(this, "Coming soon. Show search", Toast.LENGTH_SHORT).show();
            return true;
        }else if (id == R.id.action_sync) {
            PlaceholderShopOffersFragment fragment = getFragment();
            if (fragment == null)
                return true;

            if (fragment != null) {
                fragment.hideProgress();
                fragment.setData(null);
            }
            DBManager.getInstance().deleteObject(Constants.Prefs.PREF_PARAM_SHOP_OFFERS, ShopOfferListLoader.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startShopCardsLoading() {
        PlaceholderShopOffersFragment fragment = getFragment();
        if (fragment == null)
            return;

        if (fragment.isProgress())
            return;

        fragment.showProgress();
        getLoaderManager().initLoader(LOADER_SHOP_OFFER_ID, null, mLoaderCallback);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_shop_offers;
    }

    public static void launch(BaseActivity activity) {
        Intent intent = new Intent(activity, ShopOffersActivity.class);

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
    public void onClickItem(ShopOffer itemShopOffer) {
        ShopOfferDetailActivity.launch(this, itemShopOffer);
    }
}
