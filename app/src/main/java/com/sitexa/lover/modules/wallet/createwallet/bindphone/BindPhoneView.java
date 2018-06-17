package com.sitexa.lover.modules.wallet.createwallet.bindphone;

import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.bean.CodeAuthBean;

/**
 * Created by pocketEos on 2018/1/18.
 */

public interface BindPhoneView extends BaseView {

    void getCodeDataHttp(String msg);

    void getCodeAuthDataHttp(CodeAuthBean.DataBean codeAuthBean);

    void getDataHttpFail(String msg);
}
