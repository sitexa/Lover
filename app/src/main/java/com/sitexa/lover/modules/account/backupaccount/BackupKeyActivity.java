package com.sitexa.lover.modules.account.backupaccount;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.sitexa.lover.app.ActivityUtils;
import com.sitexa.lover.app.AppManager;
import com.sitexa.lover.app.MyApplication;
import com.sitexa.lover.base.BaseActivity;
import com.sitexa.lover.modules.blackbox.BlackBoxMainActivity;
import com.sitexa.lover.modules.normalvp.NormalPresenter;
import com.sitexa.lover.modules.normalvp.NormalView;
import com.sitexa.lover.R;
import com.sitexa.lover.bean.AccountInfoBean;
import com.sitexa.lover.modules.main.MainActivity;
import com.sitexa.lover.utils.EncryptUtil;
import com.sitexa.lover.utils.PasswordToKeyUtils;
import com.sitexa.lover.utils.Utils;
import com.sitexa.lover.view.dialog.passworddialog.PasswordCallback;
import com.sitexa.lover.view.dialog.passworddialog.PasswordDialog;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BackupKeyActivity extends BaseActivity<NormalView, NormalPresenter> implements NormalView {


    @BindView(R.id.switch_view)
    Switch mSwitchView;
    @BindView(R.id.go_home)
    Button mGoHome;
    @BindView(R.id.details)
    TextView mDetails;
    AccountInfoBean mAccountInfoBean = null;
    @BindView(R.id.desc)
    TextView mDesc;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_back_up_key;
    }

    @Override
    public NormalPresenter initPresenter() {
        return new NormalPresenter();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);//当前页面防截屏录屏
            setCenterTitle(getString(R.string.pra_backup));
    }

    @Override
    protected void initData() {
        mAccountInfoBean = getIntent().getParcelableExtra("account");
    }

    @Override
    public void initEvent() {
        mSwitchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    PasswordDialog mPasswordDialog = new PasswordDialog(BackupKeyActivity.this, new PasswordCallback() {
                        @Override
                        public void sure(String password) {
                            if (MyApplication.getInstance().getUserBean().getWallet_shapwd().equals(PasswordToKeyUtils.shaCheck(password))) {
                                try {
                                    mDetails.setText("OWNKEY:\n" + EncryptUtil.getDecryptString(mAccountInfoBean.getAccount_owner_private_key(), password)
                                            + "\nACTIVEKEY：\n" + EncryptUtil.getDecryptString(mAccountInfoBean.getAccount_active_private_key(), password));
                                } catch (NoSuchAlgorithmException e) {
                                    e.printStackTrace();
                                } catch (InvalidKeySpecException e) {
                                    e.printStackTrace();
                                }
                                mDetails.setVisibility(View.VISIBLE);
                                mSwitchView.setVisibility(View.GONE);
                                mDesc.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void cancle() {
                        }
                    });
                    mPasswordDialog.setCancelable(true);
                    mPasswordDialog.show();
                } else {
                    mDetails.setVisibility(View.GONE);
                    mSwitchView.setVisibility(View.VISIBLE);
                    mDesc.setVisibility(View.VISIBLE);
                }
            }
        });

        mGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.getSpUtils().getString("loginmode").equals("phone")) {
                    ActivityUtils.next(BackupKeyActivity.this, MainActivity.class, true);
                } else {
                    ActivityUtils.next(BackupKeyActivity.this, BlackBoxMainActivity.class, true);
                }
                AppManager.getAppManager().finishAllActivity();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
