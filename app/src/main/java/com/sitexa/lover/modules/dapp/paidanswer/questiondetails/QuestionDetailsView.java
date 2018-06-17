package com.sitexa.lover.modules.dapp.paidanswer.questiondetails;

import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.bean.ChainInfoBean;
import com.sitexa.lover.bean.GetChainJsonBean;
import com.sitexa.lover.bean.GetRequiredKeysBean;

/**
 * Created By sitexa on 2018/6/17
 */
public interface QuestionDetailsView extends BaseView {


    void getChainInfoHttp(ChainInfoBean.DataBean chainInfoBean);

    void getChainJsonHttp(GetChainJsonBean.DataBean getChainJsonBean);

    void getRequiredKeysHttp(GetRequiredKeysBean.DataBean getRequiredKeysBean);

    void pushtransactionHttp();

    void getDataHttpFail(String msg);
}
