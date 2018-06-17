package com.sitexa.lover.modules.transaction.redpacket.anticipationredpacket;

import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.bean.AuthRedPacketBean;

/**
 * Created By sitexa on 2018/6/17
 */
public interface AnticipationRedPacketView extends BaseView {
    void getAuthRedPacketDataHttp(AuthRedPacketBean.DataBean dataBean);

    void getDataHttpFail(String msg);
}
