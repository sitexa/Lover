package com.sitexa.lover.modules.dapp.paidanswer.questiondetails;

import android.content.Context;

import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.sitexa.lover.base.BasePresenter;
import com.sitexa.lover.base.BaseUrl;
import com.sitexa.lover.net.HttpUtils;
import com.sitexa.lover.net.callbck.JsonCallback;
import com.sitexa.lover.bean.ChainInfoBean;
import com.sitexa.lover.bean.GetChainJsonBean;
import com.sitexa.lover.bean.GetRequiredKeysBean;
import com.sitexa.lover.bean.PostChainAnswerJsonBean;
import com.sitexa.lover.bean.PostChainPublicKeyBean;
import com.sitexa.lover.bean.TransferSuccessBean;
import com.sitexa.lover.bean.ResponseBean;

import java.util.HashMap;

/**
 * Created By sitexa on 2018/6/17
 */

public class QuestionDetailsPresenter extends BasePresenter<QuestionDetailsView> {
    private Context mContext;

    public QuestionDetailsPresenter(Context context) {
        this.mContext = context;
    }

    public void getChainInfo() {
        HttpUtils.getRequets(BaseUrl.HTTP_get_chain_info, mContext, new HashMap<String, String>(), new JsonCallback<ResponseBean<ChainInfoBean.DataBean>>() {
            @Override
            public void onSuccess(Response<ResponseBean<ChainInfoBean.DataBean>> response) {
                if (response.body().code == 0) {
                    view.getChainInfoHttp(response.body().data);
                } else {
                    view.getDataHttpFail(response.body().message);
                }
            }
        });
    }

    public void getJson(PostChainAnswerJsonBean postChainAnswerJsonBean) {
        HttpUtils.postRequest(BaseUrl.HTTP_get_abi_json_to_bin, mContext, new Gson().toJson(postChainAnswerJsonBean), new JsonCallback<ResponseBean<GetChainJsonBean.DataBean>>() {
            @Override
            public void onSuccess(Response<ResponseBean<GetChainJsonBean.DataBean>> response) {
                if (response.body().code == 0) {
                    view.getChainJsonHttp(response.body().data);
                } else {
                    view.getDataHttpFail(response.body().message);
                }
            }
        });
    }

    public void get_required_keys(PostChainPublicKeyBean postChainPublicKeyBean) {
        HttpUtils.postRequest(BaseUrl.HTTP_get_required_keys, mContext, new Gson().toJson(postChainPublicKeyBean), new JsonCallback<ResponseBean<GetRequiredKeysBean.DataBean>>() {
            @Override
            public void onSuccess(Response<ResponseBean<GetRequiredKeysBean.DataBean>> response) {
                if (response.body().code == 0) {
                    view.getRequiredKeysHttp(response.body().data);
                } else {
                    view.getDataHttpFail(response.body().message);
                }
            }
        });
    }

    public void push_transaction(PostChainPublicKeyBean.TransactionBean transactionBean) {
        HttpUtils.postRequest(BaseUrl.HTTP_push_transaction, mContext, new Gson().toJson(transactionBean), new JsonCallback<ResponseBean<TransferSuccessBean.DataBeanX>>() {
            @Override
            public void onSuccess(Response<ResponseBean<TransferSuccessBean.DataBeanX>> response) {
                if (response.body().code == 0) {
                    view.pushtransactionHttp();
                } else {
                    view.getDataHttpFail(response.body().message);
                }
            }
        });
    }
}
