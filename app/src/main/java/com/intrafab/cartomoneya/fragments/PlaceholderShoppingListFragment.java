package com.intrafab.cartomoneya.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.intrafab.cartomoneya.AppApplication;
import com.intrafab.cartomoneya.R;
import com.intrafab.cartomoneya.adapters.ShoppingListItemAdapter;
import com.intrafab.cartomoneya.data.ShoppingListItem;
import com.intrafab.cartomoneya.widgets.EmptyRecyclerView;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.List;

/**
 * Created by mono on 04/06/15.
 */
public class PlaceholderShoppingListFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = PlaceholderShoppingListFragment.class.getName();

    private EmptyRecyclerView mRecyclerView;
    private LinearLayout mEmptyLayout;
    private ProgressWheel mProgress;

    private ShoppingListItemAdapter mAdapter;
    private ShoppingListItemAdapter.OnClickListener mListener;
    private EditText mEtNewItem;
    private ImageView mIvNewItem;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (ShoppingListItemAdapter.OnClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement ShoppingListItemAdapter.OnClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        setData(null);
        mListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new ShoppingListItemAdapter(mListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        mRecyclerView = (EmptyRecyclerView) rootView.findViewById(R.id.listViews);
        mEmptyLayout = (LinearLayout) rootView.findViewById(R.id.layoutEmptyList);
        mProgress = (ProgressWheel) rootView.findViewById(R.id.progress_wheel);
        mEtNewItem = (EditText) rootView.findViewById(R.id.etNewItem);
        mIvNewItem = (ImageView) rootView.findViewById(R.id.ivNewItem);

        mIvNewItem.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setEmptyView(mEmptyLayout);
    }

    public void setData(List<ShoppingListItem> data) {
        mAdapter.clear();
        mAdapter.addAll(data);
    }

    private void showEmptyView(boolean isShow) {
        int count = mAdapter.getItemCount();
        if (count <= 0 && isShow)
            mEmptyLayout.setVisibility(View.VISIBLE);
        else
            mEmptyLayout.setVisibility(View.GONE);
    }

    public boolean isProgress() {
        return mProgress.getVisibility() == View.VISIBLE;
    }

    public void showProgress() {
        mProgress.setVisibility(View.VISIBLE);
        showEmptyView(false);
    }

    public void hideProgress() {
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivNewItem) {
            if (mListener != null) {
                ShoppingListItem item = new ShoppingListItem();
                item.setName(mEtNewItem.getText().toString());
                item.setGroup("Общий");
                item.setBelongsToUser(AppApplication.getApplication(view.getContext()).getUserInfo().getId());
                item.setDone(false);

                hideKeyboard();

                mEtNewItem.setText("");

                mListener.onNewItemCreated(item);
            }
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEtNewItem.getWindowToken(), 0);
    }
}
