package com.intrafab.cartomoneya;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.intrafab.cartomoneya.actions.ActionRequestCreateBusinessCardTask;
import com.intrafab.cartomoneya.actions.ActionRequestShopBrandTask;
import com.intrafab.cartomoneya.actions.ActionRequestUpdateBusinessCardTask;
import com.intrafab.cartomoneya.adapters.CardImagePagerAdapter;
import com.intrafab.cartomoneya.adapters.ShopBrandAdapter;
import com.intrafab.cartomoneya.data.ShopBrand;
import com.intrafab.cartomoneya.data.BusinessCard;
import com.intrafab.cartomoneya.data.User;
import com.intrafab.cartomoneya.loaders.ShopBrandListLoader;
import com.intrafab.cartomoneya.utils.Images;
import com.intrafab.cartomoneya.utils.Logger;
import com.intrafab.cartomoneya.utils.SupportVersion;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.EventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.telly.groundy.CallbacksManager;
import com.telly.groundy.Groundy;
import com.telly.groundy.annotations.OnFailure;
import com.telly.groundy.annotations.OnSuccess;
import com.telly.groundy.annotations.Param;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
/**
 * Created by mikhailzubov on 04.06.15.
 */
public class NewBusinessCardActivity extends BaseActivity
        implements View.OnClickListener
{
    public static final String TAG = NewBusinessCardActivity.class.getName();

    //public static final int REQUEST_CODE_PICK_IMAGE = 200;
    public static final int REQUEST_CODE_CROP_IMAGE = 300;

    private static final int LOADER_SHOP_BRAND_ID = 11;

    public static final String ARG_BUSINESS_CARD = "arg_business_card";

    public static final String ARG_SAVE_FRONT_IMAGE = "arg_save_front_image";
    public static final String ARG_SAVE_BACK_IMAGE = "arg_save_back_image";
    public static final String ARG_SAVE_CURRENT_POSITION = "arg_save_current_position";

    private BusinessCard mBusinessCardEdit;
    private boolean mIsEditMode;

    private MaterialTabHost mTabHost;
    private ViewPager mPager;
    private CardImagePagerAdapter mAdapter;

    private IntentIntegrator mScanIntegrator;

    private MaterialEditText mEditContactName;
    private MaterialEditText mEditContactJobTitle;
    private MaterialEditText mEditContactCompany;
    private MaterialEditText mEditContactCompanyAddress;
    private MaterialEditText mEditContactSite;
    private MaterialEditText mEditContactPhone;
    private MaterialEditText mEditContactMobile;
    private MaterialEditText mEditContactEMail;
    private MaterialEditText mEditContactFax;
    private MaterialEditText mEditContactSkype;
    private MaterialEditText mEditNotes;

    private RelativeLayout mLayoutProgress;

    private Uri mFrontImageUri;
    private Uri mBackImageUri;

    private int mCurrentPosition;

    private ShopBrand mSelectedBrand;

    private CallbacksManager mCallbacksManager;

    private MaterialTabListener mTabListener = new MaterialTabListener() {
        @Override
        public void onTabSelected(MaterialTab materialTab) {
            mPager.setCurrentItem(materialTab.getPosition());
            mCurrentPosition = materialTab.getPosition();
//            mAdapter.update(mPager, mCurrentPosition == 0 ? mFrontImageUri : mBackImageUri, mCurrentPosition, true);
        }

        @Override
        public void onTabReselected(MaterialTab materialTab) {

        }

        @Override
        public void onTabUnselected(MaterialTab materialTab) {

        }
    };

    private android.app.LoaderManager.LoaderCallbacks<List<ShopBrand>> mLoaderCallback = new android.app.LoaderManager.LoaderCallbacks<List<ShopBrand>>() {
        @Override
        public android.content.Loader<List<ShopBrand>> onCreateLoader(int id, Bundle args) {
            switch (id) {
                case LOADER_SHOP_BRAND_ID:
                    return createShopBrandLoader();
                default:
                    return null;
            }
        }

        @Override
        public void onLoadFinished(android.content.Loader<List<ShopBrand>> loader, List<ShopBrand> data) {
            int id = loader.getId();
            switch (id) {
                case LOADER_SHOP_BRAND_ID:
                    finishedShopBrandLoader(data);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onLoaderReset(android.content.Loader<List<ShopBrand>> loader) {
            int id = loader.getId();
            switch (id) {
                //case LOADER_SHOP_BRAND_ID:
                //    resetShopBrandLoader();
                //    break;
                default:
                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_shop_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_create) {
            createNewCard();
            return true;
        } else if (id == R.id.action_save) {
            hideKeyboard();
            saveCard();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem itemCreate = menu.findItem(R.id.action_create);
        MenuItem itemSave = menu.findItem(R.id.action_save);
        if (itemCreate != null) {
            itemCreate.setEnabled(!isProgress());
        }

        if (mIsEditMode) {
            if (itemCreate != null)
                itemCreate.setVisible(false);
            if (itemSave != null)
                itemSave.setVisible(true);
        } else {
            if (itemCreate != null)
                itemCreate.setVisible(true);
            if (itemSave != null)
                itemSave.setVisible(false);
        }
        return true;
    }

    private android.content.Loader<List<ShopBrand>> createShopBrandLoader() {
        Logger.d(TAG, "createShopBrandLoader");
        return new ShopBrandListLoader(NewBusinessCardActivity.this);
    }

    private void finishedShopBrandLoader(List<ShopBrand> data) {
        if (data == null) {
            Logger.d(TAG, "finishedShopBrandLoader start ActionRequestShopBrandTask");
//            if (fragment != null)
//                fragment.showProgress();
            Groundy.create(ActionRequestShopBrandTask.class)
                    .callback(NewBusinessCardActivity.this)
                    .callbackManager(mCallbacksManager)
                    .queueUsing(NewBusinessCardActivity.this);
        } else {
  /*          Logger.d(TAG, "finishedShopBrandLoader setData size = " + data.size());
            final ShopBrandAdapter adapter = new ShopBrandAdapter(this, data);
            mMaterialSpinner.setAdapter(adapter);
            mMaterialSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position >= 0 && position < adapter.getCount())
                        mSelectedBrand = (ShopBrand) adapter.getItem(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
*/
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Logger.e(TAG, "onCreate");

        getSupportActionBar().getThemedContext();

        if (savedInstanceState != null) {
            String pathFrontImage = savedInstanceState.getString(ARG_SAVE_FRONT_IMAGE);
            if (!TextUtils.isEmpty(pathFrontImage)) {
                mFrontImageUri = Uri.parse(pathFrontImage);
            }

            String pathBackImage = savedInstanceState.getString(ARG_SAVE_BACK_IMAGE);
            if (!TextUtils.isEmpty(pathBackImage)) {
                mBackImageUri = Uri.parse(pathBackImage);
            }

            mCurrentPosition = savedInstanceState.getInt(ARG_SAVE_CURRENT_POSITION);
        }

        Intent intent = getIntent();
        if (intent != null) {
            mBusinessCardEdit = getIntent().getParcelableExtra(ARG_BUSINESS_CARD);
            if (mBusinessCardEdit != null) {
                mIsEditMode = true;
            }
        }

        getSupportActionBar().setTitle(mIsEditMode ? R.string.edit_card_screen_header : R.string.new_card_screen_header);
        showActionBar();

        mCallbacksManager = CallbacksManager.init(savedInstanceState);
        mCallbacksManager.linkCallbacks(this);

        mTabHost = (MaterialTabHost) this.findViewById(R.id.tabHost);
        mPager = (ViewPager) this.findViewById(R.id.tabPager);

        //mLayoutBarcode = (RelativeLayout) findViewById(R.id.layoutBarcode);
        //mBarcodeImageView = (ImageView) findViewById(R.id.ivBarcodeImage);
        //mBarcodeAddImageView = (ImageView) findViewById(R.id.ivIconAddBarcode);
        //mBarcodeNumber = (TextView) findViewById(R.id.tvBarcodeNumber);
        //mMaterialSpinner = (MaterialSpinner) findViewById(R.id.brandSpinner);
        mEditContactName     = (MaterialEditText) findViewById(R.id.etContactName);
        mEditContactJobTitle = (MaterialEditText) findViewById(R.id.etContactName);
        mEditContactCompany  = (MaterialEditText) findViewById(R.id.etContactCompanyName);
        mEditContactCompanyAddress =(MaterialEditText) findViewById(R.id.contact_company_address);
        mEditContactSite   = (MaterialEditText) findViewById(R.id.contact_company_website_address);
        mEditContactEMail  = (MaterialEditText) findViewById(R.id.contact_company_email_address);
        mEditContactMobile = (MaterialEditText) findViewById(R.id.contact_company_mobile);
        mEditContactPhone  = (MaterialEditText) findViewById(R.id.contact_company_phone);
        mEditContactFax    = (MaterialEditText) findViewById(R.id.contact_company_fax);
        mEditContactSkype  = (MaterialEditText) findViewById(R.id.contact_company_skype_address);
        mEditNotes         = (MaterialEditText) findViewById(R.id.etNotes);
        mLayoutProgress    = (RelativeLayout) findViewById(R.id.layoutProgress);

        // init view pager
        mAdapter = new CardImagePagerAdapter(getSupportFragmentManager(), mFrontImageUri, mBackImageUri);
        mPager.setOffscreenPageLimit(2);
        mPager.setAdapter(mAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                // when user do a swipe the selected tab change
                mTabHost.setSelectedNavigationItem(position);
                mCurrentPosition = position;
//                mAdapter.update(mPager, mCurrentPosition == 0 ? mFrontImageUri : mBackImageUri, mCurrentPosition, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);

                if (state == ViewPager.SCROLL_STATE_SETTLING) {
                    int position = mPager.getCurrentItem();
                    mTabHost.setSelectedNavigationItem(position);
                    mPager.setCurrentItem(position);
                    mCurrentPosition = position;
                }
            }
        });

        mTabHost.addTab(
                mTabHost.newTab()
                        .setText(getString(R.string.tab_new_card_frot))
                        .setTabListener(mTabListener)
        );
        mTabHost.addTab(
                mTabHost.newTab()
                        .setText(getString(R.string.tab_new_card_back))
                        .setTabListener(mTabListener)
        );

        mTabHost.setSelectedNavigationItem(0);
        mPager.setCurrentItem(0);
        mCurrentPosition = 0;

        getLoaderManager().initLoader(LOADER_SHOP_BRAND_ID, null, mLoaderCallback);

        if (mIsEditMode) {
            mTabHost.post(new Runnable() {
                @Override
                public void run() {
                    fillData();
                }
            });

        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            String pathFrontImage = savedInstanceState.getString(ARG_SAVE_FRONT_IMAGE);
            if (!TextUtils.isEmpty(pathFrontImage)) {
                mFrontImageUri = Uri.parse(pathFrontImage);
            }

            String pathBackImage = savedInstanceState.getString(ARG_SAVE_BACK_IMAGE);
            if (!TextUtils.isEmpty(pathBackImage)) {
                mBackImageUri = Uri.parse(pathBackImage);
            }

            mCurrentPosition = savedInstanceState.getInt(ARG_SAVE_CURRENT_POSITION);
        }
    }

    private void fillData() {
        fillPagerImages();

        if (mIsEditMode) {
          /*  String cardName = mBusinessCardEdit.getName();
            if (!TextUtils.isEmpty(cardName)) {
                mEditCardName.setText(cardName);
            }

            String cardNotes = mBusinessCardEdit.getNotes();
            if (!TextUtils.isEmpty(cardNotes)) {
                mEditNotes.setText(cardNotes);
            }
*/
            invalidateOptionsMenu();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mCallbacksManager.onSaveInstanceState(outState);

        if (mFrontImageUri != null && outState != null) {
            outState.putString(ARG_SAVE_FRONT_IMAGE, mFrontImageUri.getPath());
        }

        if (mBackImageUri != null && outState != null) {
            outState.putString(ARG_SAVE_BACK_IMAGE, mBackImageUri.getPath());
        }

        outState.putInt(ARG_SAVE_CURRENT_POSITION, mCurrentPosition);
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.e(TAG, "NewBusinessCardActivity onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.e(TAG, "NewBusinessCardActivity onDestroy");
        mCallbacksManager.onDestroy();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_new_bcard;
    }

    public static void launch(BaseActivity activity, int requestCode) {
        Intent intent = new Intent(activity, NewBusinessCardActivity.class);

        Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle();

        ActivityCompat.startActivityForResult(activity, intent, requestCode, options);
    }

    public static void launch(BaseActivity activity) {
        Intent intent = new Intent(activity, NewBusinessCardActivity.class);

        Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle();

        ActivityCompat.startActivity(activity, intent, options);
    }

    public static void launchEdit(BaseActivity activity, BusinessCard businessCard, ShopBrand shopBrand, int requestCode) {
        Intent intent = new Intent(activity, NewBusinessCardActivity.class);
        intent.putExtra(ARG_BUSINESS_CARD, businessCard);
       // intent.putExtra(ARG_SHOP_BRAND, shopBrand);

        Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle();

        ActivityCompat.startActivityForResult(activity, intent, requestCode, options);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.e(TAG, "onActivityResult requestCode: " + requestCode);
        Logger.e(TAG, "onActivityResult resultCode: " + resultCode);

//        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
//            Logger.e(TAG, "onActivityResult requestCode REQUEST_CODE_PICK_IMAGE");
//            if (resultCode == Activity.RESULT_OK) {
//                Logger.e(TAG, "onActivityResult resultCode RESULT_OK");
//                Uri imageUri = getPickImageResultUri(data, mPager.getCurrentItem() == 0 ? true : false);
//
//                CropActivity.launch(this, REQUEST_CODE_CROP_IMAGE, imageUri, mPager.getCurrentItem() == 0 ? true : false);
//            }
//        } else
        if (requestCode == REQUEST_CODE_CROP_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    String path = data.getStringExtra(CropActivity.EXTRA_ARG_URI);
                    final Uri imageUri = Uri.parse(path);
                    Logger.e(TAG, "onActivityResult path: " + imageUri.getPath());

                    if (mCurrentPosition == 0) {
                        mFrontImageUri = imageUri;
                    } else {
                        mBackImageUri = imageUri;
                    }

                    mAdapter.update(mPager, imageUri, mCurrentPosition, true);

                }
            }
        } else {
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (scanningResult != null) {
                final String scanContent = scanningResult.getContents();
                final String scanFormat = scanningResult.getFormatName();

                fillBarcode(scanContent, scanFormat);
            }
//            else {
//                Toast.makeText(getApplicationContext(),
//                        "No scan data received!", Toast.LENGTH_SHORT).show();
//            }
        }
    }

    private void fillPagerImages() {
//        Uri frontImagePath = mFrontImageUri != null ? mFrontImageUri : (mIsEditMode ? Uri.parse(mBusinessCardEdit.getFrontImagePath()) : null);
//        Logger.e(TAG, "fillPagerImages frontImagePath: " + (frontImagePath == null ? "NULL" : frontImagePath.getPath()));
//        if (frontImagePath != null) {
//            try {
//                PlaceholderCardImagePageFragment fragment = mAdapter.getFragment(0);
//                if (fragment == null) {
//                    fragment = (PlaceholderCardImagePageFragment) mAdapter.instantiateItem(mPager, 0);
//                    if (fragment != null) {
//                        fragment.setUri(frontImagePath);
//                    }
//                } else {
//                    fragment.setUri(frontImagePath);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        Uri backImagePath = mBackImageUri != null ? mBackImageUri : (mIsEditMode ? Uri.parse(mBusinessCardEdit.getBackImagePath()) : null);
//        Logger.e(TAG, "fillPagerImages backImagePath: " + (backImagePath == null ? "NULL" : backImagePath.getPath()));
//        if (backImagePath != null) {
//            try {
//                PlaceholderCardImagePageFragment fragment = mAdapter.getFragment(1);
//                if (fragment == null) {
//                    fragment = (PlaceholderCardImagePageFragment) mAdapter.instantiateItem(mPager, 1);
//                    if (fragment != null) {
//                        fragment.setUri(backImagePath);
//                    }
//                } else {
//                    fragment.setUri(backImagePath);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
        mPager.post(new Runnable() {
            @Override
            public void run() {
                Uri frontImagePath = mFrontImageUri != null ? mFrontImageUri : (mIsEditMode ? Uri.parse(mBusinessCardEdit.getFrontImagePath()) : null);
                Logger.e(TAG, "fillPagerImages frontImagePath: " + (frontImagePath == null ? "NULL" : frontImagePath.getPath()));
                if (frontImagePath != null) {
                    //mAdapter.setFrontUri(frontImagePath);
                    mAdapter.update(mPager, frontImagePath, 0, true);
//                    PlaceholderCardImagePageFragment fragmentFront = mAdapter.getFragment(0);
//                    if (fragmentFront != null) {
//                        fragmentFront.setUri(frontImagePath, mPager.getCurrentItem() == 0 ? true : false);
//                    }
                }

                Uri backImagePath = mBackImageUri != null ? mBackImageUri : (mIsEditMode ? Uri.parse(mBusinessCardEdit.getBackImagePath()) : null);
                Logger.e(TAG, "fillPagerImages backImagePath: " + (backImagePath == null ? "NULL" : backImagePath.getPath()));
                if (backImagePath != null) {
                    //mAdapter.setFrontUri(backImagePath);
                    mAdapter.update(mPager, backImagePath, 1, true);
//                    PlaceholderCardImagePageFragment fragmentBack = mAdapter.getFragment(1);
//                    if (fragmentBack != null) {
//                        fragmentBack.setUri(mBackImageUri, mPager.getCurrentItem() == 1 ? true : false);
//                    }
                }
            }
        });
        //mPager.invalidate();
    }

    @SuppressWarnings("NewApi")
    private void fillBarcode(final String scanContent, final String scanFormat) {/*
        if (!TextUtils.isEmpty(scanContent)) {
            Bitmap bitmap = null;
            try {
                BarcodeFormat barcodeFormat;
                try {
                    barcodeFormat = BarcodeFormat.valueOf(scanFormat);
                } catch (Exception e) {
                    e.printStackTrace();
                    barcodeFormat = BarcodeFormat.CODE_128;
                }

                bitmap = Images.encodeAsBitmap(scanContent, barcodeFormat, mBarcodeImageView.getWidth(), mBarcodeImageView.getHeight());
                mBarcodeImageView.setImageBitmap(bitmap);
                mBarcodeAddImageView.setVisibility(View.GONE);
                if (SupportVersion.JellyBean())
                    mLayoutBarcode.setBackground(null);
                else
                    mLayoutBarcode.setBackgroundDrawable(null);
            } catch (WriterException e) {
                e.printStackTrace();
                mBarcodeImageView.setImageResource(R.mipmap.ic_barcode);
                mBarcodeAddImageView.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
                mBarcodeImageView.setImageResource(R.mipmap.ic_barcode);
                mBarcodeAddImageView.setVisibility(View.GONE);
            } catch (Throwable e) {
                e.printStackTrace();
                mBarcodeImageView.setImageResource(R.mipmap.ic_barcode);
                mBarcodeAddImageView.setVisibility(View.GONE);
            }

            mBarcodeNumber.setVisibility(View.VISIBLE);
            mBarcodeNumber.setTextScaleX(1.0f);
            mBarcodeNumber.setText(scanContent);
            mBarcodeNumber.setTag(scanFormat);
            Paint textPaint = mBarcodeNumber.getPaint();
            float width = textPaint.measureText(scanContent);
            mBarcodeNumber.setTextScaleX(((float) mBarcodeNumber.getWidth() / width) - 0.3f);
        } else {
            mBarcodeImageView.setImageDrawable(null);
            mBarcodeAddImageView.setVisibility(View.VISIBLE);
            mBarcodeNumber.setTextScaleX(1.0f);
            mBarcodeNumber.setText("");
            mBarcodeNumber.setVisibility(View.GONE);
        }*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBarcodeImage:
            case R.id.ivIconAddBarcode:
                if (mScanIntegrator == null)
                    mScanIntegrator = new IntentIntegrator(this);
                mScanIntegrator.initiateScan();
                break;
        }
    }

    /**
     * Create a chooser intent to select the source to get image from.<br/>
     * The source can be camera's (ACTION_IMAGE_CAPTURE) or gallery's (ACTION_GET_CONTENT).<br/>
     * All possible sources are added to the intent chooser.
     */
    public Intent getPickImageChooserIntent(boolean isFront) {

        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri(isFront);

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // the main intent is the last in the list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    /**
     * Get URI to image received from capture by camera.
     */
    private Uri getCaptureImageOutputUri(boolean isFront) {
        Logger.e(TAG, "getCaptureImageOutputUri start");
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();

        if (getImage != null) {
            if (!getImage.exists()) {
                getImage.mkdirs();
            }
            File imageFile = new File(getImage.getPath(), isFront ? "pickFrontImageResult.jpeg" : "pickBackImageResult.jpeg");
            if (!imageFile.exists()) {
                try {
                    imageFile.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Logger.e(TAG, "getCaptureImageOutputUri imageFile: " + imageFile.getPath());
            outputFileUri = Uri.fromFile(imageFile);
            Logger.e(TAG, "getCaptureImageOutputUri outputFileUri: " + outputFileUri.getPath());
        }
        return outputFileUri;
    }

    public Uri getPickImageResultUri(Intent data, boolean isFront) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? getCaptureImageOutputUri(isFront) : data.getData();
    }

    private void createNewCard() {
        String cardName = validateCardName();
        if (TextUtils.isEmpty(cardName)) {
            showSnackBarError(getString(R.string.error_need_input_name));
            return;
        }

        SnackbarManager.dismiss();

        //String barcode = mBarcodeNumber.getText().toString();
       // String barcodeFormat = (String) mBarcodeNumber.getTag();

        BusinessCard newCard = new BusinessCard();
        BusinessCard.Personage newPersonage = new BusinessCard.Personage();

        newCard.setName(cardName + "Business Card");

        String contactName     = mEditContactName.getText().toString();
        String contactJobTitle = mEditContactJobTitle.getText().toString();
        String contactCompany  = mEditContactCompany.getText().toString();
        String contactCompanyAddress = mEditContactCompanyAddress.getText().toString();
        String contactSite   = mEditContactSite.getText().toString();
        String contactPhone  = mEditContactPhone.getText().toString();
        String contactMobile = mEditContactMobile.getText().toString();
        String contactEMail  = mEditContactEMail.getText().toString();
        String contactFax    = mEditContactFax.getText().toString();
        String contactSkype  = mEditContactSkype.getText().toString();

        newPersonage.setName(contactName);
        newPersonage.setJobTitle(contactJobTitle);
        newPersonage.setCompany(contactCompany);
        newPersonage.setCompanyAddress(contactCompanyAddress);
        newPersonage.setCompanySiteAddress(contactSite);
        newPersonage.setCellPhone(contactMobile);
        newPersonage.setWorkPhone(contactPhone);
        newPersonage.setFax(contactFax);
        newPersonage.setEmail(contactEMail);
        newPersonage.setSkype(contactSkype);

        newCard.setPersonage(newPersonage);

        String notes = mEditNotes.getText().toString();
        newCard.setNotes(notes);

        User user = AppApplication.getApplication(this).getUserInfo();
        if (user != null)
            newCard.setBelongsToUser(user.getId());

        showProgress();
        invalidateOptionsMenu();
        Groundy.create(ActionRequestCreateBusinessCardTask.class)
                .callback(NewBusinessCardActivity.this)
                .callbackManager(mCallbacksManager)
                .arg(ActionRequestCreateBusinessCardTask.ARG_BACK_IMAGE, mBackImageUri != null ? mBackImageUri.getPath() : "")
                .arg(ActionRequestCreateBusinessCardTask.ARG_FRONT_IMAGE, mFrontImageUri != null ? mFrontImageUri.getPath() : "")
                .arg(ActionRequestCreateBusinessCardTask.ARG_BUSINESS_CARD, newCard)
                .queueUsing(NewBusinessCardActivity.this);
    }

    private void saveCard() {
        String cardName = validateCardName();
        if (TextUtils.isEmpty(cardName)) {
            showSnackBarError(getString(R.string.error_need_input_name));
            return;
        }

        SnackbarManager.dismiss();

        BusinessCard newCard = null;
        if (mIsEditMode) {
            newCard = mBusinessCardEdit;
        } else {
            newCard = new BusinessCard();
        }

        BusinessCard.Personage newPersonage = new BusinessCard.Personage();

        newCard.setName(cardName + "Business Card");

        String contactName     = mEditContactName.getText().toString();
        String contactJobTitle = mEditContactJobTitle.getText().toString();
        String contactCompany  = mEditContactCompany.getText().toString();
        String contactCompanyAddress = mEditContactCompanyAddress.getText().toString();
        String contactSite   = mEditContactSite.getText().toString();
        String contactPhone  = mEditContactPhone.getText().toString();
        String contactMobile = mEditContactMobile.getText().toString();
        String contactEMail  = mEditContactEMail.getText().toString();
        String contactFax    = mEditContactFax.getText().toString();
        String contactSkype  = mEditContactSkype.getText().toString();

        newPersonage.setName(contactName);
        newPersonage.setJobTitle(contactJobTitle);
        newPersonage.setCompany(contactCompany);
        newPersonage.setCompanyAddress(contactCompanyAddress);
        newPersonage.setCompanySiteAddress(contactSite);
        newPersonage.setCellPhone(contactMobile);
        newPersonage.setWorkPhone(contactPhone);
        newPersonage.setFax(contactFax);
        newPersonage.setEmail(contactEMail);
        newPersonage.setSkype(contactSkype);

        newCard.setPersonage(newPersonage);

        String notes = mEditNotes.getText().toString();
        newCard.setNotes(notes);

        User user = AppApplication.getApplication(this).getUserInfo();
        if (user != null)
            newCard.setBelongsToUser(user.getId());

        showProgress();
        invalidateOptionsMenu();

        Groundy.create(ActionRequestUpdateBusinessCardTask.class)
                .callback(NewBusinessCardActivity.this)
                .callbackManager(mCallbacksManager)
                .arg(ActionRequestCreateBusinessCardTask.ARG_BACK_IMAGE, mBackImageUri != null ? mBackImageUri.getPath() : "")
                .arg(ActionRequestCreateBusinessCardTask.ARG_FRONT_IMAGE, mFrontImageUri != null ? mFrontImageUri.getPath() : "")
                .arg(ActionRequestCreateBusinessCardTask.ARG_BUSINESS_CARD, newCard)
                .queueUsing(NewBusinessCardActivity.this);
    }

    private String validateCardName() {
        String contactName = mEditContactName.getText().toString();
        if (TextUtils.isEmpty(contactName))
            return null;

        return contactName;
    }

    private void showSnackBarError(String error) {
        SnackbarManager.show(
                Snackbar.with(NewBusinessCardActivity.this) // context
                        .type(SnackbarType.MULTI_LINE)
                        .text(error)
                        .color(getResources().getColor(R.color.colorLightError))
                        .textColor(getResources().getColor(R.color.colorLightEditTextHint))
                        .swipeToDismiss(true)
                        .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                , NewBusinessCardActivity.this); // activity where it is displayed
    }

    private void showSnackBarSuccess(String text) {
        SnackbarManager.show(
                Snackbar.with(NewBusinessCardActivity.this) // context
                        .type(SnackbarType.MULTI_LINE)
                        .text(text)
                        .color(getResources().getColor(R.color.colorLightSuccess))
                        .textColor(getResources().getColor(R.color.colorLightEditTextHint))
                        .swipeToDismiss(false)
                        .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                        .eventListener(new EventListener() {
                            @Override
                            public void onShow(Snackbar snackbar) {

                            }

                            @Override
                            public void onShowByReplace(Snackbar snackbar) {

                            }

                            @Override
                            public void onShown(Snackbar snackbar) {

                            }

                            @Override
                            public void onDismiss(Snackbar snackbar) {
                                if (mIsEditMode) {
                                    Intent intentResult = new Intent();
                                    intentResult.putExtra(ARG_BUSINESS_CARD, mBusinessCardEdit);
                                    setResult(RESULT_OK, intentResult);
                                }
                                finish();
                            }

                            @Override
                            public void onDismissByReplace(Snackbar snackbar) {
                                if (mIsEditMode) {
                                    Intent intentResult = new Intent();
                                    intentResult.putExtra(ARG_BUSINESS_CARD, mBusinessCardEdit);
                                    setResult(RESULT_OK, intentResult);
                                }
                                finish();
                            }

                            @Override
                            public void onDismissed(Snackbar snackbar) {
                                if (mIsEditMode) {
                                    Intent intentResult = new Intent();
                                    intentResult.putExtra(ARG_BUSINESS_CARD, mBusinessCardEdit);
                                    setResult(RESULT_OK, intentResult);
                                }
                                finish();
                            }
                        })
                , NewBusinessCardActivity.this); // activity where it is displayed
    }

    public boolean isProgress() {
        return mLayoutProgress.getVisibility() == View.VISIBLE;
    }

    public void showProgress() {
        mLayoutProgress.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        mLayoutProgress.setVisibility(View.GONE);
    }

    @OnSuccess(ActionRequestCreateBusinessCardTask.class)
    public void onSuccessRequestCreateBusinessCard() {
        hideProgress();
        invalidateOptionsMenu();

        showSnackBarSuccess(getResources().getString(R.string.success_create_shop_card));

        mFrontImageUri = null;
        mBackImageUri = null;
    }

    @OnFailure(ActionRequestCreateBusinessCardTask.class)
    public void onFailureRequestCreateBusinessCard(@Param(Constants.Extras.PARAM_INTERNET_AVAILABLE) boolean isAvailable) {
        hideProgress();
        invalidateOptionsMenu();

        if (!isAvailable) {
            showSnackBarError(getResources().getString(R.string.error_internet_not_available));
        } else {
            showSnackBarError(getResources().getString(R.string.error_occurred));
        }
    }

    @OnSuccess(ActionRequestUpdateBusinessCardTask.class)
    public void onSuccessRequestUpdateBusinessCard(@Param(Constants.Extras.PARAM_BUSINESS_CARD) BusinessCard createdCard) {
        mBusinessCardEdit = createdCard;
        hideProgress();
        invalidateOptionsMenu();

        showSnackBarSuccess(getResources().getString(R.string.success_update_shop_card));
        mFrontImageUri = null;
        mBackImageUri = null;
    }

    @OnFailure(ActionRequestUpdateBusinessCardTask.class)
    public void onFailureRequestUpdateBusinessCard(@Param(Constants.Extras.PARAM_INTERNET_AVAILABLE) boolean isAvailable) {
        hideProgress();
        invalidateOptionsMenu();

        if (!isAvailable) {
            showSnackBarError(getResources().getString(R.string.error_internet_not_available));
        } else {
            showSnackBarError(getResources().getString(R.string.error_occurred));
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditNotes.getWindowToken(), 0);
    }

}
