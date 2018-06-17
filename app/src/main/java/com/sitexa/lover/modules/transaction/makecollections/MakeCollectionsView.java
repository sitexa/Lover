package com.sitexa.lover.modules.transaction.makecollections;

import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.bean.CoinRateBean;
import com.sitexa.lover.bean.TransferHistoryBean;

/**
 * Created By sitexa on 2018/6/17
 */
public interface MakeCollectionsView extends BaseView {
    void getCoinRateDataHttp(CoinRateBean.DataBean coinRateBean);

    void getTransferHistoryDataHttp(TransferHistoryBean.DataBeanX transferHistoryBean);


    void getDataHttpFail(String msg);
}
