package com.sitexa.lover.modules.seach;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.sitexa.lover.app.ActivityUtils;
import com.sitexa.lover.base.BaseActivity;
import com.sitexa.lover.modules.friendslist.friendsdetails.FriendsDetailsActivity;
import com.sitexa.lover.R;
import com.sitexa.lover.bean.BlockChainAccountInfoBean;
import com.sitexa.lover.utils.KeyBoardUtil;
import com.sitexa.lover.utils.RegexUtil;
import com.sitexa.lover.view.ClearEditText;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

public class SeachActivity extends BaseActivity<SeachView, SeachPresenter> implements SeachView {


    @BindView(R.id.edt_seach)
    ClearEditText mEdtSeach;
    @BindView(R.id.seach_cancel)
    TextView mSeachCancel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_seach;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 200); // 0.2秒后自动弹出
    }

    @Override
    protected void initData() {

    }

    @Override
    public void initEvent() {
        mEdtSeach.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“搜索”键*/
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!mEdtSeach.getText().toString().isEmpty() && RegexUtil.seachName(mEdtSeach.getText().toString())) {
                        KeyBoardUtil.getInstance(SeachActivity.this).hide();
                        showProgress();
                        presenter.getAccountInfoData(mEdtSeach.getText().toString());
                    } else {
                        toast(getString(R.string.eos_register_toast));
                    }
                }
                return false;
            }
        });
    }

    @Override
    public SeachPresenter initPresenter() {
        return new SeachPresenter(SeachActivity.this);
    }

    @Override
    public void getBlockchainAccountInfoDataHttp(BlockChainAccountInfoBean.DataBean blockChainAccountInfoBean) {
        hideProgress();
        Bundle bundle = new Bundle();
        bundle.putString("name", mEdtSeach.getText().toString());
        bundle.putString("friend_type", "2");
        bundle.putString("avatar", "");
        bundle.putString("uid", "");
        bundle.putString("from", "seach");
        ActivityUtils.next(SeachActivity.this, FriendsDetailsActivity.class, bundle);
    }

    @Override
    public void getDataHttpFail(String msg) {
        hideProgress();
        toast(msg);
    }

    @OnClick(R.id.seach_cancel)
    public void onViewClicked() {
        finish();
        KeyBoardUtil.getInstance(SeachActivity.this).hide();
    }


}
