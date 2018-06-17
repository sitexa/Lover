package com.sitexa.lover.modules.coindetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.widget.SpringView;
import com.sitexa.lover.app.ActivityUtils;
import com.sitexa.lover.app.MyApplication;
import com.sitexa.lover.base.BaseActivity;
import com.sitexa.lover.bean.AccountWithCoinBean;
import com.sitexa.lover.modules.otherloginorshare.BaseUIListener;
import com.sitexa.lover.modules.otherloginorshare.WxShareAndLoginUtils;
import com.sitexa.lover.modules.transaction.makecollections.MakeCollectionsActivity;
import com.sitexa.lover.modules.transaction.transferaccounts.TransferAccountsActivity;
import com.sitexa.lover.R;
import com.sitexa.lover.adapter.AdapterManger;
import com.sitexa.lover.adapter.baseadapter.wrapper.EmptyWrapper;
import com.sitexa.lover.bean.PostChainHistoryBean;
import com.sitexa.lover.bean.SparkLinesBean;
import com.sitexa.lover.bean.TransferHistoryBean;
import com.sitexa.lover.modules.empty.EmptyActivity;
import com.sitexa.lover.utils.BigDecimalUtil;
import com.sitexa.lover.utils.ChartUtil;
import com.sitexa.lover.utils.StringUtils;
import com.sitexa.lover.view.RecycleViewDivider;
import com.sitexa.lover.view.dialog.sharecoindetailsdialog.ShareCoinDetailsCallback;
import com.sitexa.lover.view.dialog.sharecoindetailsdialog.ShareCoinDetailsDialog;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzonePublish;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.Tencent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.sitexa.lover.utils.Utils.getContext;

//币种详情页
public class CoinDetailsActivity extends BaseActivity<CoinDetailsView, CoinDetailsPresenter> implements CoinDetailsView {


    @BindView(R.id.lineChart)
    LineChart mChart;

    List<Entry> yDataList = new ArrayList<>();// 数据源

