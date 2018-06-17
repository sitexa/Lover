package com.sitexa.lover.modules.blackbox.existwalletlogin;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import com.sitexa.lover.app.ActivityUtils;
import com.sitexa.lover.app.MyApplication;
import com.sitexa.lover.base.BaseActivity;
import com.sitexa.lover.bean.UserBean;
import com.sitexa.lover.gen.UserBeanDao;
import com.sitexa.lover.modules.normalvp.NormalPresenter;
import com.sitexa.lover.modules.normalvp.NormalView;
import com.sitexa.lover.R;
import com.sitexa.lover.modules.account.createaccount.CreateAccountActivity;
import com.sitexa.lover.utils.AndroidBug5497Workaround;
import com.sitexa.lover.utils.EncryptUtil;
import com.sitexa.lover.utils.PasswordToKeyUtils;
import com.sitexa.lover.utils.Utils;
import com.sitexa.lover.view.ClearEditText;

import butterknife.BindView;
import butterknife.OnClick;

public class BlackBoxCreatWalletActivity extends BaseActivity<NormalView, NormalPresenter> implements NormalView {


    @BindView(R.id.black_box_wallet_name)
    ClearEditText mBlackBoxWalletName;
    @BindView(R.id.black_box_wallet_pwd)
    ClearEditText mBlackBoxWalletPwd;
    @BindView(R.id.black_box_two_pwd)
    ClearEditText mBlackBoxTwoPwd;
    @BindView(R.id.black_box_create_wallet)
    Button mBlackBoxCreateWallet;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_black_box_creat_wallet;
    }

    @Override
    public NormalPresenter initPresenter() {
        return new NormalPresenter();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        AndroidBug5497Workaround.assistActivity(activity);
        setCenterTitle(getString(R.string.creat_blackbox_wallet));
    }

    @Override
    protected void initData() {

    }

    @Override
    public void initEvent() {

    }

    @OnClick(R.id.black_box_create_wallet)
    public void onViewClicked() {
        if (!TextUtils.isEmpty(mBlackBoxWalletName.getText().toString()) && !TextUtils.isEmpty(mBlackBoxWalletPwd.getText().toString().trim()) && !TextUtils.isEmpty(mBlackBoxTwoPwd.getText().toString().trim())) {
            if (mBlackBoxWalletPwd.getText().toString().trim().equals(mBlackBoxTwoPwd.getText().toString().trim())) {
                if (MyApplication.getDaoInstant().getUserBeanDao().queryBuilder().where(UserBeanDao.Properties.Wallet_name.eq(mBlackBoxWalletName.getText().toString().trim())).build().unique() == null) {//检测本地存在的钱包不包含该名称
                    //数据库存储数据
                    UserBean userBean = new UserBean();
                    userBean.setWallet_uid("6f1a8e0eb24afb7ddc829f96f9f74e9d");
                    userBean.setWallet_name(mBlackBoxWalletName.getText().toString().trim());
                    String randomString = EncryptUtil.getRandomString(32);
                    userBean.setWallet_shapwd(PasswordToKeyUtils.shaEncrypt(randomString+mBlackBoxWalletPwd.getText().toString().trim()));
                    MyApplication.getDaoInstant().getUserBeanDao().insert(userBean);
                    MyApplication.getInstance().setUserBean(userBean);
                    Utils.getSpUtils().put("firstUser", mBlackBoxWalletName.getText().toString().trim());//保存上次登陆钱包
                    Utils.getSpUtils().put("loginmode", "blackbox");//保存当前登录模式
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", 1);
                    ActivityUtils.next(BlackBoxCreatWalletActivity.this, CreateAccountActivity.class);
                } else {
                    toast(getString(R.string.wallet_name_exist));
                }
            } else {
                toast(getString(R.string.two_pwd));
            }
        } else {
            toast(getString(R.string.input_all_message));
        }
    }
}
