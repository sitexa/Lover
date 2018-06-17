package com.sitexa.lover.modules.wallet.createwallet.login;

import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.bean.CodeAuthBean;

/**
 * Created by pocketEos on 2018/1/18.
 */

public interface LoginView extends BaseView {

    void getCodeDataHttp(String message);

    void getCodeAuthDataHttp(CodeAuthBean.DataBean codeAuthBean);

    void getDataHttpFail(String msg);
}
