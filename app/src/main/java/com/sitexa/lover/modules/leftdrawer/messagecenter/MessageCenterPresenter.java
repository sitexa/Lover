package com.sitexa.lover.modules.leftdrawer.messagecenter;

import android.content.Context;

import com.lzy.okgo.model.Response;
import com.sitexa.lover.base.BasePresenter;
import com.sitexa.lover.base.BaseUrl;
import com.sitexa.lover.net.HttpUtils;
import com.sitexa.lover.net.callbck.JsonCallback;
import com.sitexa.lover.bean.MessageCenterBean;
import com.sitexa.lover.bean.ResponseBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created By sitexa on 2018/6/17
 */

public class MessageCenterPresenter extends BasePresenter<MessageCenterView> {
    private Context mContext;

    public MessageCenterPresenter(Context context) {
        this.mContext = context;
    }

    public void getData(int offset) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("offset", offset + "");
        hashMap.put("size", "10");
        HttpUtils.postRequest(BaseUrl.HTTP_getMagList, mContext, hashMap, new JsonCallback<ResponseBean<List<MessageCenterBean.DataBean>>>() {
            @Override
            public void onSuccess(Response<ResponseBean<List<MessageCenterBean.DataBean>>> response) {
                if (response.body().code == 0) {
                    view.getListDataHttp(response.body().data);
                } else {
                    view.getDataHttpFail(response.body().message);
                }
            }
        });
    }

}
