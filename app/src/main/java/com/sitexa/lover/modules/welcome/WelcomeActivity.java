package com.sitexa.lover.modules.welcome;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.sitexa.lover.app.ActivityUtils;
import com.sitexa.lover.app.MyApplication;
import com.sitexa.lover.base.BaseActivity;
import com.sitexa.lover.bean.UserBean;
import com.sitexa.lover.gen.UserBeanDao;
import com.sitexa.lover.modules.blackbox.nowalletlogin.BlackBoxLoginActivity;
import com.sitexa.lover.modules.normalvp.NormalPresenter;
import com.sitexa.lover.modules.normalvp.NormalView;
import com.sitexa.lover.R;
import com.sitexa.lover.modules.blackbox.BlackBoxMainActivity;
import com.sitexa.lover.modules.main.MainActivity;
import com.sitexa.lover.modules.wallet.createwallet.CreateWalletActivity;
import com.sitexa.lover.modules.wallet.createwallet.login.LoginActivity;
import com.sitexa.lover.utils.Utils;

public class WelcomeActivity extends BaseActivity<NormalView, NormalPresenter> implements NormalView {
    private static final int TIME = 500;
    private static final int GO_HOME = 200;
    private static final int GO_BLACKBOXHOME = 700;
    private static final int GO_GUIDE = 100;
    private static final int GO_LOGIN = 300;
    private static final int GO_CREATWALLET = 400;
    private static final int GO_BLACKBOXCREATWALLET = 600;
    private boolean isFirstIn = true;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GO_HOME:
                    goHome();
                    break;
                case GO_GUIDE:
                    goLogin();
                    break;
                case GO_LOGIN:
                    goLogin();
                    break;
                case GO_CREATWALLET:
                    goCreat_wallet();
                    break;
                case GO_BLACKBOXCREATWALLET:
                    goCreat_black_box_wallet();
                    break;
                case GO_BLACKBOXHOME:
                    goBlackboxHome();
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {
        if (isFirstIn && Utils.getSpUtils().getString("firstUser") == null && Utils.getSpUtils().getString("loginmode") == null) {
            mHandler.sendEmptyMessageDelayed(GO_LOGIN, TIME);
            isFirstIn = false;
        } else if(Utils.getSpUtils().getString("firstUser") != null && Utils.getSpUtils().getString("loginmode").equals("phone")){//上次为社交模式登录
            UserBean userBean = MyApplication.getDaoInstant().getUserBeanDao().queryBuilder().where(UserBeanDao.Properties.Wallet_phone.eq(Utils.getSpUtils().getString("firstUser"))).build().unique();
            if (userBean == null) {
                mHandler.sendEmptyMessageDelayed(GO_LOGIN, TIME);
            } else if (userBean != null && userBean.getWallet_main_account() == null) {//钱包存在 但是没有账号
                    mHandler.sendEmptyMessageDelayed(GO_CREATWALLET, TIME);
            } else {
                    mHandler.sendEmptyMessageDelayed(GO_HOME, TIME);
            }
        }else if(Utils.getSpUtils().getString("firstUser") != null && Utils.getSpUtils().getString("loginmode").equals("blackbox")){//上次登录模式为黑匣子模式
            UserBean userBean = MyApplication.getDaoInstant().getUserBeanDao().queryBuilder().where(UserBeanDao.Properties.Wallet_name.eq(Utils.getSpUtils().getString("firstUser"))).build().unique();
            if (userBean == null) {
                mHandler.sendEmptyMessageDelayed(GO_LOGIN, TIME);
            } else if (userBean != null && userBean.getWallet_main_account() == null) {//钱包存在 但是没有账号
                mHandler.sendEmptyMessageDelayed(GO_BLACKBOXCREATWALLET, TIME);
            } else {
                mHandler.sendEmptyMessageDelayed(GO_BLACKBOXHOME, TIME);
            }

        }
    }

    @Override
    public void initEvent() {

    }

    @Override
    public NormalPresenter initPresenter() {
        return new NormalPresenter();
    }

    private void goHome() {
        ActivityUtils.next(WelcomeActivity.this, MainActivity.class, true);
    }

    private void goBlackboxHome() {
        ActivityUtils.next(WelcomeActivity.this, BlackBoxMainActivity.class, true);
    }

    private void goLogin() {
        ActivityUtils.next(WelcomeActivity.this, LoginActivity.class, true);
    }

    private void goCreat_wallet() {
        ActivityUtils.next(WelcomeActivity.this, CreateWalletActivity.class);
    }

    private void goCreat_black_box_wallet() {
        ActivityUtils.next(WelcomeActivity.this, BlackBoxLoginActivity.class);
    }

    /*private void goGuide() {
        ActivityUtils.next(WelcomeActivity.this, GuideActivity.class, true);
    }*/
}
