package com.sitexa.lover.modules.friendslist.myfriendslist;

import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.bean.User;

import java.util.ArrayList;

/**
 * Created By sitexa on 2018/6/17
 * 获取friendslist
 */

public interface FriendsListView extends BaseView {

    void getDataHttp(ArrayList<User> mDataBeanArrayList);


    void getDataHttpFail(String msg);
}
