package cn.cerc.summer.android.Interface;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.Map;

import cn.cerc.summer.android.Utils.AppUtil;

/**
 * Created by fff on 2016/11/11.
 */

public class JSInterface extends Object {
    private Map<String, String> resultunifiedorder;
    private StringBuffer sb;
    private String appid;

    private Context context;

    public JSInterface(Context context) {
        this.context = context;
    }

    public String hello2Html() {
        return "Hello Html";
    }

    /**
     * 返回当前的版本号
     *
     * @return
     */
    public int getVersion(Context context) {
        try {
            return AppUtil.getVersionCode(context);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private PayReq req;
    private IWXAPI msgApi;

    /**
     * 供html调用 微信支付
     * @param appId         app id
     * @param partnerId     商户号
     * @param prepayId      与支付单号
     * @param nonceStr      随机码
     * @param timeStamp     时间戳
     * @param sign          签名
     */
    @JavascriptInterface
    public void wxPay(String appId, String partnerId, String prepayId, String nonceStr, String timeStamp, String sign) {
        Toast.makeText(context, "正在支付，请等待...", Toast.LENGTH_SHORT).show();
        Log.e("JSInterface",appId+" "+partnerId+" "+ prepayId+" "+ nonceStr+" "+ timeStamp+" "+ sign);
        msgApi = WXAPIFactory.createWXAPI(context, appId);
        req = new PayReq();
        req.appId = appId;
        req.partnerId = partnerId;
        req.prepayId = prepayId;
        req.packageValue = "Sign=WXPay";
        req.nonceStr = nonceStr;
        req.timeStamp = timeStamp;
        req.sign = sign;
        msgApi.registerApp(req.appId);
        msgApi.sendReq(req);
    }


}
