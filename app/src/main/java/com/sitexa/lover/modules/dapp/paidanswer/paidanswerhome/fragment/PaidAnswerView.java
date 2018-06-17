package com.sitexa.lover.modules.dapp.paidanswer.paidanswerhome.fragment;

import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.bean.PaidAnswerBean;

/**
 * Created By sitexa on 2018/6/17
 * 获取有问必答问题列表
 */
public interface PaidAnswerView extends BaseView {

    void getQuestionListDataHttp(PaidAnswerBean.DataBeanX paidAnswerBean);

    void getDataHttpFail(String msg);
}
