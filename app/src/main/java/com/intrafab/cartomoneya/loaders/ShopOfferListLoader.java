package com.intrafab.cartomoneya.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.intrafab.cartomoneya.Constants;
import com.intrafab.cartomoneya.data.ShopOffer;
import com.intrafab.cartomoneya.db.DBManager;

import java.util.List;

/**
 * Created by Vasily Laushkin <vaslinux@gmail.com> on 06/06/15.
 */
public class ShopOfferListLoader extends AsyncTaskLoader<List<ShopOffer>> {
    public static final String TAG = ShopOfferListLoader.class.getName();

    private List<ShopOffer> mData;

    public ShopOfferListLoader(Context ctx) {
        super(ctx);
    }

    @Override
    public void onCanceled(List<ShopOffer> data) {
        super.onCanceled(data);

        releaseResources(data);
    }

    @Override
    public void deliverResult(List<ShopOffer> data) {
        if (isReset()) {
            releaseResources(data);
            return;
        }

        List<ShopOffer> oldData = mData;
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

        DBManager.getInstance().registerObserver(getContext(), this, ShopOfferListLoader.class);

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
        DBManager.getInstance().unregisterObserver(this, ShopOfferListLoader.class);
    }

    @Override
    public List<ShopOffer> loadInBackground() {
        return DBManager.getInstance().readArrayToList(getContext(), Constants.Prefs.PREF_PARAM_SHOP_OFFERS, ShopOffer[].class);
    }

    private void releaseResources(List<ShopOffer> data) {
        // For a simple List, there is nothing to do. For something like a Cursor,
        // we would close it in this method. All resources associated with the
        // Loader should be released here.
    }

    @Override
    public boolean equals(Object object)
    {
        boolean isEqual= false;

        if (object != null && object instanceof ShopOfferListLoader)
        {
            isEqual = this.TAG.equals( ((ShopOfferListLoader) object).TAG );
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return this.TAG.hashCode();
    }
}
