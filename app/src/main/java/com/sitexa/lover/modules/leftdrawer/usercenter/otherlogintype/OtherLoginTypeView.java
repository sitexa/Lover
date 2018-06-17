package com.sitexa.lover.modules.leftdrawer.usercenter.otherlogintype;

import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.base.BaseView;

/**
 * Created by pocketEos on 2018/1/18.
 */

public interface OtherLoginTypeView extends BaseView {

    void unBindOtherLoginDataHttp();

    void bindOtherLoginDataHttp();

    void getDataHttpFail(String msg);
}
