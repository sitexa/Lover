package com.sitexa.lover.blockchain;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.utils.OkLogger;
import com.sitexa.lover.app.ActivityUtils;
import com.sitexa.lover.base.BaseUrl;
import com.sitexa.lover.base.Constants;
import com.sitexa.lover.blockchain.api.EosChainInfo;
import com.sitexa.lover.blockchain.bean.GetRequiredKeys;
import com.sitexa.lover.blockchain.bean.JsonToBeanResultBean;
import com.sitexa.lover.blockchain.bean.JsonToBinRequest;
import com.sitexa.lover.blockchain.bean.PushSuccessBean;
import com.sitexa.lover.blockchain.bean.RequireKeyResult;
import com.sitexa.lover.blockchain.chain.Action;
import com.sitexa.lover.blockchain.chain.PackedTransaction;
import com.sitexa.lover.blockchain.chain.SignedTransaction;
import com.sitexa.lover.blockchain.cypto.ec.EosPrivateKey;
import com.sitexa.lover.blockchain.types.TypeChainId;
import com.sitexa.lover.blockchain.util.GsonEosTypeAdapterFactory;
import com.sitexa.lover.net.HttpUtils;
import com.sitexa.lover.net.callbck.JsonCallback;
import com.sitexa.lover.R;
import com.sitexa.lover.bean.ResponseBean;
import com.sitexa.lover.bean.SendRedPacketBean;
import com.sitexa.lover.modules.transaction.redpacket.anticipationredpacket.AnticipationRedPacketActivity;
import com.sitexa.lover.utils.BigDecimalUtil;
import com.sitexa.lover.utils.PublicAndPrivateKeyUtils;
import com.sitexa.lover.utils.ShowDialog;
import com.sitexa.lover.utils.ToastUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pocketEos on 2018/5/2.
 * eosX适配
 */

public class EosDataManager {
    static String EOSCONTRACT = Constants.EOSCONTRACT;
    static String OCTCONTRACT =  Constants.OCTCONTRACT;//erctoken
    static String ACTIONTRANSFER = Constants.ACTIONTRANSFER;
    static String PERMISSONION = Constants.PERMISSONION;

    Context mContext;
    EosChainInfo mChainInfoBean = new EosChainInfo();
    JsonToBeanResultBean mJsonToBeanResultBean = new JsonToBeanResultBean();
    String[] permissions;
    SignedTransaction txnBeforeSign;
    Gson mGson = new GsonBuilder()
            .registerTypeAdapterFactory(new GsonEosTypeAdapterFactory())
            .excludeFieldsWithoutExposeAnnotation().create();

    String contract, action, message, userpassword;
    int type = 1; //0即为红包 ， 1为转账
    SendRedPacketBean.DataBean redpacketInfo = new SendRedPacketBean.DataBean(); // 红包信息
    String redpacketNumber = null;
    BigDecimal coinRate;//资产汇率

    public EosDataManager(Context context, String password) {
        mContext = context;
        this.userpassword = password;
    }

    public static EosPrivateKey[] getPrivateKey(int count) {
        EosPrivateKey[] retKeys = new EosPrivateKey[count];
        for (int i = 0; i < count; i++) {
            retKeys[i] = new EosPrivateKey();
        }

        return retKeys;
    }

    public void pushAction(String message, String permissionAccount) {
        this.message = message;
        if (message.contains("EOS")) {
            this.contract = EOSCONTRACT;
        } else {
            this.contract = OCTCONTRACT;
        }
        this.action = ACTIONTRANSFER;
        this.type = 1;
        permissions = new String[]{permissionAccount + "@" + PERMISSONION};
        getChainInfo();
    }

    public void getChainInfo() {
        HttpUtils.getRequets(BaseUrl.HTTP_get_chain_info, this, new HashMap<String, String>(), new JsonCallback<ResponseBean<EosChainInfo>>() {
            @Override
            public void onSuccess(Response<ResponseBean<EosChainInfo>> response) {
                if (response.body().code == 0) {
                    mChainInfoBean = response.body().data;
                    getabi_json_to_bin();
                } else {
                    if (ShowDialog.dialog != null) {
                        ShowDialog.dissmiss();
                    }
                    ToastUtils.showLongToast(response.body().message);
                }
            }
        });
    }

    public void getabi_json_to_bin() {
        OkLogger.i("===============>message" + message);
        JsonToBinRequest jsonToBinRequest = new JsonToBinRequest(contract, action, message.replaceAll("\\r|\\n", ""));
        HttpUtils.postRequest(BaseUrl.HTTP_get_abi_json_to_bin, this, mGson.toJson(jsonToBinRequest), new JsonCallback<ResponseBean<JsonToBeanResultBean>>() {
            @Override
            public void onSuccess(Response<ResponseBean<JsonToBeanResultBean>> response) {
                if (response.body().code == 0) {
                    mJsonToBeanResultBean = response.body().data;
                    txnBeforeSign = createTransaction(contract, action, mJsonToBeanResultBean.getBinargs(), permissions, mChainInfoBean);
                    //扫描钱包列出所有可用账号的公钥
                    List<String> pubKey =  PublicAndPrivateKeyUtils.getActivePublicKey();

                    getRequreKey(new GetRequiredKeys(txnBeforeSign, pubKey));
                } else {
                    if (ShowDialog.dialog != null) {
                        ShowDialog.dissmiss();
                    }
                    ToastUtils.showLongToast(response.body().message);
                }
            }
        });
    }

