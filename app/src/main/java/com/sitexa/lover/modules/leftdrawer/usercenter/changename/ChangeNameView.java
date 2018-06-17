package com.sitexa.lover.modules.leftdrawer.usercenter.changename;

import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.base.BaseView;

/**
 * Created by pocketEos on 2018/1/18.
 */

public interface ChangeNameView extends BaseView {

    void updateNameDataHttp();


    void getDataHttpFail(String msg);
}
