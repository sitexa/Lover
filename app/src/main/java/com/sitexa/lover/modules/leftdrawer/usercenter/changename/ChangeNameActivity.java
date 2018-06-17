package com.sitexa.lover.modules.leftdrawer.usercenter.changename;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sitexa.lover.app.MyApplication;
import com.sitexa.lover.base.BaseActivity;
import com.sitexa.lover.bean.UserBean;
import com.sitexa.lover.gen.UserBeanDao;
import com.sitexa.lover.R;
import com.sitexa.lover.utils.KeyBoardUtil;
import com.sitexa.lover.view.ClearEditText;

import butterknife.BindView;

//修改名字
public class ChangeNameActivity extends BaseActivity<ChangeNameView, ChangeNamePresenter> implements ChangeNameView {


    @BindView(R.id.tv_right_text)
    TextView mTvRightText;
    @BindView(R.id.change_name)
    ClearEditText mChangeName;
    @BindView(R.id.iv_back)
    ImageView mIvBack;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_name;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setCenterTitle(getString(R.string.change_name));
        setRightTitle(getString(R.string.save), true);
        mChangeName.setText(MyApplication.getInstance().getUserBean().getWallet_name());
    }

    @Override
    protected void initData() {

    }

    @Override
    public void initEvent() {
        mTvRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtil.getInstance(ChangeNameActivity.this).hide();
                if (!mChangeName.getText().toString().equals(MyApplication.getInstance().getUserBean().getWallet_name())) {
                    if (MyApplication.getDaoInstant().getUserBeanDao().queryBuilder().where(UserBeanDao.Properties.Wallet_name.eq(mChangeName.getText().toString().trim())).build().unique() == null) {//检测本地存在的钱包不包含该名称
                        presenter.HTTP_updateNameData(mChangeName.getText().toString());
                    } else {
                        toast(getString(R.string.wallet_name_exist));
                    }
                } else {
                    toast(getString(R.string.no_change_name_toast));
                }
            }
        });
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                KeyBoardUtil.getInstance(ChangeNameActivity.this).hide();
            }
        });
    }

    @Override
    public ChangeNamePresenter initPresenter() {
        return new ChangeNamePresenter(ChangeNameActivity.this);
    }

    @Override
    public void updateNameDataHttp() {
        toast(getString(R.string.change_name_success));
        UserBean userBean = MyApplication.getDaoInstant().getUserBeanDao().queryBuilder().where(UserBeanDao.Properties.Wallet_phone.eq(MyApplication.getInstance().getUserBean().getWallet_phone())).build().unique();
        if (userBean != null) {
            userBean.setWallet_name(mChangeName.getText().toString().trim());
            MyApplication.getDaoInstant().getUserBeanDao().update(userBean);
            MyApplication.getInstance().getUserBean().setWallet_name(mChangeName.getText().toString().trim());
            finish();
        }
    }

    @Override
    public void getDataHttpFail(String msg) {
        toast(msg);
    }
}