    private SignedTransaction createTransaction(String contract, String actionName, String dataAsHex,
                                                String[] permissions, EosChainInfo chainInfo) {

        Action action = new Action(contract, actionName);
        action.setAuthorization(permissions);
        action.setData(dataAsHex);


        SignedTransaction txn = new SignedTransaction();
        txn.addAction(action);
        txn.putSignatures(new ArrayList<String>());


        if (null != chainInfo) {
            txn.setReferenceBlock(chainInfo.getHeadBlockId());
            txn.setExpiration(chainInfo.getTimeAfterHeadBlockTime(30000));
        }
        return txn;
    }

    public void getRequreKey(GetRequiredKeys getRequiredKeys) {

        HttpUtils.postRequest(BaseUrl.HTTP_get_required_keys, this, mGson.toJson(getRequiredKeys), new JsonCallback<ResponseBean<RequireKeyResult>>() {
            @Override
            public void onSuccess(Response<ResponseBean<RequireKeyResult>> response) {
                if (response.body().code == 0) {
                    EosPrivateKey eosPrivateKey = new EosPrivateKey(PublicAndPrivateKeyUtils.getPrivateKey(response.body().data.getRequired_keys().get(0), userpassword));
                    txnBeforeSign.sign(eosPrivateKey, new TypeChainId(mChainInfoBean.getChain_id()));
                    pushTransactionRetJson(new PackedTransaction(txnBeforeSign));
                } else {
                    if (ShowDialog.dialog != null) {
                        ShowDialog.dissmiss();
                    }
                    ToastUtils.showLongToast(response.body().message);
                }
            }
        });

    }

    public void pushTransactionRetJson(PackedTransaction body) {
        HttpUtils.postRequest(BaseUrl.HTTP_push_transaction, this, mGson.toJson(body), new JsonCallback<ResponseBean<PushSuccessBean.DataBeanX>>() {
            @Override
            public void onSuccess(final Response<ResponseBean<PushSuccessBean.DataBeanX>> response) {
                if (ShowDialog.dialog != null) {
                    ShowDialog.dissmiss();
                }
                if (response.body().code == 0) {
                    final Bundle bundle = new Bundle();
                    if (type == 1) {
                        ToastUtils.showLongToast(R.string.transfersuccess);
                        bundle.putString("account", response.body().data.getProcessed().getAction_traces().get(0).getAct().getData().getFrom());
                        bundle.putString("coin_type", response.body().data.getProcessed().getAction_traces().get(0).getAct().getData().getQuantity().split(" ")[1]);
                        bundle.putString("coin_number", response.body().data.getProcessed().getAction_traces().get(0).getAct().getData().getQuantity().split(" ")[0]);
                        bundle.putString("coin_cny", BigDecimalUtil.multiply(BigDecimal.valueOf(Double.parseDouble(response.body().data.getProcessed().getAction_traces().get(0).getAct().getData().getQuantity().split(" ")[0])), coinRate, 4) + "");
                        ActivityUtils.goBackWithResult((Activity) mContext, 300, bundle);
                    } else {
                        if (redpacketInfo != null) {
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    bundle.putString("account", response.body().data.getProcessed().getAction_traces().get(0).getAct().getData().getFrom());
                                    bundle.putString("redpacketNumber", redpacketNumber);
                                    bundle.putString("amount", response.body().data.getProcessed().getAction_traces().get(0).getAct().getData().getQuantity().split(" ")[0]);
                                    bundle.putString("transferCode", response.body().data.getProcessed().getAction_traces().get(0).getAct().getData().getQuantity().split(" ")[1]);
                                    bundle.putParcelable("info", redpacketInfo);
                                    bundle.putString("memo", response.body().data.getProcessed().getAction_traces().get(0).getAct().getData().getMemo());
                                    bundle.putString("txtid", response.body().data.getTransaction_id());
                                    ActivityUtils.next((Activity) mContext, AnticipationRedPacketActivity.class, bundle);
                                }
                            }, 1000);
                        }
                    }
                } else {
                    ToastUtils.showLongToast(response.body().message);
                }
            }
        });
    }

    public void sendRedPacket(String message, String permissionAccount) {
        this.message = message;
        if (message.contains("EOS")) {
            this.contract = EOSCONTRACT;
        } else {
            this.contract = OCTCONTRACT;
        }
        this.action = ACTIONTRANSFER;
        this.type = 0;
        permissions = new String[]{permissionAccount + "@" + PERMISSONION};
        getChainInfo();
    }

    public EosDataManager setRedpacketInfo(SendRedPacketBean.DataBean dataBean, String number) {
        this.redpacketInfo = dataBean;
        this.redpacketNumber = number;
        return this;
    }

    public EosDataManager setCoinRate(BigDecimal coinRate) {
        this.coinRate = coinRate;
        return this;
    }
}
