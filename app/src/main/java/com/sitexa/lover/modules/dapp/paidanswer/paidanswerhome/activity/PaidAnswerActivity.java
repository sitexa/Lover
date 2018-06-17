package com.sitexa.lover.modules.dapp.paidanswer.paidanswerhome.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.sitexa.lover.app.ActivityUtils;
import com.sitexa.lover.base.BaseActivity;
import com.sitexa.lover.modules.dapp.paidanswer.chooseaccountwithcoin.ChooseAccountWithCoinActivity;
import com.sitexa.lover.modules.dapp.paidanswer.paidanswerhome.fragment.PaidAnswerFragment;
import com.sitexa.lover.modules.normalvp.NormalPresenter;
import com.sitexa.lover.modules.normalvp.NormalView;
import com.sitexa.lover.R;
import com.sitexa.lover.adapter.FindTabAdapter;
import com.sitexa.lover.modules.dapp.paidanswer.makequestion.MakeQuestionActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

//有问币答
public class PaidAnswerActivity extends BaseActivity<NormalView, NormalPresenter> implements NormalView {


    @BindView(R.id.go_back)
    ImageView mGoBack;
    @BindView(R.id.tabs)
    TabLayout mTabs;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.frame)
    FrameLayout mFrame;

    List<PaidAnswerFragment> mFragments;
    List<String> mTitles = new ArrayList<>();
    String account = null;
    @BindView(R.id.appbar)
    AppBarLayout mAppbar;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 200) {
            account = data.getStringExtra("account");
            init(data.getStringExtra("account"));
        }
    }

    public void init(String account) {
        mTitles.add(getResources().getString(R.string.existing_problem));
        mTitles.add(getResources().getString(R.string.past_problems));
        mFragments = new ArrayList<>();
        for (int i = 0; i < mTitles.size(); i++) {
            Bundle bundle = new Bundle();
            bundle.putString("releasedLable", String.valueOf(i));//0代表现有问题1代表以往问题
            bundle.putString("account", account);//选择的账号
            PaidAnswerFragment paidAnswerFragment = new PaidAnswerFragment();
            paidAnswerFragment.setArguments(bundle);
            mFragments.add(paidAnswerFragment);
        }
        FindTabAdapter adapter = new FindTabAdapter(getSupportFragmentManager(), mFragments, mTitles);
        mViewpager.setAdapter(adapter);
        mTabs.setupWithViewPager(mViewpager);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_paid_answer;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ActivityUtils.next(PaidAnswerActivity.this, ChooseAccountWithCoinActivity.class, 100);
    }

    @Override
    protected void initData() {
    }

    @SuppressLint("NewApi")
    @Override
    public void initEvent() {
        mGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("account", account);
                ActivityUtils.next(PaidAnswerActivity.this, MakeQuestionActivity.class, bundle);
            }
        });

    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return false;
    }

    @Override
    public NormalPresenter initPresenter() {
        return new NormalPresenter();
    }
}