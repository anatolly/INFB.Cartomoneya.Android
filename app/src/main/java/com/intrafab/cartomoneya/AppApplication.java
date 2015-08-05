package com.intrafab.cartomoneya;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.intrafab.cartomoneya.actions.ActionSaveMeTask;
import com.intrafab.cartomoneya.data.User;
import com.intrafab.cartomoneya.ocr.RecognitionContext;
import com.intrafab.cartomoneya.utils.Logger;
import com.telly.groundy.CallbacksManager;
import com.telly.groundy.Groundy;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


/****** ABBY OCR Related *********/

import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.abbyy.mobile.ocr4.AssetDataSource;
import com.abbyy.mobile.ocr4.DataSource;
import com.abbyy.mobile.ocr4.Engine;
import com.abbyy.mobile.ocr4.FileLicense;
import com.abbyy.mobile.ocr4.License;
import com.abbyy.mobile.ocr4.RecognitionLanguage;
import com.intrafab.cartomoneya.ocr.utils.PreferenceUtils;

/***************************/

/**
 * Created by Artemiy Terekhov on 06.05.2015.
 */
public class AppApplication extends MultiDexApplication {

    /** Logging tag. */
    private static final String TAG = "Cartomoneya";

    private static final String _licenseFile = "license";
    private static final String _applicationID = "Android_ID";

    private static final String _patternsFileExtension = ".mp3";
    private static final String _dictionariesFileExtension = ".mp3";
    private static final String _keywordsFileExtension = ".mp3";

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

        Resources res = getResources();
        boolean enableOfflineOCR =  res.getBoolean(R.bool.enableOfflineOCR);

        if (! enableOfflineOCR) return;

        // Write default settings to the settings store. These values will be written only during the first
        // startup or
        // if the values are rubbed.

        PreferenceManager.setDefaultValues( this, R.xml.preferences, true );

        final DataSource assetDataSrouce = new AssetDataSource( this.getAssets() );

        final List<DataSource> dataSources = new ArrayList<DataSource>();
        dataSources.add( assetDataSrouce );

        Engine.loadNativeLibrary();
        try {
            Engine.createInstance( dataSources, new FileLicense( assetDataSrouce,
                            AppApplication._licenseFile, AppApplication._applicationID ),
                    new Engine.DataFilesExtensions( AppApplication._patternsFileExtension,
                            AppApplication._dictionariesFileExtension,
                            AppApplication._keywordsFileExtension ) );

            RecognitionContext.createInstance( this );

            filterRecognitionLanguagesPreferences( RecognitionContext.getLanguagesAvailableForOcr(),
                    getString( R.string.key_recognition_languages_ocr ) );
            filterRecognitionLanguagesPreferences( RecognitionContext.getLanguagesAvailableForBcr(),
                    getString( R.string.key_recognition_languages_bcr ) );
        } catch( final IOException e ) {
        } catch( final License.BadLicenseException e ) {
        }
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

    @Override
    public void onTerminate() {

        Resources res = getResources();
        boolean enableOfflineOCR =  res.getBoolean(R.bool.enableOfflineOCR);

        if ( enableOfflineOCR )  {
            Log.v( AppApplication.TAG, "onTerminate()" );
            try {
              Engine.destroyInstance();
              RecognitionContext.destroyInstance();
                } catch( final IllegalStateException e ) {
              Log.e( AppApplication.TAG, "onTerminate failed", e );
            }
        }

        super.onTerminate();
    }

    public void filterRecognitionLanguagesPreferences( final Set<RecognitionLanguage> availableLanguages,
                                                       final String preferenceKey ) {
        final Set<RecognitionLanguage> languages =
                PreferenceUtils.getRecognitionLanguages( this, preferenceKey );
        languages.retainAll( availableLanguages );
        PreferenceUtils.setRecognitionLanguages( this, preferenceKey, languages );
    }
}
