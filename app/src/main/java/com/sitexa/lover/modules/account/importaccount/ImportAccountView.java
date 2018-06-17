package com.sitexa.lover.modules.account.importaccount;

import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.bean.BlockChainAccountInfoBean;

/**
 * Created By sitexa on 2018/6/17
 */
public interface ImportAccountView extends BaseView {

    void getBlockchainAccountInfoDataHttp(BlockChainAccountInfoBean.DataBean blockChainAccountInfoBean);

    void setMainAccountHttp();

    void getDataHttpFail(String msg);

    void postEosAccountDataHttp();
}
