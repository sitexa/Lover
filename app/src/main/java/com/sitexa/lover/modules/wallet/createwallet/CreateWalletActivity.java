package com.sitexa.lover.modules.wallet.createwallet;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sitexa.lover.app.ActivityUtils;
import com.sitexa.lover.app.MyApplication;
import com.sitexa.lover.base.BaseActivity;
import com.sitexa.lover.bean.UserBean;
import com.sitexa.lover.gen.UserBeanDao;
import com.sitexa.lover.modules.account.createaccount.CreateAccountActivity;
import com.sitexa.lover.modules.normalvp.NormalPresenter;
import com.sitexa.lover.modules.normalvp.NormalView;
import com.sitexa.lover.modules.wallet.importwallet.ImportWalletActivity;
import com.sitexa.lover.R;
import com.sitexa.lover.utils.EncryptUtil;
import com.sitexa.lover.utils.PasswordToKeyUtils;
import com.sitexa.lover.utils.Utils;
import com.sitexa.lover.view.ClearEditText;

import butterknife.BindView;
import butterknife.OnClick;

public class CreateWalletActivity extends BaseActivity<NormalView, NormalPresenter> implements NormalView {


    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.password)
    ClearEditText mPassword;
    @BindView(R.id.confirm_password)
    ClearEditText mConfirmPassword;
    @BindView(R.id.create_wallet)
    Button mCreateWallet;
    @BindView(R.id.go_import_wallet)
    TextView mGoImportWallet;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_creat_wallet;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setCenterTitle(getString(R.string.creat_wallet));
    }

    @Override
    protected void initData() {

    }

    @Override
    public void initEvent() {
        mGoImportWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.next(CreateWalletActivity.this, ImportWalletActivity.class);
            }
        });
    }

    @Override
    public NormalPresenter initPresenter() {
        return new NormalPresenter();
    }


    @OnClick(R.id.create_wallet)
    public void onViewClicked() {
        if (TextUtils.isEmpty(mPassword.getText().toString()) || TextUtils.isEmpty(mConfirmPassword.getText().toString())) {
            toast(getString(R.string.input_pwd_toast));
        } else if (mPassword.getText().toString() != null && mConfirmPassword.getText().toString() != null && mConfirmPassword.getText().toString().equals(mPassword.getText().toString())) {
            UserBean userBean = MyApplication.getDaoInstant().getUserBeanDao().queryBuilder().where(UserBeanDao.Properties.Wallet_phone.eq(Utils.getSpUtils().getString("firstUser"))).build().unique();
            if (userBean != null) {
                String randomString = EncryptUtil.getRandomString(32);
                userBean.setWallet_shapwd(PasswordToKeyUtils.shaEncrypt(randomString+mPassword.getText().toString().trim()));
                MyApplication.getDaoInstant().getUserBeanDao().update(userBean);
                MyApplication.getInstance().getUserBean().setWallet_shapwd(PasswordToKeyUtils.shaEncrypt(randomString+mPassword.getText().toString().trim()));
            }
            Bundle bundle = new Bundle();
            bundle.putInt("type", 1);
            ActivityUtils.next(CreateWalletActivity.this, CreateAccountActivity.class, bundle);
        } else {
            toast(getString(R.string.two_pwd));
        }
    }


}
