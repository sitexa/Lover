package com.sitexa.lover.view.dialog.walletcodedialog;

import android.graphics.Bitmap;

/**
 * Created By sitexa on 2018/6/17
 */

public interface WalletCodeCallback {
    void goWeixinFriend(Bitmap bitmap);

    void goWeixinCircle(Bitmap bitmap);

    void goQQFriend(Bitmap bitmap);

    void goQzone(Bitmap bitmap);
}
