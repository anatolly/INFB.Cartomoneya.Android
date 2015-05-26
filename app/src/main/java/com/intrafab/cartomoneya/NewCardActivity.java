package com.intrafab.cartomoneya;

import android.app.Activity;
import android.content.ComponentName;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.intrafab.cartomoneya.actions.ActionRequestShopBrandTask;
import com.intrafab.cartomoneya.adapters.CardImagePagerAdapter;
import com.intrafab.cartomoneya.data.ShopBrand;
import com.intrafab.cartomoneya.fragments.PlaceholderCardImagePageFragment;
import com.intrafab.cartomoneya.loaders.ShopBrandListLoader;
import com.intrafab.cartomoneya.utils.Images;
import com.intrafab.cartomoneya.utils.Logger;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.telly.groundy.CallbacksManager;
import com.telly.groundy.Groundy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

/**
 * Created by Artemiy Terekhov on 07.05.2015.
 */
public class NewCardActivity extends BaseActivity
        implements PlaceholderCardImagePageFragment.onClickListener,
        View.OnClickListener {

    public static final String TAG = NewCardActivity.class.getName();

    public static final int REQUEST_CODE_PICK_IMAGE = 200;
    public static final int REQUEST_CODE_CROP_IMAGE = 300;

    private static final int LOADER_SHOP_BRAND_ID = 11;

    private MaterialTabHost mTabHost;
    private ViewPager mPager;
    private CardImagePagerAdapter mAdapter;
    private ImageView mBarcodeImageView;
    private ImageView mBarcodeAddImageView;
    private TextView mBarcodeNumber;

    private IntentIntegrator mScanIntegrator;

    private MaterialSpinner mMaterialSpinner;

    private Uri mFrontImageUri;
    private Uri mBackImageUri;

    private CallbacksManager mCallbacksManager;

    private MaterialTabListener mTabListener = new MaterialTabListener() {
        @Override
        public void onTabSelected(MaterialTab materialTab) {
            mPager.setCurrentItem(materialTab.getPosition());
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
                case LOADER_SHOP_BRAND_ID:
                    resetShopBrandLoader();
                    break;
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
        }
        return super.onOptionsItemSelected(item);
    }

    private android.content.Loader<List<ShopBrand>> createShopBrandLoader() {
        Logger.d(TAG, "createShopBrandLoader");
        return new ShopBrandListLoader(NewCardActivity.this);
    }

    private void finishedShopBrandLoader(List<ShopBrand> data) {
        if (data == null) {
            Logger.d(TAG, "finishedShopBrandLoader start ActionRequestShopBrandTask");
//            if (fragment != null)
//                fragment.showProgress();
            Groundy.create(ActionRequestShopBrandTask.class)
                    .callback(NewCardActivity.this)
                    .callbackManager(mCallbacksManager)
                    .queueUsing(NewCardActivity.this);
        } else {
            Logger.d(TAG, "finishedShopBrandLoader setData size = " + data.size());
//            ArrayAdapter<ShopBrand> adapter = new ArrayAdapter<ShopBrand>(this, android.R.layout.simple_spinner_item, data);
            List<String> items = new ArrayList<String>();
            for (ShopBrand brand : data)
                items.add(brand.getName());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mMaterialSpinner.setAdapter(adapter);
        }
    }

    private void resetShopBrandLoader() {
        Logger.d(TAG, "resetShopBrandLoader");

        mMaterialSpinner.setAdapter(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().getThemedContext();

        getSupportActionBar().setTitle(R.string.new_card_screen_header);
        showActionBar();

        mCallbacksManager = CallbacksManager.init(savedInstanceState);
        mCallbacksManager.linkCallbacks(this);

        mTabHost = (MaterialTabHost) this.findViewById(R.id.tabHost);
        mPager = (ViewPager) this.findViewById(R.id.tabPager);

        mBarcodeImageView = (ImageView) findViewById(R.id.ivBarcodeImage);
        mBarcodeAddImageView = (ImageView) findViewById(R.id.ivIconAddBarcode);
        mBarcodeNumber = (TextView) findViewById(R.id.tvBarcodeNumber);
        mMaterialSpinner = (MaterialSpinner) findViewById(R.id.brandSpinner);

        mBarcodeImageView.setOnClickListener(this);
        mBarcodeAddImageView.setOnClickListener(this);

        // init view pager
        mAdapter = new CardImagePagerAdapter(getSupportFragmentManager());
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
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);

                if (state == ViewPager.SCROLL_STATE_SETTLING) {
                    int position = mPager.getCurrentItem();
                    mTabHost.setSelectedNavigationItem(position);
                    mPager.setCurrentItem(position);
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

        getLoaderManager().initLoader(LOADER_SHOP_BRAND_ID, null, mLoaderCallback);
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

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_new_card;
    }

    public static void launch(BaseActivity activity) {
        Intent intent = new Intent(activity, NewCardActivity.class);

        Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle();

        ActivityCompat.startActivity(activity, intent, options);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri imageUri = getPickImageResultUri(data, mPager.getCurrentItem() == 0 ? true : false);

                    CropActivity.launch(this, REQUEST_CODE_CROP_IMAGE, imageUri, mPager.getCurrentItem() == 0 ? true : false);
                }
            }
        } else if (requestCode == REQUEST_CODE_CROP_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    String path = data.getStringExtra(CropActivity.EXTRA_ARG_URI);
                    Uri imageUri = Uri.parse(path);
                    Logger.e(TAG, "onActivityResult path: " + imageUri.getPath());

                    if (mPager.getCurrentItem() == 0) {
                        mFrontImageUri = imageUri;
                    } else {
                        mBackImageUri = imageUri;
                    }
                    mAdapter.getFragment(mPager.getCurrentItem()).setUri(imageUri);
                }
            }
        } else {
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (scanningResult != null) {
                final String scanContent = scanningResult.getContents();
                final String scanFormat = scanningResult.getFormatName();

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
                    mBarcodeNumber.setText(scanContent);
                    mBarcodeNumber.requestLayout();
                    Paint textPaint = mBarcodeNumber.getPaint();
                    float width = textPaint.measureText(scanContent);
                    mBarcodeNumber.setTextScaleX(((float) mBarcodeImageView.getMeasuredWidth() / width) - 0.5f);
                } else {
                    mBarcodeImageView.setImageDrawable(null);
                    mBarcodeAddImageView.setVisibility(View.VISIBLE);
                    mBarcodeNumber.setText("");
                    mBarcodeNumber.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(),
                            "No scan data received!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(),
                        "No scan data received!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCardImageClick() {
        startActivityForResult(getPickImageChooserIntent(true), REQUEST_CODE_PICK_IMAGE);
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
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), isFront ? "pickFrontImageResult.jpeg" : "pickBackImageResult.jpeg"));
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
        if (!validateImage()) {
            showSnackBarError(getString(R.string.error_need_front_image));
            return;
        }

        if (!validateBrand()) {
            showSnackBarError(getString(R.string.error_need_brand));
            return;
        }

        SnackbarManager.dismiss();
    }

    private boolean validateImage() {
        if (mFrontImageUri != null && new File(mFrontImageUri.getPath()).exists())
            return true;

        return false;
    }

    private boolean validateBrand() {
        if (mMaterialSpinner.getSelectedItem() == null)
            return false;
        String brand = mMaterialSpinner.getSelectedItem().toString();
        if (TextUtils.isEmpty(brand))
            return false;

        if (getString(R.string.select_brand).equals(brand))
            return false;

        return true;
    }

    private void showSnackBarError(String error) {
        SnackbarManager.show(
                Snackbar.with(NewCardActivity.this) // context
                        .type(SnackbarType.MULTI_LINE)
                        .text(error)
                        .color(getResources().getColor(R.color.colorLightError))
                        .textColor(getResources().getColor(R.color.colorLightEditTextHint))
                        .swipeToDismiss(true)
                        .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                , NewCardActivity.this); // activity where it is displayed
    }
}
