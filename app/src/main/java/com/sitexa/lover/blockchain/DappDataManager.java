package com.sitexa.lover.blockchain;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lzy.okgo.model.Response;
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
import com.sitexa.lover.bean.ResponseBean;
import com.sitexa.lover.utils.PublicAndPrivateKeyUtils;
import com.sitexa.lover.utils.ShowDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pocketEos on 2018/5/8.
 * Dapp合约调用
 */

public class DappDataManager {
    public final static String EOSCONTRACT =  Constants.EOSCONTRACT;
    public final static String OCTCONTRACT =  Constants.OCTCONTRACT;//erctoken
    public final static String ACTIONTRANSFER = Constants.ACTIONTRANSFER;
    public final static String PERMISSONION = Constants.PERMISSONION;

    Callback mCallback;
    Context mContext;
    EosChainInfo mChainInfoBean = new EosChainInfo();
    JsonToBeanResultBean mJsonToBeanResultBean = new JsonToBeanResultBean();
    String[] permissions;
    SignedTransaction txnBeforeSign;
    Gson mGson = new GsonBuilder()
            .registerTypeAdapterFactory(new GsonEosTypeAdapterFactory())
            .excludeFieldsWithoutExposeAnnotation().create();

    String contract, action, message, userpassword;

    public DappDataManager(Context context, String userpassword, Callback callback) {
        mCallback = callback;
        mContext = context;
        this.userpassword = userpassword;
    }

    public void pushAction(String message, String permissionAccount) {
        this.message = message;
        if (message.contains("EOS")) {
            this.contract = EOSCONTRACT;
        } else {
            this.contract = OCTCONTRACT;
        }
        this.action = ACTIONTRANSFER;
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
                    mCallback.erroMsg(response.body().message);
                }
            }
        });
    }

    public void getabi_json_to_bin() {
        JsonToBinRequest jsonToBinRequest = new JsonToBinRequest(contract, action, message.replaceAll("\\r|\\n", ""));
        HttpUtils.postRequest(BaseUrl.HTTP_get_abi_json_to_bin, this, mGson.toJson(jsonToBinRequest), new JsonCallback<ResponseBean<JsonToBeanResultBean>>() {
            @Override
            public void onSuccess(Response<ResponseBean<JsonToBeanResultBean>> response) {
                if (response.body().code == 0) {
                    mJsonToBeanResultBean = response.body().data;
                    txnBeforeSign = createTransaction(contract, action, mJsonToBeanResultBean.getBinargs(), permissions, mChainInfoBean);
                    //扫描钱包列出所有可用账号的公钥
                    List<String> pubKey = PublicAndPrivateKeyUtils.getActivePublicKey();

                    getRequreKey(new GetRequiredKeys(txnBeforeSign, pubKey));
                } else {
                    if (ShowDialog.dialog != null) {
                        ShowDialog.dissmiss();
                    }
                    mCallback.erroMsg(response.body().message);
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
                    mCallback.erroMsg(response.body().message);
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
                    mCallback.getTxid(response.body().data.getTransaction_id());
                } else {
                    mCallback.erroMsg(response.body().message);
                }
            }
        });
    }

    public interface Callback {
        void getTxid(String txId);

        void erroMsg(String msg);
    }
}
