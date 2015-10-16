package com.intrafab.cartomoneya;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.Menu;
import android.view.MenuItem;

import com.intrafab.cartomoneya.actions.ActionRequestBusinessCardsTask;
import com.intrafab.cartomoneya.adapters.BizCardAdapter;
import com.intrafab.cartomoneya.data.BusinessCardPopulated;
import com.intrafab.cartomoneya.db.DBManager;
import com.intrafab.cartomoneya.fragments.PlaceholderBizCardsFragment;
import com.intrafab.cartomoneya.loaders.BizCardPopulatedListLoader;
import com.intrafab.cartomoneya.utils.Logger;
import com.telly.groundy.CallbacksManager;
import com.telly.groundy.Groundy;

import java.util.List;

/**
 * Created by Artemiy Terekhov on 06.05.2015.
 */
public class FavoriteCardsActivity extends BaseActivity implements BizCardAdapter.OnClickListener {


    public static final int REQUEST_CODE_NEW_BCARD = 500;

    public static final String TAG = BusinessCardsActivity.class.getName();

    private static final int LOADER_BIZ_CARD_ID = 10;

    private CallbacksManager mCallbacksManager;

    private android.app.LoaderManager.LoaderCallbacks<List<BusinessCardPopulated>> mLoaderCallback = new android.app.LoaderManager.LoaderCallbacks<List<BusinessCardPopulated>>() {
        @Override
        public android.content.Loader<List<BusinessCardPopulated>> onCreateLoader(int id, Bundle args) {
            switch (id) {
                case LOADER_BIZ_CARD_ID:
                    return createBizCardLoader();
                default:
                    return null;
            }
        }

        @Override
        public void onLoadFinished(android.content.Loader<List<BusinessCardPopulated>> loader, List<BusinessCardPopulated> data) {
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
        public void onLoaderReset(android.content.Loader<List<BusinessCardPopulated>> loader) {
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

    private android.content.Loader<List<BusinessCardPopulated>> createBizCardLoader() {
        Logger.d(TAG, "createBizCardLoader");
        return new BizCardPopulatedListLoader(FavoriteCardsActivity.this);
    }

    private void finishedBizCardLoader(List<BusinessCardPopulated> data) {
        Logger.d(TAG, "finishedBizCardLoader");


        PlaceholderBizCardsFragment fragment = getFragment();
        if (data == null) {
            Logger.d(TAG, "finishedBizCardLoader start ActionRequestBizCardsTask");
            if (fragment != null)
                fragment.showProgress();
            Groundy.create(ActionRequestBusinessCardsTask.class)
                    .callback(FavoriteCardsActivity.this)
                    .callbackManager(mCallbacksManager)
                    .queueUsing(FavoriteCardsActivity.this);
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
        if (fragment != null && fragment instanceof PlaceholderBizCardsFragment) {
            return (PlaceholderBizCardsFragment) fragment;
        }
        else {
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().getThemedContext();

        getSupportActionBar().setTitle(R.string.favorites_card_screen_header);
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
//        if (id == R.id.action_settings) {
//            Toast.makeText(this, "Coming soon. Show settings screen", Toast.LENGTH_SHORT).show();
//            return true;
//        } else if (id == R.id.action_search_card) {
//            Toast.makeText(this, "Coming soon. Show search", Toast.LENGTH_SHORT).show();
//            return true;
//        } else
        if (id == R.id.action_add_card) {
            NewBusinessCardActivity.launch(this, REQUEST_CODE_NEW_BCARD);
            return true;
        }
         else if (id == R.id.action_sync) {
            PlaceholderBizCardsFragment fragment = getFragment();
            if (fragment == null)
                return true;

            if (fragment != null) {
                fragment.hideProgress();
                fragment.setData(null);
            }
            DBManager.getInstance().deleteObject(Constants.Prefs.PREF_PARAM_BUSINESS_CARDS_POPULATED, BizCardPopulatedListLoader.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_business_cards;
    }

    @Override
    protected int getActivityTheme() {
        return R.style.AppBizTheme;
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
    public void onClickItem(BusinessCardPopulated itemShopCard) {
        BusinessCardDetailActivity.launch(this, itemShopCard);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( requestCode == REQUEST_CODE_NEW_BCARD) {
            PlaceholderBizCardsFragment fragment = getFragment();
            if (fragment == null)
                return;

            if (fragment != null) {
                fragment.hideProgress();
                fragment.setData(null);
            }
            DBManager.getInstance().deleteObject(Constants.Prefs.PREF_PARAM_BUSINESS_CARDS_POPULATED, BizCardPopulatedListLoader.class);
        }
    }

}
