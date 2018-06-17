package com.sitexa.lover.modules.coindetails;

import android.content.Context;

import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.utils.OkLogger;
import com.sitexa.lover.base.BasePresenter;
import com.sitexa.lover.base.BaseUrl;
import com.sitexa.lover.net.HttpUtils;
import com.sitexa.lover.net.callbck.JsonCallback;
import com.sitexa.lover.bean.PostChainHistoryBean;
import com.sitexa.lover.bean.SparkLinesBean;
import com.sitexa.lover.bean.TransferHistoryBean;
import com.sitexa.lover.bean.ResponseBean;

import java.util.HashMap;

/**
 * Created By sitexa on 2018/6/17
 */

public class CoinDetailsPresenter extends BasePresenter<CoinDetailsView> {
    private Context mContext;

    public CoinDetailsPresenter(Context context) {
        this.mContext = context;
    }

    public void getTransferHistoryData(PostChainHistoryBean postChainHistoryBean) {
        HttpUtils.postRequest(BaseUrl.HTTP_get_transaction_history, mContext, new Gson().toJson(postChainHistoryBean), new JsonCallback<ResponseBean<TransferHistoryBean.DataBeanX>>() {
            @Override
            public void onSuccess(Response<ResponseBean<TransferHistoryBean.DataBeanX>> response) {
                OkLogger.i("===========>"+response.body().code);
                if (response.body().code == 0) {
                    view.getTransferHistoryDataHttp(response.body().data);
                } else {
                    view.getDataHttpFail(response.body().message);
                }
            }
        });
    }

    public void getSparklinesData() {
        HttpUtils.getRequets(BaseUrl.HTTP_get_sparklines, mContext, new HashMap<String, String>(), new JsonCallback<ResponseBean<SparkLinesBean.DataBean>>() {
            @Override
            public void onSuccess(Response<ResponseBean<SparkLinesBean.DataBean>> response) {
                if (response.body().code == 0) {
                    view.getSparklinesData(response.body().data);
                } else {
                    view.getDataHttpFail(response.body().message);
                }
            }
        });
    }
}
