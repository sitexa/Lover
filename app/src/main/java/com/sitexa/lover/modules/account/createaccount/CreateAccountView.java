package com.sitexa.lover.modules.account.createaccount;

import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.bean.EosRegisterBean;

/**
 * Created by pocketEos on 2018/1/18.
 */

public interface CreateAccountView extends BaseView {

    void getEosRegisterhDataHttp(EosRegisterBean.DataBeanX eosRegisterBean);
    void postEosAccountDataHttp();
    void getDataHttpFail(String msg);
    void setMainAccountHttp();
}
