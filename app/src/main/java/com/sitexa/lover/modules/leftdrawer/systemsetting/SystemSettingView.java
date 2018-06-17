package com.sitexa.lover.modules.leftdrawer.systemsetting;

import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.bean.SystemInfoBean;

/**
 * Created by pocketEos on 2018/1/18.
 */

public interface SystemSettingView extends BaseView {

    void getSystemInfoHttp(SystemInfoBean.DataBean systemInfoBean, String id);


    void getDataHttpFail(String msg);
}