    @BindView(R.id.img_right)
    ImageView mImgRight;
    @BindView(R.id.icon_total_number)
    TextView mIconTotalNumber;
    @BindView(R.id.coin_user_number)
    TextView mCoinUserNumber;
    @BindView(R.id.icon_number)
    TextView mIconNumber;
    @BindView(R.id.icon_rmb_number)
    TextView mIconRmbNumber;
    @BindView(R.id.go_transfer_accounts)
    TextView mGoTransferAccounts;
    @BindView(R.id.go_make_collections)
    TextView mGoMakeCollections;
    @BindView(R.id.go_red_packet)
    TextView mGoRedPacket;
    @BindView(R.id.recycle_coin_history)
    RecyclerView mRecycleCoinHistory;
    @BindView(R.id.spring)
    SpringView mSpring;
    @BindView(R.id.coin_uoanddown)
    TextView mCoinUoanddown;
    @BindView(R.id.coin_upanddown_img)
    ImageView mCoinUpanddownImg;
    @BindView(R.id.coin_maket_cap)
    TextView mCoinMaketCap;
    ShareCoinDetailsDialog dialog = null;
    private AccountWithCoinBean accountWithCoinBean = new AccountWithCoinBean();
    private List<TransferHistoryBean.DataBeanX.TransactionsBean> mDataBeanList = new ArrayList<>();//交易历史
    private EmptyWrapper mHistoryAdapter;
    private int size = 10; //每页加载的数量
    private PostChainHistoryBean mPostChainHistoryBean = new PostChainHistoryBean();
    private String cointype = "eos";
    private int skip_seq = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_icon_type;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(CoinDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setSmoothScrollbarEnabled(true);
        mRecycleCoinHistory.setLayoutManager(layoutManager);
        mRecycleCoinHistory.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL, 1, getResources().getColor(R.color.line)));

        accountWithCoinBean = getIntent().getParcelableExtra("coin");
        if (accountWithCoinBean.getCoinName().equals("EOS")) {
            cointype = "eos";
        } else {
            cointype = "oct";
        }
        setCenterTitle(accountWithCoinBean.getCoinName());
        setRightImg(true);
        mImgRight.setImageDrawable(getResources().getDrawable(R.mipmap.makecollectionshare));
        mIconTotalNumber.setText(StringUtils.addComma(accountWithCoinBean.getCoinForCny()) + " CNY");
        mCoinUoanddown.setText(accountWithCoinBean.getCoinUpsAndDowns() + getString(R.string.today));
        mCoinUserNumber.setText(getIntent().getStringExtra("account"));
        mIconNumber.setText(StringUtils.addComma(accountWithCoinBean.getCoinNumber()) + " " + accountWithCoinBean.getCoinName());
        mIconRmbNumber.setText("≈" + StringUtils.addComma(accountWithCoinBean.getCoinForCny()) + " CNY");

        if (accountWithCoinBean.getCoinName().equals("EOS")) {
            mCoinMaketCap.setText(getString(R.string.rated_24) + accountWithCoinBean.getEos_market_cap_cny() + "CNY");
        } else {
            mCoinMaketCap.setText(getString(R.string.rated_24) + accountWithCoinBean.getOct_market_cap_cny() + "CNY");
        }
        //系统刷新
        mSpring.setFooter(new AliFooter(this));
        mSpring.setGive(SpringView.Give.BOTTOM);
        mSpring.setType(SpringView.Type.FOLLOW);
        mSpring.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                mSpring.onFinishFreshAndLoad();
            }

            @Override
            public void onLoadmore() {
                mPostChainHistoryBean.setSkip_seq(skip_seq);
                presenter.getTransferHistoryData(mPostChainHistoryBean);
            }
        });
    }

    @Override
    protected void initData() {
        //给上面的X、Y轴数据源做假数据测试
        for (int i = 0; i < 20; i++) {
            float mult = 30 / 2f;
            float val = (float) (Math.random() * mult) + 50;
            yDataList.add(new Entry(i, val));
        }
        //显示图表,参数（ 上下文，图表对象数据，图表标题，曲线图例名称，坐标点击弹出提示框中数字单位）
        ChartUtil.showChart(this, mChart, yDataList, "", "", "");

        showProgress();
        presenter.getSparklinesData();

        mPostChainHistoryBean.setAccount_name(mCoinUserNumber.getText().toString());
        mPostChainHistoryBean.setSkip_seq(0);
        mPostChainHistoryBean.setNum_seq(size);
        presenter.getTransferHistoryData(mPostChainHistoryBean);


        mHistoryAdapter = new EmptyWrapper(AdapterManger.getCoinDetailsHistoryAdapter(this, mDataBeanList, mCoinUserNumber.getText().toString()));
        mHistoryAdapter.setEmptyView(R.layout.empty_project);

        mRecycleCoinHistory.setAdapter(mHistoryAdapter);

    }

    @Override
    public void initEvent() {

    }

    @Override
    public CoinDetailsPresenter initPresenter() {
        return new CoinDetailsPresenter(CoinDetailsActivity.this);
    }


    @Override
    public void getTransferHistoryDataHttp(TransferHistoryBean.DataBeanX transferHistoryBean) {
        hideProgress();
        mSpring.onFinishFreshAndLoad();
        skip_seq += transferHistoryBean.getTransactions().size();
        for (int i = 0; i < transferHistoryBean.getTransactions().size(); i++) {
            if (transferHistoryBean.getTransactions().get(i).getTransaction().getTransaction().getActions().get(0).getName().equals("transfer")) {
                if (transferHistoryBean.getTransactions().get(i).getTransaction().getTransaction().getActions().get(0).getData().getQuantity().contains(cointype.toUpperCase())) {
                    TransferHistoryBean.DataBeanX.TransactionsBean itemdata = transferHistoryBean.getTransactions().get(i);
                    mDataBeanList.add(itemdata);
                }
            }
        }
        mHistoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void getSparklinesData(SparkLinesBean.DataBean dataBean) {
        if (accountWithCoinBean.getCoinName().equals("EOS")) {
            MyApplication.getInstance().showImage(dataBean.getSparkline_eos_png(), mCoinUpanddownImg);
        } else {
            MyApplication.getInstance().showImage(dataBean.getSparkline_oct_png(), mCoinUpanddownImg);
        }
    }

    @Override
    public void getDataHttpFail(String msg) {
        hideProgress();
        mSpring.onFinishFreshAndLoad();
        toast(msg);
    }


    @OnClick({R.id.img_right, R.id.go_transfer_accounts, R.id.go_make_collections, R.id.go_red_packet})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.img_right://分享操作
                String desc = null;
                final Bundle params = new Bundle();
                if (accountWithCoinBean.getCoinName().equals("OCT")) {
                    desc = getString(R.string.reference_price_cny) + StringUtils.addComma(accountWithCoinBean.getOct_price_cny()) + getString(R.string.toast_down_up) + accountWithCoinBean.getCoinUpsAndDowns() + getString(R.string.all_market_price)+ StringUtils.addComma(accountWithCoinBean.getOct_market_cap_cny());
                } else {
                    desc =  getString(R.string.reference_price_cny) + StringUtils.addComma(accountWithCoinBean.getEos_price_cny()) + getString(R.string.toast_down_up) + accountWithCoinBean.getCoinUpsAndDowns() + getString(R.string.all_market_price) + StringUtils.addComma(accountWithCoinBean.getEos_market_cap_cny());
                }
                if (dialog == null) {
                    bundle.putParcelable("coin", accountWithCoinBean);
                    final String finalDesc = desc;
                    dialog = new ShareCoinDetailsDialog(CoinDetailsActivity.this, new ShareCoinDetailsCallback() {
                        @Override
                        public void goWeixinFriend() {
                            WxShareAndLoginUtils.WxUrlShare(CoinDetailsActivity.this, "https://pocketeos.com", accountWithCoinBean.getCoinName() + "最新资讯详情", finalDesc,
                                    MyApplication.getInstance().getUserBean().getWallet_img(),
                                    WxShareAndLoginUtils.WECHAT_FRIEND);
                        }

                        @Override
                        public void goWeixinCircle() {
                            WxShareAndLoginUtils.WxUrlShare(CoinDetailsActivity.this, "https://pocketeos.com", accountWithCoinBean.getCoinName() + "最新资讯详情", finalDesc,
                                    MyApplication.getInstance().getUserBean().getWallet_img(),
                                    WxShareAndLoginUtils.WECHAT_MOMENT);
                        }

                        @Override
                        public void goQQFriend() {
                            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                            params.putString(QQShare.SHARE_TO_QQ_TITLE, accountWithCoinBean.getCoinName() + "最新资讯详情");// 标题
                            params.putString(QQShare.SHARE_TO_QQ_SUMMARY, finalDesc);// 摘要
                            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "https://pocketeos.com");// 内容地址
                            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://pocketeos.oss-cn-beijing.aliyuncs.com/yl/pocketeos.png?Expires=1839400748&OSSAccessKeyId=LTAIdWMZ4ikcYbmF&Signature=dAyqIz3DfCq4emFtdUu%2F%2Fq7kQYU%3D");
                            params.putString(QQShare.SHARE_TO_QQ_EXT_INT, "其它附加功能");
                            MyApplication.getInstance().getTencent().shareToQQ(CoinDetailsActivity.this, params, new BaseUIListener(CoinDetailsActivity.this, true));
                        }

                        @Override
                        public void goQzone() {
                            params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
                            params.putString(QzoneShare.SHARE_TO_QQ_TITLE, accountWithCoinBean.getCoinName() + "最新资讯详情");// 标题
                            params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, finalDesc);// 摘要
                            params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "https://pocketeos.com");// 内容地址
                            ArrayList<String> imgUrlList = new ArrayList<>();
                            imgUrlList.add("http://pocketeos.oss-cn-beijing.aliyuncs.com/yl/pocketeos.png?Expires=1839400748&OSSAccessKeyId=LTAIdWMZ4ikcYbmF&Signature=dAyqIz3DfCq4emFtdUu%2F%2Fq7kQYU%3D");
                            params.putStringArrayList(QzonePublish.PUBLISH_TO_QZONE_IMAGE_URL,
                                    imgUrlList);// 图片地址ArrayList
                            MyApplication.getInstance().getTencent().shareToQzone(CoinDetailsActivity.this, params, new BaseUIListener(CoinDetailsActivity.this, true));
                        }
                    });
                    dialog.setContent(bundle);
                    dialog.setCancelable(true);
                    dialog.show();
                } else {
                    dialog.show();
                }

                break;
            case R.id.go_transfer_accounts:
                bundle.putString("account", getIntent().getStringExtra("account"));
                bundle.putString("coin", accountWithCoinBean.getCoinName());
                bundle.putString("from", "coin");
                ActivityUtils.next(CoinDetailsActivity.this, TransferAccountsActivity.class, bundle, 100);
                break;
            case R.id.go_make_collections:
                bundle.putString("account", getIntent().getStringExtra("account"));
                bundle.putString("coin", accountWithCoinBean.getCoinName());
                ActivityUtils.next(CoinDetailsActivity.this, MakeCollectionsActivity.class, bundle);
                break;
            case R.id.go_red_packet:
                ActivityUtils.next(CoinDetailsActivity.this, EmptyActivity.class);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        hideProgress();
