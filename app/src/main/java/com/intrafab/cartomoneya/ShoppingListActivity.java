package com.intrafab.cartomoneya;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.intrafab.cartomoneya.actions.ActionRequestCreateShoppingItem;
import com.intrafab.cartomoneya.actions.ActionRequestDeleteShoppingItem;
import com.intrafab.cartomoneya.actions.ActionRequestShoppingListTask;
import com.intrafab.cartomoneya.actions.ActionRequestUpdateShoppingItem;
import com.intrafab.cartomoneya.adapters.ShoppingListItemAdapter;
import com.intrafab.cartomoneya.data.ShoppingListItem;
import com.intrafab.cartomoneya.db.DBManager;
import com.intrafab.cartomoneya.fragments.EditShoppingListItemDialogFragment;
import com.intrafab.cartomoneya.fragments.PlaceholderShoppingListFragment;
import com.intrafab.cartomoneya.fragments.ProgressDialogFragment;
import com.intrafab.cartomoneya.loaders.ShoppingListLoader;
import com.intrafab.cartomoneya.utils.Logger;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.telly.groundy.CallbacksManager;
import com.telly.groundy.Groundy;
import com.telly.groundy.annotations.OnFailure;
import com.telly.groundy.annotations.OnSuccess;
import com.telly.groundy.annotations.Param;

import java.util.List;

/**
 * Created by Artemiy Terekhov on 07.05.2015.
 */
