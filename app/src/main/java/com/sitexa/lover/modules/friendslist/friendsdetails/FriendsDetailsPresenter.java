package com.sitexa.lover.modules.friendslist.friendsdetails;

import android.content.Context;

import com.lzy.okgo.model.Response;
import com.sitexa.lover.app.MyApplication;
import com.sitexa.lover.base.BasePresenter;
import com.sitexa.lover.base.BaseUrl;
import com.sitexa.lover.bean.AccountWithCoinBean;
import com.sitexa.lover.net.HttpUtils;
import com.sitexa.lover.net.callbck.JsonCallback;
import com.sitexa.lover.bean.AccountDetailsBean;
import com.sitexa.lover.bean.WalletDetailsBean;
import com.sitexa.lover.bean.ResponseBean;
import com.sitexa.lover.utils.RegexUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pocketEos on 2018/1/18.
 */

public class FriendsDetailsPresenter extends BasePresenter<FriendsDetailsView> {
    private Context mContext;

    public FriendsDetailsPresenter(Context context) {
        this.mContext = context;
    }

    public void getAccountDetailsData(final String name) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("name", name);
        HttpUtils.postRequest(BaseUrl.HTTP_eos_get_account, mContext, hashMap, new JsonCallback<ResponseBean<AccountDetailsBean>>() {
            @Override
            public void onSuccess(Response<ResponseBean<AccountDetailsBean>> response) {
                if (response.body().code == 0) {
                    if (response.body().data.getAccount_name().equals(name)) {
                        List<AccountWithCoinBean> accountWithCoinBeens = new ArrayList<>();
                        AccountWithCoinBean eos = new AccountWithCoinBean();
                        eos.setCoinName("EOS");
                        eos.setCoinForCny(RegexUtil.subZeroAndDot(response.body().data.getEos_balance_cny()));
                        eos.setCoinNumber(RegexUtil.subZeroAndDot(response.body().data.getEos_balance()));
                        eos.setCoinImg(response.body().data.getAccount_icon());
                        eos.setEos_market_cap_usd(response.body().data.getEos_market_cap_usd());
                        eos.setEos_market_cap_cny(response.body().data.getEos_market_cap_cny());
                        eos.setEos_price_cny(response.body().data.getEos_price_cny());
                        if (response.body().data.getEos_price_change_in_24h().contains("-")) {
                            eos.setCoinUpsAndDowns(response.body().data.getEos_price_change_in_24h() + "%");
                        } else {
                            eos.setCoinUpsAndDowns("+" + response.body().data.getEos_price_change_in_24h() + "%");
                        }
                        accountWithCoinBeens.add(eos);
                        AccountWithCoinBean oct = new AccountWithCoinBean();
                        oct.setCoinName("OCT");
                        oct.setCoinForCny(RegexUtil.subZeroAndDot(response.body().data.getOct_balance_cny()));
                        oct.setCoinNumber(RegexUtil.subZeroAndDot(response.body().data.getOct_balance()));
                        oct.setCoinImg(response.body().data.getAccount_icon());
                        oct.setOct_market_cap_cny(response.body().data.getOct_market_cap_cny());
                        oct.setOct_market_cap_usd(response.body().data.getOct_market_cap_usd());
                        oct.setOct_price_cny(response.body().data.getOct_price_cny());
                        if (response.body().data.getOct_price_change_in_24h().contains("-")) {
                            oct.setCoinUpsAndDowns(response.body().data.getOct_price_change_in_24h() + "%");
                        } else {
                            oct.setCoinUpsAndDowns("+" + response.body().data.getOct_price_change_in_24h() + "%");
                        }
                        accountWithCoinBeens.add(oct);
                        view.getAccountDetailsDataHttp(accountWithCoinBeens);
                    }
                } else {
                    view.getDataHttpFail(response.body().message);
                }
            }
        });
    }

    public void getWalletDetailsData(String uid) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("uid", uid);
        HttpUtils.postRequest(BaseUrl.HTTP_FriendsDetaislist, mContext, hashMap, new JsonCallback<ResponseBean<List<WalletDetailsBean.DataBean>>>() {
            @Override
            public void onSuccess(Response<ResponseBean<List<WalletDetailsBean.DataBean>>> response) {
                if (response.body().code == 0) {
                    view.getWalletDetailsDataHttp(response.body().data);
                } else {
                    view.getDataHttpFail(response.body().message);
                }
            }
        });
    }

    public void AddFollowData(String followType, String uid, String followTarget) {//添加关注

        HashMap<String, String> params = new HashMap<>();
        params.put("fuid", MyApplication.getInstance().getUserBean().getWallet_uid());
        params.put("followType", followType);
        if (followType.equals("1")) {
            params.put("uid", uid);
        } else {
            params.put("followTarget", followTarget);
        }
        params.put("followTarget", followTarget);
        HttpUtils.postRequest(BaseUrl.HTTP_add_follow, mContext, params, new JsonCallback<ResponseBean<String>>() {
            @Override
            public void onSuccess(Response<ResponseBean<String>> response) {
                if (response.body().code == 0) {
                    view.getAddFollowsDataHttp();
                } else {
                    view.getDataHttpFail(response.body().message);
                }
            }
        });

    }


    public void CancelFollowData(String followType, String uid, String followTarget) {
        HashMap<String, String> params = new HashMap<>();
        params.put("fuid", MyApplication.getInstance().getUserBean().getWallet_uid());
        params.put("followType", followType);
        if (followType.equals("1")) {
            params.put("uid", uid);
        } else {
            params.put("followTarget", followTarget);
        }
        params.put("followTarget", followTarget);
        HttpUtils.postRequest(BaseUrl.HTTP_cancel_follow, mContext, params, new JsonCallback<ResponseBean<String>>() {
            @Override
            public void onSuccess(Response<ResponseBean<String>> response) {
                if (response.body().code == 0) {
                    view.getCancelFollow();
                } else {
                    view.getDataHttpFail(response.body().message);
                }
            }
        });
    }

    public void IsFollow(String followType, String uid) {
        HashMap<String, String> params = new HashMap<>();
        params.put("fuid", MyApplication.getInstance().getUserBean().getWallet_uid());
        params.put("followType", followType);
        params.put("uid", uid);
        HttpUtils.postRequest(BaseUrl.HTTP_is_follow, mContext, params, new JsonCallback<ResponseBean<Boolean>>() {
            @Override
            public void onSuccess(Response<ResponseBean<Boolean>> response) {
                if (response.body().code == 0) {
                    view.isfollow(response.body().data);
                } else {
                    view.getDataHttpFail(response.body().message);
                }
            }
        });
    }
}

