package com.sitexa.lover.modules.wallet.createwallet.bindphone;

import android.content.Context;

import com.lzy.okgo.model.Response;
import com.sitexa.lover.base.BasePresenter;
import com.sitexa.lover.base.BaseUrl;
import com.sitexa.lover.net.HttpUtils;
import com.sitexa.lover.net.callbck.JsonCallback;
import com.sitexa.lover.bean.CodeAuthBean;
import com.sitexa.lover.bean.ResponseBean;

import java.util.HashMap;

/**
 * Created by pocketEos on 2018/1/18.
 */

public class BindPhonePresenter extends BasePresenter<BindPhoneView> {
    private Context mContext;

    public BindPhonePresenter(Context context) {
        this.mContext = context;
    }

    public void getCodeData(String mobilephone) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("phoneNum", mobilephone);
        HttpUtils.postRequest(BaseUrl.HTTP_Get_code, mContext, hashMap, new JsonCallback<ResponseBean<String>>() {
            @Override
            public void onSuccess(Response<ResponseBean<String>> response) {
                if (response.body().code == 0) {
                    view.getCodeDataHttp(response.body().message);
                } else {
                    view.getDataHttpFail(response.body().message);
                }
            }
        });
    }

    public void get_bind_phone_number(String phoneNum, String openid, String type, String name, String avatar, String code) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("phoneNum", phoneNum);
        hashMap.put("openid", openid);
        hashMap.put("type", type);
        hashMap.put("name", name);
        hashMap.put("avatar", avatar);
        hashMap.put("code", code);
        HttpUtils.postRequest(BaseUrl.HTTP_bind_phone_number, mContext, hashMap, new JsonCallback<ResponseBean<CodeAuthBean.DataBean>>() {
            @Override
            public void onSuccess(Response<ResponseBean<CodeAuthBean.DataBean>> response) {
                if (response.body().code == 0) {
                    view.getCodeAuthDataHttp(response.body().data);
                } else {
                    view.getDataHttpFail(response.body().message);
                }
            }
        });
    }
}

