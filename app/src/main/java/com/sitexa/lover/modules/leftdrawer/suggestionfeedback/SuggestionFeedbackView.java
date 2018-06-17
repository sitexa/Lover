package com.sitexa.lover.modules.leftdrawer.suggestionfeedback;

import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.bean.SuggestionBean;

import java.util.List;

/**
 * Created By sitexa on 2018/6/17
 * 获取friendslist
 */
public interface SuggestionFeedbackView extends BaseView {

    void postSuggestionHttp();

    void getSuggestionListHttp(List<SuggestionBean.DataBean> suggestionBean);

    void getDataHttpFail(String msg);
}
