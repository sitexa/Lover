package com.sitexa.lover.modules.transaction.transferaccounts;

import android.content.Context;

import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.sitexa.lover.base.BasePresenter;
import com.sitexa.lover.base.BaseUrl;
import com.sitexa.lover.net.HttpUtils;
import com.sitexa.lover.net.callbck.JsonCallback;
import com.sitexa.lover.bean.AccountDetailsBean;
import com.sitexa.lover.bean.CoinRateBean;
import com.sitexa.lover.bean.PostChainHistoryBean;
import com.sitexa.lover.bean.ResponseBean;
import com.sitexa.lover.bean.TransferHistoryBean;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;

/**
 * Created By sitexa on 2018/6/17
 */

public class TransferAccountsPresenter extends BasePresenter<TransferAccountsView> {
    private Context mContext;

    public TransferAccountsPresenter(Context context) {
        this.mContext = context;
    }

    public void getCoinRateData(String coinmarket_id) {//获取token汇率
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("coinmarket_id", coinmarket_id);
        HttpUtils.postRequest(BaseUrl.HTTP_eos_get_coin_rate, mContext, hashMap, new JsonCallback<ResponseBean<CoinRateBean.DataBean>>() {
            @Override
            public void onSuccess(Response<ResponseBean<CoinRateBean.DataBean>> response) {
                if (response.body().code == 0) {
                    view.getCoinRateDataHttp(response.body().data);
                } else {
                    view.getDataHttpFail(response.body().message);
                }
            }
        });
    }

    public void getAccountDetailsData(final String name) {//动态获取账号资产信息
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("name", name);
        HttpUtils.postRequest(BaseUrl.HTTP_eos_get_account, mContext, hashMap, new JsonCallback<ResponseBean<AccountDetailsBean>>() {
            @Override
            public void onSuccess(Response<ResponseBean<AccountDetailsBean>> response) {
                if (response.body().code == 0) {
                    view.getAccountDetailsDataHttp(response.body().data);
                } else {
                    view.getDataHttpFail(response.body().message);
                }
            }
        });
    }


    public void getTransferHistoryData(PostChainHistoryBean postChainHistoryBean) {//获取账号交易历史
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

    public void pushAction(String contract, String action, String message, String scopes, String permissionAccount, String permissionName ){


        // can make
        String[] permissions = ( StringUtils.isEmpty(permissionAccount) || StringUtils.isEmpty( permissionName))
                ? null : new String[]{permissionAccount + "@" + permissionName };


  /*      addDisposable(
                mDataManager.pushAction(contract, action, message.replaceAll("\\r|\\n","")
                        , permissions)
                        .mergeWith( jsonObject -> mDataManager.addAccountHistory( getAccountListForHistory( contract, permissionAccount) ))
                        .subscribeOn( getSchedulerProvider().io())
                        .observeOn( getSchedulerProvider().ui())
                        .subscribeWith(new RxCallbackWrapper<PushTxnResponse>( this) {
                                           @Override
                                           public void onNext(PushTxnResponse result) {
                                               if (!isViewAttached()) return;

                                               getMvpView().showLoading(false);

                                               getMvpView().showResult( Utils.prettyPrintJson( result ), result.toString() );
                                           }
                                       }
                        )*/

    }


}
