package com.intrafab.cartomoneya;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.intrafab.cartomoneya.adapters.CardPageAdapter;
import com.intrafab.cartomoneya.data.BusinessCard;
import com.intrafab.cartomoneya.data.BusinessCardPopulated;
import com.intrafab.cartomoneya.data.Personage;
import com.intrafab.cartomoneya.fragments.PlaceholderCardPageFragment;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Vasily Laushkin <vaslinux@gmail.com> on 24/05/15.
 */
public class BusinessCardDetailActivity extends BaseActivity {

    public static final String TAG = ShopCardDetailActivity.class.getName();
    public static final String EXTRA_PARAM_BIZ_CARD = "param_biz_card";
    private static final int NUM_PAGES = 2;

    private BusinessCardPopulated mBusinessCard;
    private ViewPager mViewpager;
    private CircleIndicator mIndicator;
    private TextView mNotesText;
    private CardPageAdapter mPagerAdapter;
    private TextView mTvName;
    private TextView mTvJobTitle;
    private TextView mTvCompany;
    private TextView mTvEMail;
    private TextView mTvAddress;
    private TextView mTvWebSite;
    private TextView mTvWorkPhone;
    private TextView mTvCellPhone;
    private TextView mTvFAX;
    private TextView mTvSkype;
    private TextView mTvEMailLable;
    private TextView mTvAddressLable;
    private TextView mTvWebSiteLable;
    private TextView mTvWorkPhoneLabel;
    private TextView mTvCellPhoneLabel;
    private TextView mTvFAXLable;
    private TextView mTvSkypeLabel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().getThemedContext();

        Intent intent = getIntent();
        if (intent != null) {
            mBusinessCard = getIntent().getParcelableExtra(EXTRA_PARAM_BIZ_CARD);
        }

        getSupportActionBar().setTitle(mBusinessCard.getName());
        showActionBar();

        mViewpager = (ViewPager) findViewById(R.id.pager);
        mIndicator = (CircleIndicator) findViewById(R.id.indicator_default);

        mTvName = (TextView) findViewById(R.id.tvName);
        mTvCompany = (TextView) findViewById(R.id.tvCompany);
        mTvJobTitle = (TextView) findViewById(R.id.tvJobTitle);
        mTvEMail = (TextView) findViewById(R.id.tvContactEMailAddress);
        mTvEMailLable = (TextView) findViewById(R.id.tvContactEMailAddressLabel);
        mTvAddress = (TextView) findViewById(R.id.tvContactCompanyAddress);
        mTvAddressLable  = (TextView) findViewById(R.id.tvContactCompanyAddressLabel);
        mTvWebSite  = (TextView) findViewById(R.id.tvContactCompanyWebsiteName);
        mTvWebSiteLable  = (TextView) findViewById(R.id.tvContactCompanyWebsiteNameLabel);
        mTvWorkPhoneLabel = (TextView) findViewById(R.id.tvWorkPhoneLabel);
        mTvWorkPhone = (TextView) findViewById(R.id.tvWorkPhone);
        mTvCellPhoneLabel = (TextView) findViewById(R.id.tvCellPhoneLabel);
        mTvCellPhone = (TextView) findViewById(R.id.tvCellPhone);
        mTvFAX = (TextView) findViewById(R.id.tvFAX);
        mTvFAXLable = (TextView) findViewById(R.id.tvFAXLabel);
        mTvSkypeLabel = (TextView) findViewById(R.id.tvSkypeLabel);
        mTvSkype = (TextView) findViewById(R.id.tvSkype);

        mNotesText = (TextView) findViewById(R.id.tvNotesText);

        mPagerAdapter = new CardPageAdapter(getSupportFragmentManager());

        mPagerAdapter.add(PlaceholderCardPageFragment.create(1, mBusinessCard.getFrontImagePath()));
        mPagerAdapter.add(PlaceholderCardPageFragment.create(2, mBusinessCard.getBackImagePath()));

        mViewpager.setOffscreenPageLimit(NUM_PAGES);
        mViewpager.setAdapter(mPagerAdapter);

        mIndicator.setViewPager(mViewpager);

        mViewpager.post(new Runnable() {
            @Override
            public void run() {
                fillData();
            }
        });
    }

    private void fillData() {
        if (mBusinessCard.getPersonage() != null) {
            Personage personage = mBusinessCard.getPersonage();
            fillDescriptionIfExists(mTvName, personage.getName());
            fillDescriptionIfExists(mTvCompany, personage.getCompany());
            fillDescriptionIfExists(mTvJobTitle, personage.getJobTitle());
            fillContactIfExists(mTvEMailLable, mTvEMail, personage.getEmail());
            fillContactIfExists(mTvAddressLable, mTvAddress, personage.getCompanyAddress());
            fillContactIfExists(mTvWebSiteLable, mTvWebSite, personage.getCompanySiteAddress());
            fillContactIfExists(mTvWorkPhoneLabel, mTvWorkPhone, personage.getWorkPhone());
            fillContactIfExists(mTvCellPhoneLabel, mTvCellPhone, personage.getCellPhone());
            fillContactIfExists(mTvFAXLable, mTvFAX, personage.getFax());
            fillContactIfExists(mTvSkypeLabel, mTvSkype, personage.getSkype());
        } else {
            mTvName.setVisibility(View.GONE);
            mTvCompany.setVisibility(View.GONE);
            mTvJobTitle.setVisibility(View.GONE);

            mTvWorkPhoneLabel.setVisibility(View.GONE);
            mTvWorkPhone.setVisibility(View.GONE);

            mTvCellPhoneLabel.setVisibility(View.GONE);
            mTvCellPhone.setVisibility(View.GONE);

            mTvSkypeLabel.setVisibility(View.GONE);
            mTvSkype.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mBusinessCard.getNotes())) {
            mNotesText.setVisibility(View.VISIBLE);
            mNotesText.setText(mBusinessCard.getNotes());
        } else {
            mNotesText.setVisibility(View.INVISIBLE);
        }

        ((PlaceholderCardPageFragment)mPagerAdapter.getFragment(0)).setUri(mBusinessCard.getFrontImagePath());
        ((PlaceholderCardPageFragment)mPagerAdapter.getFragment(1)).setUri(mBusinessCard.getBackImagePath());
    }

    private void fillContactIfExists(TextView label, TextView textView, String contact) {
        if (!TextUtils.isEmpty(contact)) {
            textView.setText(contact);
            label.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        } else {
            label.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        }
    }

    private void fillDescriptionIfExists(TextView textView, String value) {
        if (!TextUtils.isEmpty(value)) {
            textView.setText(value);
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_business_card_detail;
    }

    public static void launch(BaseActivity activity, BusinessCardPopulated item) {
        Intent intent = new Intent(activity, BusinessCardDetailActivity.class);
        intent.putExtra(EXTRA_PARAM_BIZ_CARD, item);

        Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle();

        ActivityCompat.startActivity(activity, intent, options);
    }
}
