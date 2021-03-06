package com.intrafab.cartomoneya.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.intrafab.cartomoneya.Constants;
import com.intrafab.cartomoneya.data.BusinessCard;
import com.intrafab.cartomoneya.data.BusinessCardPopulated;
import com.intrafab.cartomoneya.db.DBManager;

import java.util.List;

/**
 * Created by mikhailzubov on 08.06.15.
 */
public class BizCardPopulatedListLoader  extends AsyncTaskLoader<List<BusinessCardPopulated>> {
    public static final String TAG = BizCardPopulatedListLoader.class.getName();

    private List<BusinessCardPopulated> mData;

    public BizCardPopulatedListLoader(Context ctx) {
        super(ctx);
    }

    @Override
    public void onCanceled(List<BusinessCardPopulated> data) {
        super.onCanceled(data);

        releaseResources(data);
    }

    @Override
    public void deliverResult(List<BusinessCardPopulated> data) {
        if (isReset()) {
            releaseResources(data);
            return;
        }

        List<BusinessCardPopulated> oldData = mData;
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

        DBManager.getInstance().registerObserver(getContext(), this, BizCardPopulatedListLoader.class);

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
        DBManager.getInstance().unregisterObserver(this, BizCardPopulatedListLoader.class);
    }

    @Override
    public List<BusinessCardPopulated> loadInBackground() {
        return DBManager.getInstance().readArrayToList(getContext(), Constants.Prefs.PREF_PARAM_BUSINESS_CARDS_POPULATED, BusinessCardPopulated[].class);
    }

    private void releaseResources(List<BusinessCardPopulated> data) {
        // For a simple List, there is nothing to do. For something like a Cursor,
        // we would close it in this method. All resources associated with the
        // Loader should be released here.
    }

    @Override
    public boolean equals(Object object)
    {
        boolean isEqual= false;

        if (object != null && object instanceof BizCardPopulatedListLoader)
        {
            isEqual = this.TAG.equals( ((BizCardPopulatedListLoader) object).TAG );
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return this.TAG.hashCode();
    }
}
