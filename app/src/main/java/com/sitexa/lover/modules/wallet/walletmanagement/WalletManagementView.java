package com.sitexa.lover.modules.wallet.walletmanagement;

import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.base.BaseView;
import com.sitexa.lover.bean.BaseBean;

/**
 * Created by pocketEos on 2018/1/18.
 */

public interface WalletManagementView extends BaseView {

    void setPolicyAccountHttp(BaseBean baseBean, int position);


    void getDataHttpFail(String msg);
}
