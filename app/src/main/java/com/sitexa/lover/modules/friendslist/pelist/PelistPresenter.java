package com.sitexa.lover.modules.friendslist.pelist;

import android.content.Context;

import com.lzy.okgo.model.Response;
import com.sitexa.lover.base.BasePresenter;
import com.sitexa.lover.base.BaseUrl;
import com.sitexa.lover.net.HttpUtils;
import com.sitexa.lover.net.callbck.JsonCallback;
import com.sitexa.lover.bean.PelistBean;
import com.sitexa.lover.bean.ResponseBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created By sitexa on 2018/6/17
 */

public class PelistPresenter extends BasePresenter<PelistView> {
    private Context mContext;

    public PelistPresenter(Context context) {
        this.mContext = context;
    }


    public void getData(String type, int offset) {
        String url = null;
        if (type.equals("0")) {
            url = BaseUrl.HTTP_pelist;
        } else {
            url = BaseUrl.HTTP_commpanylist;
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("offset", offset + "");
        hashMap.put("size", "10");
        HttpUtils.postRequest(url, mContext, hashMap, new JsonCallback<ResponseBean<List<PelistBean.DataBean>>>() {
            @Override
            public void onSuccess(Response<ResponseBean<List<PelistBean.DataBean>>> response) {
                if (response.body().code == 0) {
                    view.getListDataHttp(response.body().data);
                } else {
                    view.getDataHttpFail(response.body().message);
                }
            }
        });
    }

}
