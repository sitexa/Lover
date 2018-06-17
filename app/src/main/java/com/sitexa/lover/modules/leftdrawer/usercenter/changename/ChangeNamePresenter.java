package com.sitexa.lover.modules.leftdrawer.usercenter.changename;

import android.content.Context;

import com.lzy.okgo.model.Response;
import com.sitexa.lover.app.MyApplication;
import com.sitexa.lover.base.BasePresenter;
import com.sitexa.lover.base.BaseUrl;
import com.sitexa.lover.net.HttpUtils;
import com.sitexa.lover.net.callbck.JsonCallback;
import com.sitexa.lover.bean.ResponseBean;

import java.util.HashMap;

/**
 * Created by pocketEos on 2018/1/18.
 */

public class ChangeNamePresenter extends BasePresenter<ChangeNameView> {

    private Context mContext;

    public ChangeNamePresenter(Context context) {
        this.mContext = context;
    }
    public void HTTP_updateNameData(String userName) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("uid",  MyApplication.getInstance().getUserBean().getWallet_uid());
        hashMap.put("userName", userName);
        HttpUtils.postRequest(BaseUrl.HTTP_updateName, mContext, hashMap, new JsonCallback<ResponseBean<String>>() {
            @Override
            public void onSuccess(Response<ResponseBean<String>> response) {
                if (response.body().code == 0) {
                    view.updateNameDataHttp();
                } else {
                    view.getDataHttpFail(response.body().message);
                }
            }
        });
    }

}

