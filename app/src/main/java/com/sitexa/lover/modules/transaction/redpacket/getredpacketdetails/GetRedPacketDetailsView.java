package com.sitexa.lover.modules.transaction.redpacket.getredpacketdetails;

import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.bean.RedPacketDetailsBean;
import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.bean.RedPacketDetailsBean;

/**
 * Created By sitexa on 2018/6/17
 */
public interface GetRedPacketDetailsView extends BaseView {
    void getRedPacketDetailsDataHttp(RedPacketDetailsBean.DataBean dataBean);

    void getDataHttpFail(String msg);
}
