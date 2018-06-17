package com.sitexa.lover.modules.dapp.dappcompany;

import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.bean.DappBean;

import java.util.List;

/**
 * Created By sitexa on 2018/6/17
 * 获取friendslist
 */
public interface DappCompanyDetailsView extends BaseView {

    void getDappCompanyDataHttp(List<DappBean.DataBean> dappBean);


    void getDataHttpFail(String msg);
}
