package com.sitexa.lover.modules.blackbox.blackhome;

import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.bean.AccountWithCoinBean;
import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.bean.AccountWithCoinBean;

import java.util.List;

/**
 * Created by pocketEos on 2018/1/18.
 */

public interface BlackBoxHomeView extends BaseView {

    void getAccountDetailsDataHttp(List<AccountWithCoinBean> mAccountWithCoinBeen);

    void getDataHttpFail(String msg);
}
