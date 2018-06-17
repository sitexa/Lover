package com.sitexa.lover.modules.transaction.transferaccounts.switchfriend;

import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.bean.FriendsListInfoBean;

import java.util.List;

/**
 * Created By sitexa on 2018/6/17
 * 获取friendslist
 */

public interface SwitchFriendView extends BaseView {

    void getDataHttp(List<FriendsListInfoBean> dataBeanList);


    void getDataHttpFail(String msg);
}
