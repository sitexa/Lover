package com.sitexa.lover.modules.seach;

import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.bean.BlockChainAccountInfoBean;

/**
 * Created By sitexa on 2018/6/17
 */
public interface SeachView extends BaseView {

    void getBlockchainAccountInfoDataHttp(BlockChainAccountInfoBean.DataBean blockChainAccountInfoBean);

    void getDataHttpFail(String msg);
}
