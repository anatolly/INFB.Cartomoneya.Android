package com.intrafab.cartomoneya;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.intrafab.cartomoneya.actions.ActionSaveMeTask;
import com.intrafab.cartomoneya.data.User;
import com.intrafab.cartomoneya.utils.Logger;
import com.telly.groundy.CallbacksManager;
import com.telly.groundy.Groundy;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Artemiy Terekhov on 06.05.2015.
 */
public class AppApplication extends MultiDexApplication {

    public static final String CARTOMONEYA_SHARED = "CArTOMONEYA_APP";

    private User userInfo;
    private static String token;

    public User getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(User userInfo) {
        this.userInfo = userInfo;
    }

    public void setUserInfo(Context context, CallbacksManager callbacksManager, User userInfo) {
        this.userInfo = userInfo;

        if (userInfo != null) {
            Groundy.create(ActionSaveMeTask.class)
                    .callback(context)
                    .callbackManager(callbacksManager)
                    .arg(ActionSaveMeTask.USER_DATA, userInfo)
                    .queueUsing(context);
        }
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        AppApplication.token = token;
    }

    @Override
    public void onCreate() {
        initCalligraphy();

        Logger.setApplicationTag("Cartomoneya");
        Logger.setRelease(Constants.RELEASE_MODE);

        //test
        userInfo = new User();
        userInfo.setId(1);
    }

//    public static Context getContext() {
//        return getContext();
//    }

    public static AppApplication getApplication(Context context) {
        if (context instanceof AppApplication) {
            return (AppApplication) context;
        }
        return (AppApplication) context.getApplicationContext();
    }

    private static void initCalligraphy() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(CARTOMONEYA_SHARED, MODE_PRIVATE);
        token = prefs.getString("isLogin", "");
        return !TextUtils.isEmpty(token);
    }

    public static void setLogin(Context context, String token) {
        setToken(token);
        SharedPreferences prefs = context.getSharedPreferences(CARTOMONEYA_SHARED, MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("isLogin", token);
        edit.commit();
    }
}
