package com.sitexa.lover.modules.leftdrawer.usercenter;

import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.bean.UpdataPhotoBean;

/**
 * Created by pocketEos on 2018/1/18.
 */

public interface UserCenterView extends BaseView {

    void headImgUploadaDataHttp(UpdataPhotoBean updataPhotoBean);


    void getDataHttpFail(String msg);
}
