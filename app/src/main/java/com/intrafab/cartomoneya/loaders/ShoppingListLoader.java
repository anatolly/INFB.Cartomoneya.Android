package com.intrafab.cartomoneya.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.intrafab.cartomoneya.Constants;
import com.intrafab.cartomoneya.data.ShoppingListItem;
import com.intrafab.cartomoneya.db.DBManager;

import java.util.List;

/**
 * Created by mono on 06/06/15.
 */
public class ShoppingListLoader extends AsyncTaskLoader<List<ShoppingListItem>> {

    public static final String TAG = ShoppingListLoader.class.getName();

    private List<ShoppingListItem> mData;

    public ShoppingListLoader(Context context) {
        super(context);
    }

    @Override
    public void onCanceled(List<ShoppingListItem> data) {
        super.onCanceled(data);

        releaseResources(data);
    }

    @Override
    public void deliverResult(List<ShoppingListItem> data) {
        if (isReset()) {
            releaseResources(data);
            return;
        }

        List<ShoppingListItem> oldData = mData;
        mData = data;

        if (isStarted()) {
            super.deliverResult(data);
        }

        if (oldData != null && oldData != data) {
            releaseResources(oldData);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
        }

        DBManager.getInstance().registerObserver(getContext(), this, ShoppingListLoader.class);

        if (takeContentChanged() || mData == null) {
            forceLoad();
        }
    }

    @Override
    public void forceLoad() {
        super.forceLoad();
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();

        // At this point we can release the resources associated with 'apps'.
        if (mData != null) {
            releaseResources(mData);
            mData = null;
        }

        // The Loader is being reset, so we should stop monitoring for changes.
        DBManager.getInstance().unregisterObserver(this, ShoppingListLoader.class);
    }

    @Override
    public List<ShoppingListItem> loadInBackground() {
        return DBManager.getInstance().readArrayToList(getContext(), Constants.Prefs.PREF_PARAM_SHOPPING_LIST, ShoppingListItem[].class);
    }

    private void releaseResources(List<ShoppingListItem> data) {
        // For a simple List, there is nothing to do. For something like a Cursor,
        // we would close it in this method. All resources associated with the
        // Loader should be released here.
    }

    @Override
    public boolean equals(Object object)
    {
        boolean isEqual= false;

        if (object != null && object instanceof ShoppingListLoader)
        {
            isEqual = this.TAG.equals( ((ShoppingListLoader) object).TAG );
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return this.TAG.hashCode();
    }
}
