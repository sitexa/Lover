package com.sitexa.lover.modules.blackbox.existwalletlogin;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sitexa.lover.app.ActivityUtils;
import com.sitexa.lover.app.AppManager;
import com.sitexa.lover.app.MyApplication;
import com.sitexa.lover.base.BaseActivity;
import com.sitexa.lover.bean.UserBean;
import com.sitexa.lover.modules.leftdrawer.systemsetting.RichTextActivity;
import com.sitexa.lover.modules.normalvp.NormalPresenter;
import com.sitexa.lover.modules.normalvp.NormalView;
import com.sitexa.lover.R;
import com.sitexa.lover.adapter.AdapterManger;
import com.sitexa.lover.adapter.baseadapter.CommonAdapter;
import com.sitexa.lover.modules.blackbox.BlackBoxMainActivity;
import com.sitexa.lover.utils.AndroidBug5497Workaround;
import com.sitexa.lover.utils.FilesUtils;
import com.sitexa.lover.utils.Utils;
import com.sitexa.lover.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.sitexa.lover.utils.Utils.getContext;

public class ExistBlackBoxLoginActivity extends BaseActivity<NormalView, NormalPresenter> implements NormalView {


    @BindView(R.id.social_contact)
    TextView mSocialContact;
    @BindView(R.id.title)
    RelativeLayout mTitle;
    @BindView(R.id.wallet)
    RecyclerView mWallet;
    @BindView(R.id.black_box_sure)
    Button mBlackBoxSure;
    @BindView(R.id.create_a_wallet)
    TextView mCreateAWallet;
    @BindView(R.id.black_box_info)
    TextView mBlackBoxInfo;

    List<UserBean> mUserBeanList = new ArrayList<>();
    private CommonAdapter mCommonAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_exist_black_box_login;
    }

    @Override
    public NormalPresenter initPresenter() {
        return new NormalPresenter();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        AndroidBug5497Workaround.assistActivity(activity);
    }

    @Override
    protected void initData() {
        mUserBeanList = MyApplication.getDaoInstant().getUserBeanDao().loadAll();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setSmoothScrollbarEnabled(true);
        mWallet.setLayoutManager(layoutManager);
        mWallet.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL, 1, getResources().getColor(R.color.line)));
        mCommonAdapter = AdapterManger.getWalletListAdapter(this, mUserBeanList);
        mWallet.setAdapter(mCommonAdapter);
    }

    @Override
    public void initEvent() {

    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.fitsSystemWindows(false).statusBarColor(R.color.transparent).titleBar(mTitle).statusBarDarkFont(false, 0f).init();
    }

    @OnClick({R.id.social_contact, R.id.black_box_sure, R.id.create_a_wallet, R.id.black_box_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.social_contact:
                finish();
                break;
            case R.id.black_box_sure:
                Utils.getSpUtils().put("firstUser", mUserBeanList.get(AdapterManger.getSelectedPos()).getWallet_name());//保存上次登陆钱包
                Utils.getSpUtils().put("loginmode", "blackbox");//保存当前登录模式
                ActivityUtils.next(this, BlackBoxMainActivity.class);
                AppManager.getAppManager().finishAllActivity();
                break;
            case R.id.create_a_wallet:
                ActivityUtils.next(this, BlackBoxCreatWalletActivity.class);
                break;
            case R.id.black_box_info:
                Bundle bundle = new Bundle();
                bundle.putString("details", FilesUtils.readAssetsTxt(this,"black_box_intro"));
                bundle.putString("title", getString(R.string.black_box));
                ActivityUtils.next(ExistBlackBoxLoginActivity.this, RichTextActivity.class, bundle);
                break;
        }
    }
}
