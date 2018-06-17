package com.sitexa.lover.modules.leftdrawer.transactionhistory;

import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.bean.TransferHistoryBean;

/**
 * Created By sitexa on 2018/6/17
 */
public interface TransactionHistoryView extends BaseView {


    void getTransferHistoryDataHttp(TransferHistoryBean.DataBeanX transferHistoryBean);


    void getDataHttpFail(String msg);
}
