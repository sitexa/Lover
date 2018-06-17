package com.sitexa.lover.modules.leftdrawer.transactionhistory;

import android.content.Context;

import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.sitexa.lover.base.BasePresenter;
import com.sitexa.lover.base.BaseUrl;
import com.sitexa.lover.net.HttpUtils;
import com.sitexa.lover.net.callbck.JsonCallback;
import com.sitexa.lover.bean.PostChainHistoryBean;
import com.sitexa.lover.bean.TransferHistoryBean;
import com.sitexa.lover.bean.ResponseBean;

/**
 * Created By sitexa on 2018/6/17
 */

public class TransactionHistoryPresenter extends BasePresenter<TransactionHistoryView> {
    private Context mContext;

    public TransactionHistoryPresenter(Context context) {
        this.mContext = context;
    }

    public void getTransferHistoryData(PostChainHistoryBean postChainHistoryBean) {
        HttpUtils.postRequest(BaseUrl.HTTP_get_transaction_history, mContext, new Gson().toJson(postChainHistoryBean), new JsonCallback<ResponseBean<TransferHistoryBean.DataBeanX>>() {
            @Override
            public void onSuccess(Response<ResponseBean<TransferHistoryBean.DataBeanX>> response) {
                if (response.body().code == 0) {
                    view.getTransferHistoryDataHttp(response.body().data);
                } else {
                    view.getDataHttpFail(response.body().message);
                }
            }
        });
    }
}
