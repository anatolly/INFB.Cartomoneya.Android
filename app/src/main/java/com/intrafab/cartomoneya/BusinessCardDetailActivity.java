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
import com.intrafab.cartomoneya.data.BizCard;
import com.intrafab.cartomoneya.fragments.PlaceholderCardPageFragment;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Vasily Laushkin <vaslinux@gmail.com> on 24/05/15.
 */
public class BusinessCardDetailActivity extends BaseActivity {

    public static final String TAG = ShopCardDetailActivity.class.getName();
    public static final String EXTRA_PARAM_BIZ_CARD = "param_biz_card";
    private static final int NUM_PAGES = 2;

    private BizCard mBizCard;
    private ViewPager mViewpager;
    private CircleIndicator mIndicator;
    private TextView mNotesText;
    private CardPageAdapter mPagerAdapter;
    private TextView mTvName;
    private TextView mTvJobTitle;
    private TextView mTvCompany;
    private TextView mTvWorkPhone;
    private TextView mTvCellPhone;
    private TextView mTvSkype;
    private TextView mTvWorkPhoneLabel;
    private TextView mTvCellPhoneLabel;
    private TextView mTvSkypeLabel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().getThemedContext();

        Intent intent = getIntent();
        if (intent != null) {
            mBizCard = getIntent().getParcelableExtra(EXTRA_PARAM_BIZ_CARD);
        }

        getSupportActionBar().setTitle(mBizCard.getName());
        showActionBar();

        mViewpager = (ViewPager) findViewById(R.id.pager);
        mIndicator = (CircleIndicator) findViewById(R.id.indicator_default);

        mTvName = (TextView) findViewById(R.id.tvName);
        mTvCompany = (TextView) findViewById(R.id.tvCompany);
        mTvJobTitle = (TextView) findViewById(R.id.tvJobTitle);

        mTvWorkPhoneLabel = (TextView) findViewById(R.id.tvWorkPhoneLabel);
        mTvWorkPhone = (TextView) findViewById(R.id.tvWorkPhone);
        mTvCellPhoneLabel = (TextView) findViewById(R.id.tvCellPhoneLabel);
        mTvCellPhone = (TextView) findViewById(R.id.tvCellPhone);
        mTvSkypeLabel = (TextView) findViewById(R.id.tvSkypeLabel);
        mTvSkype = (TextView) findViewById(R.id.tvSkype);

        mNotesText = (TextView) findViewById(R.id.tvNotesText);

        mPagerAdapter = new CardPageAdapter(getSupportFragmentManager());

        mPagerAdapter.add(PlaceholderCardPageFragment.create(1, mBizCard.getFrontImagePath()));
        mPagerAdapter.add(PlaceholderCardPageFragment.create(2, mBizCard.getBackImagePath()));

        mViewpager.setOffscreenPageLimit(NUM_PAGES);
        mViewpager.setAdapter(mPagerAdapter);

        mIndicator.setViewPager(mViewpager);

        if (mBizCard.getPersonage() != null) {
            BizCard.Personage personage = mBizCard.getPersonage();
            fillDescriptionIfExists(mTvName, personage.getName());
            fillDescriptionIfExists(mTvCompany, personage.getCompany());
            fillDescriptionIfExists(mTvJobTitle, personage.getJobTitle());

            fillContactIfExists(mTvWorkPhoneLabel, mTvWorkPhone, personage.getWorkPhone());
            fillContactIfExists(mTvCellPhoneLabel, mTvCellPhone, personage.getCellPhone());
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

        if (!TextUtils.isEmpty(mBizCard.getNotes())) {
            mNotesText.setVisibility(View.VISIBLE);
            mNotesText.setText(mBizCard.getNotes());
        } else {
            mNotesText.setVisibility(View.INVISIBLE);
        }
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

    public static void launch(BaseActivity activity, BizCard item) {
        Intent intent = new Intent(activity, BusinessCardDetailActivity.class);
        intent.putExtra(EXTRA_PARAM_BIZ_CARD, item);

        Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle();

        ActivityCompat.startActivity(activity, intent, options);
    }
}
