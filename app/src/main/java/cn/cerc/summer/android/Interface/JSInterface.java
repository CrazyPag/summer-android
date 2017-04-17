package cn.cerc.summer.android.Interface;

import android.content.pm.PackageManager;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.xutils.http.annotation.HttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.cerc.summer.android.MyApplication;
import cn.cerc.summer.android.MyConfig;
import cn.cerc.summer.android.Utils.AppUtil;

import static java.lang.System.in;
import static java.net.Proxy.Type.HTTP;

/**
 * 供js调用的js
 * Created by fff on 2016/11/11.
 */
public class JSInterface extends Object {
    private Map<String, String> resultunifiedorder;
    private StringBuffer sb;
    private String appid;

    private JSInterfaceLintener jsInterfaceLintener;

    public JSInterface(JSInterfaceLintener jsInterfaceLintener) throws IOException {
        this.jsInterfaceLintener = jsInterfaceLintener;
    }

    public String hello2Html() {
        return "Hello Html";
    }

    /**
     * 返回当前的版本号
     *
     * @return
     */
    public int getVersion() {
        try {
            return AppUtil.getVersionCode(jsInterfaceLintener.getContext());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private PayReq req;
    private IWXAPI msgApi;

    /**
     * 供html调用 微信支付
     *
     * @param appId     app id
     * @param partnerId 商户号
     * @param prepayId  与支付单号
     * @param nonceStr  随机码
     * @param timeStamp 时间戳
     * @param sign      签名
     */
    @JavascriptInterface
    public void wxPay(String appId, String partnerId, String prepayId, String nonceStr, String timeStamp, String sign) {
        Toast.makeText(jsInterfaceLintener.getContext(), "正在支付，请等待...", Toast.LENGTH_SHORT).show();
        Log.e("JSInterface", appId + " " + partnerId + " " + prepayId + " " + nonceStr + " " + timeStamp + " " + sign);
        msgApi = WXAPIFactory.createWXAPI(jsInterfaceLintener.getContext(), appId);
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


    /**
     * 登陆
     */
    @JavascriptInterface
    public void login() {
        String loginUrl = MyConfig.HOME_URL+"/forms/Login.exit";
        login(loginUrl);
    }

    @JavascriptInterface
    public void login(String loginUrl) {
        jsInterfaceLintener.LoginOrLogout(true, loginUrl);
    }

    /*
        微信登录
     */
    @JavascriptInterface
    public void wxLogin() {
        Log.e("WXLogin","take it");
        if(msgApi==null){
            msgApi = WXAPIFactory.createWXAPI(jsInterfaceLintener.getContext(), MyConfig.WX_appId);
            msgApi.registerApp(MyConfig.WX_appId);
        }
        SendAuth.Req req =new SendAuth.Req();
        req.scope ="snsapi_userinfo";
        req.state ="1245";
        msgApi.sendReq(req);
    }

    /**
     * 隐藏返回按钮
     */
    @JavascriptInterface
    public void hideBack(String loginUrl) {
        jsInterfaceLintener.showBack(true);
    }
    /**
     * 显示右上角文字按键
     */
    @JavascriptInterface
    public void showBtn(String text,String callback) {
        Log.e("showBtn", "执行");
        jsInterfaceLintener.showBtn(text, callback, true);
    }


    /**
     * 退出
     */
    @JavascriptInterface
    public void logout() {
        jsInterfaceLintener.LoginOrLogout(false, "");
    }

    /**
     * 拍照
     * @param json  json格式的
     */
    @JavascriptInterface
    public void paizhao(String json){
        String action = JSON.parseObject(json).getString("action");
        jsInterfaceLintener.Action(json, action);
    }
    /**
     * 打开或关闭刷新手势监控
     * @param flag  true 打开，fasle 关闭
     */
    @JavascriptInterface
    public void pullrefresh(boolean flag){

        jsInterfaceLintener.pullrefresh(flag);
    }

    /**
     * 播放声音
     * @param json
     */
    @JavascriptInterface
    public void soundplay(String json){
        String action = JSON.parseObject(json).getString("action");
        jsInterfaceLintener.Action(json, action);
    }

    /**
     * 扫码
     */
    @JavascriptInterface
    public void zxing(){
//        String action = JSON.parseObject(json).getString("action");
        String action = "zxing";
        Log.e("zxing", "zxing");
        jsInterfaceLintener.Action("", action);
    }
    /**
     * 版本更新
     */
    @JavascriptInterface
    public void version(){
//        String action = JSON.parseObject(json).getString("action");
        String action = "version";
        Log.e("version", "update");
        jsInterfaceLintener.Action("", action);
    }

    /**
     * 打电话
     * @param phone
     */
    @JavascriptInterface
    public void callphone(String phone){
        jsInterfaceLintener.Action(phone, "call");
    }

    /**
     * 扫卡
     */
    @JavascriptInterface
    public void scancard(){
        String action = "card";
        jsInterfaceLintener.Action("", action);
    }


    /**
     * 显示图片
     */
    @JavascriptInterface
    public void showimage(String imagepath){
        jsInterfaceLintener.showImage(imagepath);
    }

    /*
    下载文件
     */
    @JavascriptInterface
    public void downfile(String url){
        jsInterfaceLintener.Action(url,"File");
    }
    /**
     * 显示外部的URL
     */
    @JavascriptInterface
    public void openAd(String url){
        jsInterfaceLintener.openAd(url);
    }

    /**
     *
     * 获取图表URL
     */
    @JavascriptInterface
    public void tabledata(String json){
        jsInterfaceLintener.Action(json,"TABLE");
    }


//客户端从服务器端获取json数据
    //利用HttpUrlConnection
    /**
     * 从指定的URL中获取数组
     * @param urlPath
     * @return
     * @throws Exception
     */
    public static String readParse(String urlPath) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        URL url = new URL("http://ehealth.lucland.com/forms/introduce");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStream inStream = conn.getInputStream();
        while ((len = inStream.read(data)) != -1) {
            outStream.write(data, 0, len);
        }
        inStream.close();
        return new String(outStream.toByteArray());//通过out.Stream.toByteArray获取到写的数据
    }



    /**
     * json数据解析
     *
     * @throws JSONException
     */





}
