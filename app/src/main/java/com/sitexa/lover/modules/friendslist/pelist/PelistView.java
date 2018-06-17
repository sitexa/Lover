package com.sitexa.lover.modules.friendslist.pelist;

import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.bean.PelistBean;

import java.util.List;

/**
 * Created By sitexa on 2018/6/17
 * 获取friendslist
 */
public interface PelistView extends BaseView {

    void getListDataHttp(List<PelistBean.DataBean> pelistBean);

    void getDataHttpFail(String msg);
}
