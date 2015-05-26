package com.intrafab.cartomoneya.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.intrafab.cartomoneya.Constants;
import com.intrafab.cartomoneya.data.BizCard;
import com.intrafab.cartomoneya.db.DBManager;

import java.util.List;

/**
 * Created by Vasily Laushkin <vaslinux@gmail.com> on 25/05/15.
 */
public class BizCardListLoader extends AsyncTaskLoader<List<BizCard>> {
    public static final String TAG = BizCardListLoader.class.getName();

    private List<BizCard> mData;

    public BizCardListLoader(Context ctx) {
        super(ctx);
    }

    @Override
    public void onCanceled(List<BizCard> data) {
        super.onCanceled(data);

        releaseResources(data);
    }

    @Override
    public void deliverResult(List<BizCard> data) {
        if (isReset()) {
            releaseResources(data);
            return;
        }

        List<BizCard> oldData = mData;
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

        DBManager.getInstance().registerObserver(getContext(), this, BizCardListLoader.class);

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
        DBManager.getInstance().unregisterObserver(this, BizCardListLoader.class);
    }

    @Override
    public List<BizCard> loadInBackground() {
        return DBManager.getInstance().readArrayToList(getContext(), Constants.Prefs.PREF_PARAM_BUSINESS_CARDS, BizCard[].class);
    }

    private void releaseResources(List<BizCard> data) {
        // For a simple List, there is nothing to do. For something like a Cursor,
        // we would close it in this method. All resources associated with the
        // Loader should be released here.
    }

    @Override
    public boolean equals(Object object)
    {
        boolean isEqual= false;

        if (object != null && object instanceof BizCardListLoader)
        {
            isEqual = this.TAG.equals( ((BizCardListLoader) object).TAG );
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return this.TAG.hashCode();
    }
}
