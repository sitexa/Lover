package com.sitexa.lover.modules.news;

import android.content.Context;

import com.lzy.okgo.model.Response;
import com.sitexa.lover.base.BasePresenter;
import com.sitexa.lover.base.BaseUrl;
import com.sitexa.lover.bean.NewsBean;
import com.sitexa.lover.net.HttpUtils;
import com.sitexa.lover.net.callbck.JsonCallback;
import com.sitexa.lover.bean.CoinBean;
import com.sitexa.lover.bean.ResponseBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created By sitexa on 2018/6/17
 */

public class NewsPresenter extends BasePresenter<NewsView> {
    private Context mContext;

    public NewsPresenter(Context context) {
        this.mContext = context;
    }

    public void getCoinData() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        HttpUtils.getRequets(BaseUrl.HTTP_getAssetCategoryAll, mContext, hashMap, new JsonCallback<ResponseBean<List<CoinBean.DataBean>>>() {
            @Override
            public void onSuccess(Response<ResponseBean<List<CoinBean.DataBean>>> response) {
                if (response.body().code == 0) {
                    view.getCoinTypeDataHttp(response.body().data);
                } else {
                    view.getDataHttpFail(response.body().message);
                }
            }
        });
    }

    public void getNewsData(int offset, String assetCategoryId) {

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("offset", offset + "");
        hashMap.put("assetCategoryId", assetCategoryId);
        hashMap.put("size", "5");
        hashMap.put("scope", "2");//获取新闻列表
        HttpUtils.postRequest(BaseUrl.HTTP_Get_news_list, mContext, hashMap, new JsonCallback<ResponseBean<List<NewsBean.DataBean>>>() {
            @Override
            public void onSuccess(Response<ResponseBean<List<NewsBean.DataBean>>> response) {
                if (response.body().code == 0) {
                    view.getNewsDataHttp(response.body().data);
                } else {
                    view.getDataHttpFail(response.body().message);
                }
            }
        });
    }

    public void getBannersData() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("scope", "1");//获取banner
        HttpUtils.postRequest(BaseUrl.HTTP_Get_news_list, mContext, hashMap, new JsonCallback<ResponseBean<List<NewsBean.DataBean>>>() {
            @Override
            public void onSuccess(Response<ResponseBean<List<NewsBean.DataBean>>> response) {
                if (response.body().code == 0) {
                    view.getBannerDataHttp(response.body().data);
                } else {
                    view.getDataHttpFail(response.body().message);
                }
            }
        });
    }
}
