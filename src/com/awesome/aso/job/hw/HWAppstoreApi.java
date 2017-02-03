package com.awesome.aso.job.hw;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.awesome.aso.job.util.ApkParserUtils;
import com.awesome.aso.job.util.CommUtils;
import com.awesome.aso.job.util.HWJsonParseUtils;
import com.awesome.aso.job.util.HttpRequestUtils;

public class HWAppstoreApi {

    private HWJobContext context;
    private Proxy proxy;

    public HWAppstoreApi(HWJobContext context, Proxy proxy) {
        super();
        this.context = context;
        this.proxy = proxy;
    }

    public HWAppstoreApi(HWJobContext context) {
        super();
        this.context = context;
    }
    public HWAppstoreApi(HWJobContext context,String proxyHost,int proxyPort) {
        super();
        this.context = context;
        this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
    }
    /**
     * 登录接口，调用其余接口前，要先调用该接口。
     * 
     * @throws Exception
     */
    public void statup() throws Exception {
        String body = RequestParamUtils.getStatup(context);
        String value = HttpRequestUtils.doPostRequest(HWJobContext.URL_STOREAPI3, body, proxy);
        JSONObject obj = (JSONObject) JSONValue.parseWithException(value);
        if (obj != null) {
            String hcrId = (String) obj.get("hcrId");
            String sign = (String) obj.get("sign");
            if (hcrId != null && !"".equals(hcrId) && sign != null && !"".equals(sign)) {
                Map<String, String> tabInfo = HWJsonParseUtils.getTabInfo(obj);
                context.SESSION_CLIENT_HCRID.set(hcrId);
                context.SESSION_CLIENT_SIGN.set(sign);
                context.SESSION_TAB_INFO.set(tabInfo);
            }
        }
    }

    /**
     * 上传安装列表，
     * 
     * @param tagetAppInfo
     *            安装的App信息，没全时为上传用户安装列表。
     * @return
     * @throws Exception
     */
    public JSONObject diffUpgrade(Map<String, String> tagetAppInfo) throws Exception {
        List<Map<String, String>> tagetAppInfoList;
        if (tagetAppInfo != null) {
            tagetAppInfoList = new ArrayList<>();
            tagetAppInfoList.add(tagetAppInfo);
        } else {
            tagetAppInfoList = context.USER_KEY_INSTALL_LIST.getWithError();
        }
        String body = RequestParamUtils.getDiffUpgrade2(context, tagetAppInfoList, tagetAppInfo != null);
        String value = HttpRequestUtils.doPostRequest(HWJobContext.URL_STOREAPI3, body, proxy);
        return (JSONObject) JSONValue.parseWithException(value);
    }

    /**
     * 上传安装列表，
     * 
     * @param tagetAppInfo
     *            安装的App信息，没全时为上传用户安装列表。
     * @return
     * @throws Exception
     */
    public JSONObject diffUpgradeTargetDetail() throws Exception {
        Map detailInfo = context.SESSION_TARGET_DETAIL.getWithError();
        String downurl = (String) detailInfo.get("downurl");
        Map<String, String> tagetAppInfo = ApkParserUtils.downloadAndParseAPPInfo(downurl);
        return diffUpgrade(tagetAppInfo);
    }

    /**
     * 获取详情接口
     * 
     * @param uri
     *            搜索为：searchApp|关键词 </br> market协议为：package|包名</br>
     *            其余用接口中返回的uri或者detailId
     * @param trace
     *            上一接口获取。
     * @param reqPageNum
     *            请求页数
     * @return
     * @throws Exception
     */
    public JSONObject getTabDetail(String uri, String trace, int reqPageNum) throws Exception {
        String body = RequestParamUtils.getDetailParams(context, uri, trace, reqPageNum);
        String value = HttpRequestUtils.doPostRequest(HWJobContext.URL_STOREAPI2, body);
        return (JSONObject) JSONValue.parseWithException(value);
    }

    /**
     * 下载开始接口上报，必须Context SESSION_TARGET_DETAIL中有值
     * 
     * @return
     * @throws Exception
     */
    public JSONObject startDownloadRep() throws Exception {
        Map detailInfo = context.SESSION_TARGET_DETAIL.getWithError();
        context.SESSION_DOWNLOAD_START_TIME.set(System.currentTimeMillis());
        String body = RequestParamUtils.getStartDownloadRep(context, detailInfo);
        String value = HttpRequestUtils.doPostRequest(HWJobContext.URL_ENCRYPTAPI2, body, proxy);
        return (JSONObject) JSONValue.parseWithException(value);
    }

    /**
     * 下载接口，必须Context SESSION_TARGET_DETAIL中有值
     * 
     * @return
     * @throws IOException
     */
    public String download(boolean isSave) throws IOException {
        Map detailInfo = context.SESSION_TARGET_DETAIL.getWithError();
        String downurl = (String) detailInfo.get("downurl");
        String fileName = CommUtils.findFileName(downurl);
        HttpRequestUtils.downloadFile(downurl, isSave ? fileName : null, "0-100", proxy);
        return fileName;
    }

    /**
     * 下载开始结果上报，必须Context SESSION_TARGET_DETAIL中有值 </br> 并且在下载上报接口之后调用。
     * 
     * @return
     * @throws Exception
     */
    public JSONObject downloadResultRep() throws Exception {
        Map detailInfo = context.SESSION_TARGET_DETAIL.getWithError();
        long startTime = context.SESSION_DOWNLOAD_START_TIME.getWithError();
        long endTime = System.currentTimeMillis();
        String body = RequestParamUtils.getDownloadResultRep(context, detailInfo, startTime, endTime);
        String value = HttpRequestUtils.doPostRequest(HWJobContext.URL_ENCRYPTAPI2, body, proxy);
        return (JSONObject) JSONValue.parseWithException(value);
    }

}
