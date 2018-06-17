package com.sitexa.lover.modules.leftdrawer.messagecenter;

import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.bean.MessageCenterBean;

import java.util.List;

/**
 * Created By sitexa on 2018/6/17
 * 获取friendslist
 */
public interface MessageCenterView extends BaseView {

    void getListDataHttp(List<MessageCenterBean.DataBean> messageBean);

    void getDataHttpFail(String msg);
}
