package com.sitexa.lover.modules.wallet.importwallet;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.sitexa.lover.app.ActivityUtils;
import com.sitexa.lover.base.BaseActivity;
import com.sitexa.lover.modules.account.createaccount.CreateAccountActivity;
import com.sitexa.lover.modules.normalvp.NormalPresenter;
import com.sitexa.lover.modules.normalvp.NormalView;
import com.sitexa.lover.R;

import butterknife.BindView;
import butterknife.OnClick;

//导入钱包
public class ImportWalletActivity extends BaseActivity<NormalView, NormalPresenter> implements NormalView {


    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.import_wallet_edt)
    EditText mImportWalletEdt;
    @BindView(R.id.import_wallet)
    Button mImportWallet;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_import_wallet;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setCenterTitle(getString(R.string.import_wallet));
    }

    @Override
    protected void initData() {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public NormalPresenter initPresenter() {
        return new NormalPresenter();
    }


    @OnClick({R.id.import_wallet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.import_wallet:
                Bundle bundle = new Bundle();
                bundle.putInt("type", 1);
                ActivityUtils.next(ImportWalletActivity.this, CreateAccountActivity.class, bundle);
                break;
        }
    }
}
