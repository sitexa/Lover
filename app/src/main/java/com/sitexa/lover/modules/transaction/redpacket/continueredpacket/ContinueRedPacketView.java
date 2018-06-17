package com.sitexa.lover.modules.transaction.redpacket.continueredpacket;

import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.bean.RedPacketDetailsBean;
import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.bean.CoinRateBean;
import com.sitexa.lover.bean.RedPacketDetailsBean;

/**
 * Created By sitexa on 2018/6/17
 */
public interface ContinueRedPacketView extends BaseView {
    void getCoinRateDataHttp(CoinRateBean.DataBean coinRateBean);

    void getRedPacketDetailsDataHttp(RedPacketDetailsBean.DataBean dataBean);

    void getDataHttpFail(String msg);
}
