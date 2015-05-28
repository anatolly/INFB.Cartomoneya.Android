package com.intrafab.cartomoneya;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.intrafab.cartomoneya.actions.ActionRequestBizCardsTask;
import com.intrafab.cartomoneya.adapters.BizCardAdapter;
import com.intrafab.cartomoneya.data.BizCard;
import com.intrafab.cartomoneya.db.DBManager;
import com.intrafab.cartomoneya.fragments.PlaceholderBizCardsFragment;
import com.intrafab.cartomoneya.fragments.PlaceholderShoppingCardsFragment;
import com.intrafab.cartomoneya.loaders.BizCardListLoader;
import com.intrafab.cartomoneya.loaders.ShopCardListLoader;
import com.intrafab.cartomoneya.utils.Logger;
import com.telly.groundy.CallbacksManager;
import com.telly.groundy.Groundy;

import java.util.List;

/**
 * Created by Artemiy Terekhov on 06.05.2015.
 */
public class BusinessCardsActivity extends BaseActivity implements BizCardAdapter.OnClickListener {

    public static final String TAG = BusinessCardsActivity.class.getName();

    private static final int LOADER_BIZ_CARD_ID = 10;

    private CallbacksManager mCallbacksManager;

    private android.app.LoaderManager.LoaderCallbacks<List<BizCard>> mLoaderCallback = new android.app.LoaderManager.LoaderCallbacks<List<BizCard>>() {
        @Override
        public android.content.Loader<List<BizCard>> onCreateLoader(int id, Bundle args) {
            switch (id) {
                case LOADER_BIZ_CARD_ID:
                    return createBizCardLoader();
                default:
                    return null;
            }
        }

        @Override
        public void onLoadFinished(android.content.Loader<List<BizCard>> loader, List<BizCard> data) {
            int id = loader.getId();
            switch (id) {
                case LOADER_BIZ_CARD_ID:
                    finishedBizCardLoader(data);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onLoaderReset(android.content.Loader<List<BizCard>> loader) {
            int id = loader.getId();
            switch (id) {
                case LOADER_BIZ_CARD_ID:
                    resetBizCardLoader();
                    break;
                default:
                    break;
            }
        }
    };

    private android.content.Loader<List<BizCard>> createBizCardLoader() {
        Logger.d(TAG, "createBizCardLoader");
        return new BizCardListLoader(BusinessCardsActivity.this);
    }

    private void finishedBizCardLoader(List<BizCard> data) {
        Logger.d(TAG, "finishedBizCardLoader");


        PlaceholderBizCardsFragment fragment = getFragment();
        if (data == null) {
            Logger.d(TAG, "finishedBizCardLoader start ActionRequestBizCardsTask");
            if (fragment != null)
                fragment.showProgress();
            Groundy.create(ActionRequestBizCardsTask.class)
                    .callback(BusinessCardsActivity.this)
                    .callbackManager(mCallbacksManager)
                    .queueUsing(BusinessCardsActivity.this);
        } else {
            Logger.d(TAG, "finishedBizCardLoader setData size = " + data.size());
            if (fragment != null) {
                fragment.hideProgress();
                fragment.setData(data);
            }
        }
    }

    private void resetBizCardLoader() {
        Logger.d(TAG, "resetBizCardLoader");

        PlaceholderBizCardsFragment fragment = getFragment();
        if (fragment != null) {
            fragment.hideProgress();
            fragment.setData(null);
        }
    }

    public PlaceholderBizCardsFragment getFragment() {
        Fragment fragment = getFragmentManager().findFragmentByTag(PlaceholderBizCardsFragment.TAG);
        if (fragment != null && fragment instanceof PlaceholderBizCardsFragment)
            return (PlaceholderBizCardsFragment) fragment;

        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().getThemedContext();

        getSupportActionBar().setTitle(R.string.business_card_screen_header);
        showActionBar();

        mCallbacksManager = CallbacksManager.init(savedInstanceState);
        mCallbacksManager.linkCallbacks(this);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderBizCardsFragment(), PlaceholderBizCardsFragment.TAG)
                    .commit();
        }

        toolbar.post(new Runnable() {
            @Override
            public void run() {
                startBizCardsLoading();
            }
        });
    }

    private void startBizCardsLoading() {
        PlaceholderBizCardsFragment fragment = getFragment();
        if (fragment == null)
            return;

        if (fragment.isProgress())
            return;

        fragment.showProgress();
        getLoaderManager().initLoader(LOADER_BIZ_CARD_ID, null, mLoaderCallback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_biz_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Coming soon. Show settings screen", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_search_card) {
            Toast.makeText(this, "Coming soon. Show search", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_add_card) {
            NewCardActivity.launch(this);
            return true;
        } else if (id == R.id.action_sync) {
            PlaceholderBizCardsFragment fragment = getFragment();
            if (fragment == null)
                return true;

            if (fragment != null) {
                fragment.hideProgress();
                fragment.setData(null);
            }
            DBManager.getInstance().deleteObject(Constants.Prefs.PREF_PARAM_BUSINESS_CARDS, BizCardListLoader.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
    public void onClickItem(BizCard itemShopCard) {
        BusinessCardDetailActivity.launch(this, itemShopCard);
    }
}
