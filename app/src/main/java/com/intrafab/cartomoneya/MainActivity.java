package com.intrafab.cartomoneya;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.intrafab.cartomoneya.actions.ActionRequestUsers;
import com.intrafab.cartomoneya.data.User;
import com.telly.groundy.Groundy;
import com.telly.groundy.annotations.OnFailure;
import com.telly.groundy.annotations.OnSuccess;
import com.telly.groundy.annotations.Param;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = MainActivity.class.getName();

    private TextView mButtonBusinessCards;
    private TextView mButtonShoppingCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().getThemedContext();

        getSupportActionBar().setTitle(R.string.app_name);
        showActionBar();

        mButtonBusinessCards = (TextView) findViewById(R.id.btnBusinessCards);
        mButtonShoppingCards = (TextView) findViewById(R.id.btnShoppingCards);

        mButtonBusinessCards.setOnClickListener(this);
        mButtonShoppingCards.setOnClickListener(this);

        // test
        Groundy.create(ActionRequestUsers.class)
                .callback(MainActivity.this)
                //.callbackManager(mCallbacksManager)
                .queueUsing(MainActivity.this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBusinessCards:
                BusinessCardsActivity.launch(MainActivity.this);
                break;
            case R.id.btnShoppingCards:
                ShoppingCardsActivity.launch(MainActivity.this);
                break;
        }
    }

    @OnSuccess(ActionRequestUsers.class)
    public void onSuccessRequestUsers(
            @Param(Constants.Extras.PARAM_USER_DATA) User userData) {
        AppApplication.getApplication(this).setUserInfo(userData);
    }

    @OnFailure(ActionRequestUsers.class)
    public void onFailureRequestUsers(
            @Param(Constants.Extras.PARAM_INTERNET_AVAILABLE) boolean isAvailable) {

    }
}
