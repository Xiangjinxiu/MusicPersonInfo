package com.test.autoscroll;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.iv_header_bg_ext)
    ImageView ivHeaderBgExt;
    @BindView(R.id.iv_header_bg)
    ImageView mIvHeaderBg;
    @BindView(R.id.iv_pi_header)
    CircleImageView ivPiHeader;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;

    @BindView(R.id.rl_avatar)
    RelativeLayout mRlavatar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fl_toolbar)
    FrameLayout mFlToolbar;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.appBarlayout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.tv_pi_name)
    TextView tvPiName;
    @BindView(R.id.iv_pi_sex)
    ImageView ivPiSex;
    @BindView(R.id.ll_name_sex)
    LinearLayout llNameSex;
    @BindView(R.id.tv_change_background)
    TextView mTvBackground;


    private MyPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {

        initToolbar();
        initRefresh();
        ArrayList<Fragment> mListFragment = new ArrayList<>();
        String[] titles = {"音乐", "动态", "关于我"};
        mListFragment.add(DynamicFragment.newInstance());
        mListFragment.add(DynamicFragment.newInstance());
        mListFragment.add(DynamicFragment.newInstance());
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), mListFragment);
        mPagerAdapter.setTitles(titles);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString spannableString = changeTextColor("20");
        builder.append("音乐");
        builder.append(spannableString);
        mTabLayout.getTabAt(0).setText(builder);
    }


    private void initToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        int statusHeight = getStatusHeight(this);

        toolbar.measure(0, 0);
        int measuredHeight = toolbar.getMeasuredHeight();

        ViewGroup.LayoutParams layoutParams = toolbar.getLayoutParams();
        layoutParams.height = measuredHeight + statusHeight;
        toolbar.setLayoutParams(layoutParams);


        ViewGroup.LayoutParams params = mFlToolbar.getLayoutParams();
        params.height = measuredHeight + statusHeight;
        mFlToolbar.setLayoutParams(params);


    }

    private void initRefresh() {


        mIvHeaderBg.setScaleX(1.3f);
        mIvHeaderBg.setScaleY(1.3f);

        final int[] location = new int[2];
        ivPiHeader.getLocationInWindow(location);
        final int endOffset = DensityUtil.dip2px(this, 100);

        final int[] pointV = new int[2];
        tvPiName.getLocationInWindow(pointV);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int[] ponitF = new int[2];
                int disten = -verticalOffset;
                int disOffset = endOffset;
                if (disten >= disOffset) {
                    mRlavatar.setAlpha(0);
                    mIvHeaderBg.setScaleX(1.0f);
                    mIvHeaderBg.setScaleY(1.0f);
                } else if (disten == 0) {
                    mRlavatar.setAlpha(1);
                    mIvHeaderBg.setScaleX(1.3f);
                    mIvHeaderBg.setScaleY(1.3f);
                } else {
                    float precents = (float) (disOffset - disten) / disOffset;
                    mRlavatar.setAlpha(precents);

                    mIvHeaderBg.setScaleX(1.0f+0.3f*(precents));
                    mIvHeaderBg.setScaleY(1.0f+0.3f*(precents));
                }


                ivPiHeader.getLocationInWindow(ponitF);

                int y = ponitF[1];
                if (y > 0) {
                    if (y < endOffset) {
                        float precent = (float) (endOffset - y) / endOffset;
                        toolbar.setAlpha(precent);
                    } else if (y >= endOffset) {
                        toolbar.setAlpha(0);
                    }
                } else if (y < 0) {
                    toolbar.setAlpha(1);
                } else if (y == 0) {
                    toolbar.setAlpha(1);
                }


            }
        });


    }


    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }


    private SpannableString changeTextColor(String str) {
        SpannableString spanString = new SpannableString(str);
        ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.text_font_little));
        spanString.setSpan(span, 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanString.setSpan(new RelativeSizeSpan(0.8f), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }
}
