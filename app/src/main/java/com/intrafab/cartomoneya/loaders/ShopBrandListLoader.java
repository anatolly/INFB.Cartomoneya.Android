package com.intrafab.cartomoneya.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.intrafab.cartomoneya.Constants;
import com.intrafab.cartomoneya.data.ShopBrand;
import com.intrafab.cartomoneya.db.DBManager;

import java.util.List;

/**
 * Created by Artemiy Terekhov on 08.05.2015.
 */
public class ShopBrandListLoader extends AsyncTaskLoader<List<ShopBrand>> {

    public static final String TAG = ShopBrandListLoader.class.getName();

    private List<ShopBrand> mData;

    public ShopBrandListLoader(Context ctx) {
        super(ctx);
    }

    @Override
    public void onCanceled(List<ShopBrand> data) {
        super.onCanceled(data);

        releaseResources(data);
    }

    @Override
    public void deliverResult(List<ShopBrand> data) {
        if (isReset()) {
            releaseResources(data);
            return;
        }

        List<ShopBrand> oldData = mData;
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

        DBManager.getInstance().registerObserver(getContext(), this, ShopBrandListLoader.class);

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
        DBManager.getInstance().unregisterObserver(this, ShopBrandListLoader.class);
    }

    @Override
    public List<ShopBrand> loadInBackground() {
        return DBManager.getInstance().readArrayToList(getContext(), Constants.Prefs.PREF_PARAM_SHOP_BRANDS, ShopBrand[].class);
    }

    private void releaseResources(List<ShopBrand> data) {
        // For a simple List, there is nothing to do. For something like a Cursor,
        // we would close it in this method. All resources associated with the
        // Loader should be released here.
    }

    @Override
    public boolean equals(Object object)
    {
        boolean isEqual= false;

        if (object != null && object instanceof ShopBrandListLoader)
        {
            isEqual = this.TAG.equals( ((ShopBrandListLoader) object).TAG );
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return this.TAG.hashCode();
    }

}
