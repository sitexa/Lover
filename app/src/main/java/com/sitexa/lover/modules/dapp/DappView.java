package com.sitexa.lover.modules.dapp;

import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.bean.DappBean;
import com.sitexa.lover.bean.DappCompanyBean;

import java.util.List;

/**
 * Created By sitexa on 2018/6/17
 * 获取friendslist
 */

public interface DappView extends BaseView {
    void getDappCompanyDataHttp(List<DappCompanyBean.DataBean> dappCommpanyBean);

    void getDappDataHttp(List<DappBean.DataBean> dappBean);

    void getDataHttpFail(String msg);
}
