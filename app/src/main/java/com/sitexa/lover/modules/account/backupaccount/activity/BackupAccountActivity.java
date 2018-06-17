package com.sitexa.lover.modules.account.backupaccount.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;

import com.sitexa.lover.base.BaseActivity;
import com.sitexa.lover.modules.account.backupaccount.fragment.BackupAccountFragment;
import com.sitexa.lover.modules.normalvp.NormalPresenter;
import com.sitexa.lover.modules.normalvp.NormalView;
import com.sitexa.lover.R;
import com.sitexa.lover.adapter.baseadapter.BackupAccountTabAdapter;
import com.sitexa.lover.bean.AccountInfoBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 备份eos账号
 */
public class BackupAccountActivity extends BaseActivity<NormalView, NormalPresenter> implements NormalView {


    @BindView(R.id.tabs)
    TabLayout mTabs;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;

    List<BackupAccountFragment> mFragments;
    List<String> mTitles = new ArrayList<>();
    AccountInfoBean mAccountInfoBean = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_back_up_account;
    }

    @Override
    public NormalPresenter initPresenter() {
        return new NormalPresenter();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);//当前页面防截屏录屏
        setCenterTitle(getString(R.string.back_up_account));
    }

    @Override
    protected void initData() {
        mAccountInfoBean = getIntent().getParcelableExtra("account");
        init(mAccountInfoBean);
    }

    public void init(AccountInfoBean account) {
        mTitles.add(getResources().getString(R.string.pra_backup));
        mTitles.add(getResources().getString(R.string.word_backup));
        mFragments = new ArrayList<>();
        for (int i = 0; i < mTitles.size(); i++) {
            Bundle bundle = new Bundle();
            bundle.putInt("i",i);
            bundle.putParcelable("account", account);//选择的账号
            BackupAccountFragment backupAccountFragment = new BackupAccountFragment();
            backupAccountFragment.setArguments(bundle);
            mFragments.add(backupAccountFragment);
        }
        BackupAccountTabAdapter adapter = new BackupAccountTabAdapter(getSupportFragmentManager(), mFragments, mTitles);
        mViewpager.setAdapter(adapter);
        mTabs.setupWithViewPager(mViewpager);
    }

    @Override
    public void initEvent() {

    }

}
