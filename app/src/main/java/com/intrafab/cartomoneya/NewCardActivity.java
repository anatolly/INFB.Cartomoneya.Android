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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.intrafab.cartomoneya.adapters.CardImagePagerAdapter;
import com.intrafab.cartomoneya.fragments.PlaceholderCardImagePageFragment;
import com.intrafab.cartomoneya.utils.Images;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    private MaterialTabHost mTabHost;
    private ViewPager mPager;
    private CardImagePagerAdapter mAdapter;
    private ImageView mBarcodeImageView;
    private ImageView mBarcodeAddImageView;
    private TextView mBarcodeNumber;

    private IntentIntegrator mScanIntegrator;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().getThemedContext();

        getSupportActionBar().setTitle(R.string.new_card_screen_header);
        showActionBar();

        mTabHost = (MaterialTabHost) this.findViewById(R.id.tabHost);
        mPager = (ViewPager) this.findViewById(R.id.tabPager);

        mBarcodeImageView = (ImageView) findViewById(R.id.ivBarcodeImage);
        mBarcodeAddImageView = (ImageView) findViewById(R.id.ivIconAddBarcode);
        mBarcodeNumber = (TextView) findViewById(R.id.tvBarcodeNumber);

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
        if (resultCode == Activity.RESULT_OK && requestCode == 200) {
            Uri imageUri = getPickImageResultUri(data, true);

            mAdapter.getFragment(mPager.getCurrentItem()).setUri(imageUri);
        }

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
                mBarcodeNumber.setTextScaleX(((float) mBarcodeNumber.getMeasuredWidth() / width) - 0.2f);
            } else {
                mBarcodeImageView.setImageDrawable(null);
                mBarcodeAddImageView.setVisibility(View.VISIBLE);
                mBarcodeNumber.setText("");
                mBarcodeNumber.setVisibility(View.GONE);

                Toast.makeText(getApplicationContext(),
                        "No scan data received!", Toast.LENGTH_SHORT).show();
            }
        } else{
            Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCardImageClick() {
        startActivityForResult(getPickImageChooserIntent(true), 200);
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
}
