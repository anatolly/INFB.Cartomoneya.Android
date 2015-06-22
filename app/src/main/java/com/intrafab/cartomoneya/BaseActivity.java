package com.intrafab.cartomoneya;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.intrafab.cartomoneya.utils.Connectivity;

/**
 * Created by Artemiy Terekhov on 06.05.2015.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected Toolbar toolbar;
    protected android.support.v7.app.ActionBar bar;

    private BroadcastReceiver mNetworkReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || context == null)
                return;

            Connectivity.updateNetworkState(context);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getActivityTheme());
        setContentView(getLayoutResource());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
            bar = getSupportActionBar();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            registerReceiver(mNetworkReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract int getLayoutResource();
    protected int getActivityTheme() {
        return R.style.AppShopTheme;
    }

    protected void setActionBarIcon(int iconRes) {
        if (toolbar != null)
            toolbar.setNavigationIcon(iconRes);
    }

    protected void hideActionBar() {
        if (toolbar != null) {
            toolbar.setVisibility(View.GONE);
        }
    }

    protected void showActionBar() {
        if (toolbar != null) {
            toolbar.setVisibility(View.VISIBLE);
            bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorLightPrimary)));
        }
    }

    protected void showTransparentActionBar() {
        if (toolbar != null) {
            toolbar.setVisibility(View.VISIBLE);
            bar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}
