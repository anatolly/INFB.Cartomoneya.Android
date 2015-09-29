package com.intrafab.cartomoneya;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.abbyy.mobile.ocr4.License;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.intrafab.cartomoneya.utils.Connectivity;

/**
 * Created by Artemiy Terekhov on 06.05.2015.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected Toolbar toolbar;
    protected android.support.v7.app.ActionBar bar;

    /** It is forbidden to redefine in child Activities dialog with dialogID = DIALOG_BAD_LICENSE */
    private static final int DIALOG_BAD_LICENSE = 100500;

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
        Resources res = getResources();
        boolean enableOfflineOCR =  res.getBoolean(R.bool.enableOfflineOCR);

        if (! enableOfflineOCR) return;

        if( enableOfflineOCR && !License.isLoaded() ) {
            dispatchBadLicense();
        }

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
//            bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorLightPrimary)));
        }
    }

    protected void showTransparentActionBar() {
        if (toolbar != null) {
            toolbar.setVisibility(View.VISIBLE);
            bar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    protected Dialog onCreateDialog( final int dialogId ) {
        switch ( dialogId ) {
            case DIALOG_BAD_LICENSE:
                return new AlertDialog.Builder( this )
                        .setCancelable( false )
                        .setTitle( getString( R.string.dialog_error ) )
                        .setMessage( getString( R.string.error_bad_license ) )
                        .setPositiveButton( getString( R.string.button_close ),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick( final DialogInterface dialog, final int id ) {
                                        BaseActivity.this.finish();
                                    }
                                } ).create();
            default:
                return super.onCreateDialog( dialogId );
        }
    }

    private void dispatchBadLicense() {
        showDialog( BaseActivity.DIALOG_BAD_LICENSE );
    }
    @Override
    public boolean onSearchRequested() {
        return false;
    }
}