//        Tencent.onActivityResultData(requestCode, resultCode, data, new BaseUIListener(CoinDetailsActivity.this, true));
        if (requestCode == Constants.REQUEST_QQ_SHARE || requestCode == Constants.REQUEST_QZONE_SHARE || requestCode == Constants.REQUEST_OLD_SHARE) {
            Tencent.handleResultData(data, new BaseUIListener(CoinDetailsActivity.this, true));
        }
        if (requestCode == 100 && resultCode == 300) {
            if (data.getExtras().getString("coin_type").equals(accountWithCoinBean.getCoinName())) {
                mDataBeanList.clear();
                skip_seq = 0;
                mPostChainHistoryBean.setAccount_name(mCoinUserNumber.getText().toString());
                mPostChainHistoryBean.setSkip_seq(0);
                mPostChainHistoryBean.setNum_seq(size);
                presenter.getTransferHistoryData(mPostChainHistoryBean);
                BigDecimal oldcoinforcny = new BigDecimal(accountWithCoinBean.getCoinForCny());
                BigDecimal oldcoin = new BigDecimal(accountWithCoinBean.getCoinNumber());
                BigDecimal minuscny = new BigDecimal(data.getExtras().getString("coin_cny"));
                BigDecimal minuscoin = new BigDecimal(data.getExtras().getString("coin_number"));
                mIconTotalNumber.setText(StringUtils.addComma(BigDecimalUtil.minus(oldcoinforcny, minuscny, 4) + "") + " CNY");
                mIconNumber.setText(StringUtils.addComma(BigDecimalUtil.minus(oldcoin, minuscoin, 4) + "") + " " + accountWithCoinBean.getCoinName());
                mIconRmbNumber.setText("≈" + StringUtils.addComma(BigDecimalUtil.minus(oldcoinforcny, minuscny, 4) + "") + " CNY");
            }
        }
    }

}
