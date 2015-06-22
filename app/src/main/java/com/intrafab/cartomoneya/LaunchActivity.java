package com.intrafab.cartomoneya;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Artemiy Terekhov on 22.06.2015.
 */
public class LaunchActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AppApplication.isLoggedIn(this)) {
            finish();
            MainActivity.launch(this);
        } else {
            finish();
            LoginActivity.launch(this);
        }
    }
}
