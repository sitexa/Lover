package com.sitexa.lover.modules.leftdrawer.systemsetting;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.sitexa.lover.base.BaseActivity;
import com.sitexa.lover.R;
import com.sitexa.lover.modules.normalvp.NormalPresenter;
import com.sitexa.lover.modules.normalvp.NormalView;
import com.sitexa.lover.utils.LocalManageUtil;
import com.sitexa.lover.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

public class LanguageActivity extends BaseActivity<NormalView, NormalPresenter> implements NormalView {

    @BindView(R.id.language_cn)
    RelativeLayout mLanguageCn;
    @BindView(R.id.language_en)
    RelativeLayout mLanguageEn;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_language;
    }

    @Override
    public NormalPresenter initPresenter() {
        return new NormalPresenter();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setCenterTitle(getString(R.string.select_language));
    }

    @Override
    protected void initData() {

    }

    @Override
    public void initEvent() {

    }



    @OnClick({R.id.language_cn, R.id.language_en})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.language_cn:
                LocalManageUtil.saveSelectLanguage(this, 1);
                Utils.restartAPP(10);
                break;
            case R.id.language_en:
                LocalManageUtil.saveSelectLanguage(this, 2);
                Utils.restartAPP(10);
                break;
        }
    }
}
