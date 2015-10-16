package com.intrafab.cartomoneya;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.intrafab.cartomoneya.nfc.CardEmulationFragment;
import com.intrafab.cartomoneya.utils.Logger;
import com.telly.groundy.CallbacksManager;
import com.telly.groundy.Groundy;

import java.util.List;

/**
 * Created by Artemiy Terekhov on 06.05.2015.
 */
public class NfcCardsActivity extends BaseActivity  {

    public static final String TAG = NfcCardsActivity.class.getName();

    private CallbacksManager mCallbacksManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().getThemedContext();

        getSupportActionBar().setTitle(R.string.nfc_card_screen_header);
        showActionBar();

        mCallbacksManager = CallbacksManager.init(savedInstanceState);
        mCallbacksManager.linkCallbacks(this);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            CardEmulationFragment fragment = new CardEmulationFragment();
            transaction.replace(R.id.container, fragment);
            transaction.commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_biz_card, menu);
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
//        }

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
        Intent intent = new Intent(activity, NfcCardsActivity.class);

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
/*
    @Override
    public void onClickItem(BusinessCardPopulated itemShopCard) {
        BusinessCardDetailActivity.launch(this, itemShopCard);
    } */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*
        if ( requestCode == REQUEST_CODE_NEW_BCARD) {
            PlaceholderBizCardsFragment fragment = getFragment();
            if (fragment == null)
                return;

            if (fragment != null) {
                fragment.hideProgress();
                fragment.setData(null);
            }
            DBManager.getInstance().deleteObject(Constants.Prefs.PREF_PARAM_BUSINESS_CARDS_POPULATED, BizCardPopulatedListLoader.class);
        } */
    }

}