public class ShoppingListActivity extends BaseActivity
        implements ShoppingListItemAdapter.OnClickListener,
        EditShoppingListItemDialogFragment.EditShoppingListItemDialogListener {

    public static final String TAG = ShoppingListActivity.class.getName();

    private static final int LOADER_SHOPPING_LIST_ID = 10;

    private CallbacksManager mCallbacksManager;
    private ProgressDialogFragment mProgressDialogFragment;
    private EditShoppingListItemDialogFragment mEditDialog;

    private android.app.LoaderManager.LoaderCallbacks<List<ShoppingListItem>> mLoaderCallback = new android.app.LoaderManager.LoaderCallbacks<List<ShoppingListItem>>() {
        @Override
        public android.content.Loader<List<ShoppingListItem>> onCreateLoader(int id, Bundle args) {
            switch (id) {
                case LOADER_SHOPPING_LIST_ID:
                    return createShoppingListLoader();
                default:
                    return null;
            }
        }

        @Override
        public void onLoadFinished(android.content.Loader<List<ShoppingListItem>> loader, List<ShoppingListItem> data) {
            int id = loader.getId();
            switch (id) {
                case LOADER_SHOPPING_LIST_ID:
                    finishedShoppingListLoader(data);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onLoaderReset(android.content.Loader<List<ShoppingListItem>> loader) {
            int id = loader.getId();
            switch (id) {
                case LOADER_SHOPPING_LIST_ID:
                    resetShoppingListLoader();
                    break;
                default:
                    break;
            }
        }
    };

    private android.content.Loader<List<ShoppingListItem>> createShoppingListLoader() {
        Logger.d(TAG, "createShoppingListLoader");
        return new ShoppingListLoader(ShoppingListActivity.this);
    }

    private void finishedShoppingListLoader(List<ShoppingListItem> data) {
        Logger.d(TAG, "finishedShoppingListLoader");


        PlaceholderShoppingListFragment fragment = getFragment();
        if (data == null) {
            Logger.d(TAG, "finishedShoppingListLoader start ActionRequestShoppingListTask");
            if (fragment != null)
                fragment.showProgress();
            Groundy.create(ActionRequestShoppingListTask.class)
                    .callback(ShoppingListActivity.this)
                    .callbackManager(mCallbacksManager)
                    .arg(ActionRequestShoppingListTask.ARG_USER, AppApplication.getApplication(this).getUserInfo())
                    .queueUsing(ShoppingListActivity.this);
        } else {
            Logger.d(TAG, "finishedBizCardLoader setData size = " + data.size());
            if (fragment != null) {
                fragment.hideProgress();
                fragment.setData(data);
            }
        }
    }

    private void resetShoppingListLoader() {
        Logger.d(TAG, "resetShoppingListLoader");

        PlaceholderShoppingListFragment fragment = getFragment();
        if (fragment != null) {
            fragment.hideProgress();
            fragment.setData(null);
        }
    }

    public PlaceholderShoppingListFragment getFragment() {
        Fragment fragment = getFragmentManager().findFragmentByTag(PlaceholderShoppingListFragment.TAG);
        if (fragment != null && fragment instanceof PlaceholderShoppingListFragment)
            return (PlaceholderShoppingListFragment) fragment;

        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().getThemedContext();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getSupportActionBar().setTitle(R.string.shopping_list_title);
        showActionBar();

        mProgressDialogFragment = new ProgressDialogFragment();

        mCallbacksManager = CallbacksManager.init(savedInstanceState);
        mCallbacksManager.linkCallbacks(this);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderShoppingListFragment(), PlaceholderShoppingListFragment.TAG)
                    .commit();
        }

        toolbar.post(new Runnable() {
            @Override
            public void run() {
                startShoppingListLoading();
            }
        });
    }

    private void startShoppingListLoading() {
        PlaceholderShoppingListFragment fragment = getFragment();
        if (fragment == null)
            return;

        if (fragment.isProgress())
            return;

        fragment.showProgress();
        getLoaderManager().initLoader(LOADER_SHOPPING_LIST_ID, null, mLoaderCallback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shopping_list, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sync) {
            PlaceholderShoppingListFragment fragment = getFragment();
            if (fragment == null)
                return true;

            if (fragment != null) {
                fragment.hideProgress();
                fragment.setData(null);
            }
            DBManager.getInstance().deleteObject(Constants.Prefs.PREF_PARAM_SHOPPING_LIST, ShoppingListLoader.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_shopping_list;
    }

    public static void launch(BaseActivity activity) {
        Intent intent = new Intent(activity, ShoppingListActivity.class);

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
    public void onNewItemCreated(ShoppingListItem itemShopList) {
        showProgress();

        Groundy.create(ActionRequestCreateShoppingItem.class)
                .callback(ShoppingListActivity.this)
                .callbackManager(mCallbacksManager)
                .arg(ActionRequestCreateShoppingItem.ARG_SHOPPING_ITEM, itemShopList)
                .queueUsing(ShoppingListActivity.this);
    }

    @Override
    public void onItemDelete(ShoppingListItem itemShopItem) {
        showProgress();

        Groundy.create(ActionRequestDeleteShoppingItem.class)
                .callback(ShoppingListActivity.this)
                .callbackManager(mCallbacksManager)
                .arg(ActionRequestDeleteShoppingItem.ARG_SHOPPING_ITEM, itemShopItem)
                .queueUsing(ShoppingListActivity.this);
    }

    @Override
    public void onItemEdit(ShoppingListItem itemShopItem) {
        mEditDialog = new EditShoppingListItemDialogFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(EditShoppingListItemDialogFragment.ARG_SHOPPING_ITEM, itemShopItem);
        mEditDialog.setArguments(arguments);
        mEditDialog.show(getSupportFragmentManager(), mEditDialog.toString());
    }

    @Override
    public void onCheckChange(ShoppingListItem itemShopItem, boolean value) {
        itemShopItem.setDone(value);

        showProgress();

        Groundy.create(ActionRequestUpdateShoppingItem.class)
                .callback(ShoppingListActivity.this)
                .callbackManager(mCallbacksManager)
                .arg(ActionRequestUpdateShoppingItem.ARG_SHOPPING_ITEM, itemShopItem)
                .queueUsing(ShoppingListActivity.this);
    }

    @Override
    public void onItemEdited(ShoppingListItem item) {
        showProgress();

        Groundy.create(ActionRequestUpdateShoppingItem.class)
                .callback(ShoppingListActivity.this)
                .callbackManager(mCallbacksManager)
                .arg(ActionRequestUpdateShoppingItem.ARG_SHOPPING_ITEM, item)
                .queueUsing(ShoppingListActivity.this);
    }

    private void showProgress() {
        mProgressDialogFragment.show(getSupportFragmentManager(), mProgressDialogFragment.toString());
    }

    private void hideProgress() {
        mProgressDialogFragment.dismiss();
    }

    private void showSnackBarError(String error) {
        SnackbarManager.show(
                Snackbar.with(ShoppingListActivity.this) // context
                        .type(SnackbarType.MULTI_LINE)
                        .text(error)
                        .color(getResources().getColor(R.color.colorLightError))
                        .textColor(getResources().getColor(R.color.colorLightEditTextHint))
                        .swipeToDismiss(true)
                        .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                , ShoppingListActivity.this); // activity where it is displayed
    }

    @OnSuccess(ActionRequestCreateShoppingItem.class)
    public void onSuccessRequestCreateShoppingList() {
        hideProgress();
    }

    @OnFailure(ActionRequestCreateShoppingItem.class)
    public void onFailureRequestCreateShoppingList(@Param(Constants.Extras.PARAM_INTERNET_AVAILABLE) boolean isAvailable) {
        hideProgress();

        if (!isAvailable) {
            showSnackBarError(getResources().getString(R.string.error_internet_not_available));
        } else {
            showSnackBarError(getResources().getString(R.string.error_occurred));
        }
    }

    @OnSuccess(ActionRequestDeleteShoppingItem.class)
    public void onSuccessRequestDeleteShoppingList() {
        hideProgress();
    }

    @OnFailure(ActionRequestDeleteShoppingItem.class)
    public void onFailureRequestDeleteShoppingList(@Param(Constants.Extras.PARAM_INTERNET_AVAILABLE) boolean isAvailable) {
        hideProgress();

        if (!isAvailable) {
            showSnackBarError(getResources().getString(R.string.error_internet_not_available));
        } else {
            showSnackBarError(getResources().getString(R.string.error_occurred));
        }
    }

    @OnSuccess(ActionRequestUpdateShoppingItem.class)
    public void onSuccessRequestUpdateShoppingList() {
        hideProgress();
    }

    @OnFailure(ActionRequestUpdateShoppingItem.class)
    public void onFailureRequestUpdateShoppingList(@Param(Constants.Extras.PARAM_INTERNET_AVAILABLE) boolean isAvailable) {
        hideProgress();

        if (!isAvailable) {
            showSnackBarError(getResources().getString(R.string.error_internet_not_available));
        } else {
            showSnackBarError(getResources().getString(R.string.error_occurred));
        }
    }


}
