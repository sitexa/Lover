package com.sitexa.lover.modules.coindetails;

import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.bean.SparkLinesBean;
import com.sitexa.lover.bean.TransferHistoryBean;

/**
 * Created By sitexa on 2018/6/17
 */
public interface CoinDetailsView extends BaseView {


    void getTransferHistoryDataHttp(TransferHistoryBean.DataBeanX transferHistoryBean);

    void getSparklinesData(SparkLinesBean.DataBean dataBean);


    void getDataHttpFail(String msg);
}
