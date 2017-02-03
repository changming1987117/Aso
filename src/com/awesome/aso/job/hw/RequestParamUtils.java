package com.awesome.aso.job.hw;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONValue;

import com.awesome.aso.job.hw.core.AESUtil;
import com.awesome.aso.job.hw.core.BaseStringUtils;
import com.awesome.aso.job.hw.core.EncryptKeyUtils;
import com.awesome.aso.job.hw.core.SecretUtil;
import com.awesome.aso.job.hw.core.StringUtils;

public class RequestParamUtils {

    static String CLIENT_PACKAGE = "com.huawei.appmarket";
    static String VER = "1.1";
    // static String VERSION = "7.0.1";
    // static String VERSIONCODE = "70001301";
    static String VERSION = "7.2.1";
    static String VERSIONCODE = "70201303";

    static String CNO = "4010001";
    static String CODE = "0200";

    static String ZONE = "1";

    // 登录请求参数
    public static String getStatup(HWJobContext context) {
        String method = "client.front2";
        Map<String, String> info = context.USER_KEY_DEVICE.getWithError();
        String buildNumber = info.get("model");
        String density = info.get("density");
        String firmwareVersion = info.get("os_version");
        String phoneType = info.get("product");
        String mcc = info.get("mcc");
        String mnc = info.get("mnc");
        String locale = info.get("locale");
        String gmsSupport = info.get("gms_support");
        String gameProvider = info.get("game_provider");
        String resolution = info.get("resolution");
        String net = info.get("net");
        String isSubUser = info.get("is_sub_user");

        //
        String ts = String.valueOf(System.currentTimeMillis());
        String encryptKey = getEncryptKey(context);
        String encryptSignKey = getSignEncryptKey(context);
        String iv = genIVKey();
        String salt = genSalt();
        String subId = genSubid(iv, context);
        String userId = genUid(iv, context);

        Map<String, String> params = new HashMap<String, String>();
        String hcrid = context.SESSION_CLIENT_HCRID.get();
        String isFirstLaunch = "1";
        if (hcrid != null) {
            isFirstLaunch = "0";
            params.put("hcrId", hcrid);
        }
        params.put("buildNumber", buildNumber);
        params.put("clientPackage", CLIENT_PACKAGE);
        params.put("cno", CNO);
        params.put("code", CODE);
        params.put("density", density);
        params.put("firmwareVersion", firmwareVersion);
        params.put("gameProvider", gameProvider);
        params.put("gmsSupport", gmsSupport);
        params.put("isFirstLaunch", isFirstLaunch);
        params.put("isSubUser", isSubUser);
        params.put("locale", locale);
        params.put("mcc", mcc);
        params.put("method", method);
        params.put("mnc", mnc);
        params.put("net", net);
        params.put("packageName", CLIENT_PACKAGE);
        params.put("phoneType", phoneType);
        params.put("resolution", resolution);
        params.put("screen", resolution);
        params.put("serviceType", "0");
        params.put("sysBits", "1");
        params.put("theme", "true");
        params.put("ver", VER);
        params.put("version", VERSION);
        params.put("versionCode", VERSIONCODE);

        //
        params.put("zone", ZONE);
        params.put("ts", ts);
        params.put("encryptKey", encryptKey);
        params.put("encryptSignKey", encryptSignKey);
        params.put("iv", iv);
        params.put("salt", salt);
        params.put("subId", subId);
        params.put("userId", userId);

        //
        String body = genBody(params);
        String nsp_key = genNspKey("storeApi3", ts, body, userId, context);
        params.put("nsp_key", nsp_key);
        String data = body + "&nsp_key=" + nsp_key;
        return data;
    }

    // 安装列表上传
    public static String getDiffUpgrade2(HWJobContext context, List<Map<String, String>> appList, boolean isInstallCheck) {
        String method = "client.diffUpgrade2";

        Map<String, String> info = context.USER_KEY_DEVICE.getWithError();

        String net = info.get("net");
        String sign = context.SESSION_CLIENT_SIGN.getWithError();
        String hcrId = context.SESSION_CLIENT_HCRID.getWithError();

        String ts = String.valueOf(System.currentTimeMillis());
        String iv = genIVKey();
        String salt = genSalt();
        String userId = genUid(iv, context);

        String CMP = "1";
        String installCheck = isInstallCheck ? "1" : "0";
        String isFullUpgrade = "0";
        String isWlanIdle = "1";
        String serviceType = "0";
        String times = "1";
        String maxMem = info.get("max_mem");

        String body = upgradeParamsData(appList, iv, context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("CMP", CMP);
        params.put("body", body);
        params.put("clientPackage", CLIENT_PACKAGE);
        params.put("cno", CNO);
        params.put("code", CODE);
        params.put("hcrId", hcrId);
        params.put("installCheck", installCheck);
        params.put("isFullUpgrade", isFullUpgrade);
        params.put("isWlanIdle", isWlanIdle);
        params.put("iv", iv);
        params.put("maxMem", maxMem);
        params.put("method", method);
        params.put("net", net);
        params.put("salt", salt);
        params.put("serviceType", serviceType);
        params.put("sign", sign);
        params.put("times", times);
        params.put("ts", ts);
        params.put("userId", userId);
        params.put("ver", VER);
        // params.put("nsp_key", nsp_key);
        String reqBody = genBody(params);
        String nsp_key = genNspKey("storeApi3", ts, reqBody, userId, context);
        params.put("nsp_key", nsp_key);
        String data = reqBody + "&nsp_key=" + nsp_key;
        return data;
    }

    //
    public static String getOperReport(HWJobContext context) {
        String method = "client.operReport";
        Map<String, String> info = context.USER_KEY_DEVICE.getWithError();
        String net = info.get("net");
        String sign = context.SESSION_CLIENT_SIGN.getWithError();
        String hcrId = context.SESSION_CLIENT_HCRID.getWithError();
        String ts = String.valueOf(System.currentTimeMillis());
        String iv = genIVKey();
        String salt = genSalt();
        String userId = genUid(iv, context);

        String oper = "1";
        String serviceType = "0";

        //
        Map<String, String> tabInfo = context.SESSION_TAB_INFO.getWithError();
        String uri = tabInfo.get("推荐");

        Map<String, String> params = new HashMap<String, String>();
        params.put("clientPackage", CLIENT_PACKAGE);
        params.put("cno", CNO);
        params.put("code", CODE);
        params.put("hcrId", hcrId);
        params.put("iv", iv);
        params.put("method", method);
        params.put("net", net);
        params.put("oper", oper);
        params.put("salt", salt);
        params.put("serviceType", serviceType);
        params.put("sign", sign);
        params.put("ts", ts);
        params.put("uri", uri);
        params.put("userId", userId);
        params.put("ver", VER);
        // params.put("nsp_key", nsp_key);
        String body = genBody(params);
        String nsp_key = genNspKey("storeApi2", ts, body, userId, context);
        String data = body + "&nsp_key=" + nsp_key;
        return data;
    }

    public static String getDeviceSummary(HWJobContext context) {
        String method = "client.getDeviceSummary";
        Map<String, String> info = context.USER_KEY_DEVICE.getWithError();
        String sign = context.SESSION_CLIENT_SIGN.getWithError();
        String hcrId = context.SESSION_CLIENT_HCRID.getWithError();
        String ts = String.valueOf(System.currentTimeMillis());

        String net = info.get("net");
        String iv = genIVKey();
        String salt = genSalt();
        String userId = genUid(iv, context);

        String serviceType = "0";

        Map<String, String> params = new HashMap<String, String>();
        params.put("clientPackage", CLIENT_PACKAGE);
        params.put("cno", CNO);
        params.put("code", CODE);
        params.put("hcrId", hcrId);
        params.put("iv", iv);
        params.put("method", method);
        params.put("net", net);
        params.put("salt", salt);
        params.put("serviceType", serviceType);
        params.put("sign", sign);
        params.put("ts", ts);
        params.put("userId", userId);
        params.put("ver", VER);
        String body = genBody(params);
        String nsp_key = genNspKey("storeApi2", ts, body, userId, context);
        String data = body + "&nsp_key=" + nsp_key;
        return data;
    }

    public static String loginImage(HWJobContext context) {
        String method = "client.loginImage";
        Map<String, String> info = context.USER_KEY_DEVICE.getWithError();
        String sign = context.SESSION_CLIENT_SIGN.getWithError();
        String hcrId = context.SESSION_CLIENT_HCRID.getWithError();
        String ts = String.valueOf(System.currentTimeMillis());

        String net = info.get("net");
        String iv = genIVKey();
        String salt = genSalt();
        String userId = genUid(iv, context);

        String serviceType = "0";

        Map<String, String> params = new HashMap<String, String>();
        params.put("clientPackage", CLIENT_PACKAGE);
        params.put("cno", CNO);
        params.put("code", CODE);
        params.put("hcrId", hcrId);
        params.put("iv", iv);
        params.put("method", method);
        params.put("net", net);
        params.put("salt", salt);
        params.put("serviceType", serviceType);
        params.put("sign", sign);
        params.put("ts", ts);
        params.put("userId", userId);
        params.put("ver", VER);
        String body = genBody(params);
        String nsp_key = genNspKey("storeApi2", ts, body, userId, context);
        String data = body + "&nsp_key=" + nsp_key;
        return data;
    }

    // 进入页， 热门搜索 。
    public static String getHotSearchList(HWJobContext context, String firstWord, String entyTabId) {
        String method = "client.getHotSearchList";

        Map<String, String> info = context.USER_KEY_DEVICE.getWithError();
        String sign = context.SESSION_CLIENT_SIGN.getWithError();
        String hcrId = context.SESSION_CLIENT_HCRID.getWithError();
        String ts = String.valueOf(System.currentTimeMillis());

        String trace = entyTabId;

        String net = info.get("net");
        String iv = genIVKey();
        String salt = genSalt();
        String userId = genUid(iv, context);

        String serviceType = "0";
        Map<String, String> params = new HashMap<String, String>();
        params.put("clientPackage", CLIENT_PACKAGE);
        params.put("cno", CNO);
        params.put("code", CODE);
        params.put("firstWord", firstWord);
        params.put("hcrId", hcrId);
        params.put("iv", iv);
        params.put("method", method);
        params.put("net", net);
        params.put("salt", salt);
        params.put("serviceType", serviceType);
        params.put("sign", sign);
        params.put("trace", trace);
        params.put("ts", ts);
        params.put("userId", userId);
        params.put("ver", VER);
        // params.put("nsp_key", nsp_key);
        String body = genBody(params);
        String nsp_key = genNspKey("storeApi2", ts, body, userId, context);
        String data = body + "&nsp_key=" + nsp_key;
        return data;
    }

    // 搜索词匹配
    public static String completeSearchWord(HWJobContext context, String keyword) {
        String method = "client.completeSearchWord";

        Map<String, String> info = context.USER_KEY_DEVICE.getWithError();
        String sign = context.SESSION_CLIENT_SIGN.getWithError();
        String hcrId = context.SESSION_CLIENT_HCRID.getWithError();

        String ts = String.valueOf(System.currentTimeMillis());

        String net = info.get("net");
        String iv = genIVKey();
        String salt = genSalt();
        String userId = genUid(iv, context);

        String serviceType = "0";
        Map<String, String> params = new HashMap<String, String>();
        params.put("clientPackage", CLIENT_PACKAGE);
        params.put("cno", CNO);
        params.put("code", CODE);
        params.put("hcrId", hcrId);
        params.put("isCommendApp", "1");
        params.put("iv", iv);
        params.put("keyword", keyword);
        params.put("method", method);
        params.put("net", net);
        params.put("salt", salt);
        params.put("serviceType", serviceType);
        params.put("sign", sign);
        params.put("ts", ts);
        params.put("userId", userId);
        params.put("ver", VER);
        // params.put("nsp_key", nsp_key);
        String body = genBody(params);
        String nsp_key = genNspKey("storeApi2", ts, body, userId, context);
        String data = body + "&nsp_key=" + nsp_key;
        return data;
    }

    public static String getSearchParams(HWJobContext context, String keyword, int pageNumber, String entyTabId) {
        String method = "client.newSearchApp1";

        Map<String, String> info = context.USER_KEY_DEVICE.getWithError();
        String sign = context.SESSION_CLIENT_SIGN.getWithError();
        String hcrId = context.SESSION_CLIENT_HCRID.getWithError();

        String net = info.get("net");

        String trace = entyTabId;

        String ts = String.valueOf(System.currentTimeMillis());
        String iv = genIVKey();
        String salt = genSalt();
        String userId = genUid(iv, context);

        Map<String, String> params = new HashMap<String, String>();
        params.put("clientPackage", CLIENT_PACKAGE);
        params.put("cno", CNO);
        params.put("code", CODE);
        params.put("isClassified", "1");
        params.put("isHotWord", "0");
        params.put("isShake", "0");
        params.put("maxResults", "25");
        params.put("method", method);
        params.put("name", keyword);
        params.put("net", net);
        params.put("reqPageNum", String.valueOf(pageNumber));
        params.put("serviceType", "0");
        params.put("shakeReqPageNum", "0");
        params.put("ver", VER);
        params.put("sign", sign);
        params.put("hcrId", hcrId);
        params.put("trace", trace);
        params.put("ts", ts);
        params.put("iv", iv);
        params.put("salt", salt);
        params.put("userId", userId);

        String body = genBody(params);
        String nsp_key = genNspKey("storeApi2", ts, body, userId, context);
        String data = body + "&nsp_key=" + nsp_key;
        return data;
    }

    public static String getDetailParams(HWJobContext context, String uri, String trace, int reqPageNum) {
        String method = "client.getTabDetail";
        Map<String, String> info = context.USER_KEY_DEVICE.getWithError();
        String sign = context.SESSION_CLIENT_SIGN.getWithError();
        String hcrId = context.SESSION_CLIENT_HCRID.getWithError();

        String net = info.get("net");
        String ts = String.valueOf(System.currentTimeMillis());
        String iv = genIVKey();
        String salt = genSalt();
        String userId = genUid(iv, context);

        Map<String, String> params = new HashMap<String, String>();
        params.put("clientPackage", "com.huawei.appmarket");
        params.put("cno", CNO);
        params.put("code", CODE);
        params.put("contentPkg", "");
        params.put("hcrId", hcrId);
        params.put("isShake", "0");
        params.put("iv", iv);
        params.put("maxResults", "25");
        params.put("method", method);
        params.put("net", net);
        params.put("reqPageNum", Integer.toString(reqPageNum));
        params.put("salt", salt);
        params.put("serviceType", "0");
        params.put("shakeReqPageNum", "0");
        params.put("sign", sign);
        if (trace != null && !trace.equals("")) {
            params.put("trace", trace);
        }
        params.put("ts", ts);
        params.put("uri", uri);
        params.put("userId", userId);
        params.put("ver", VER);
        // 搜索App
        if (uri.startsWith("searchApp")) {
            params.put("isHotWord", "0");
        }
        String body = genBody(params);
        String nsp_key = genNspKey("storeApi2", ts, body, userId, context);
        String data = body + "&nsp_key=" + nsp_key;
        return data;
    }

    public static String getStartDownloadRep(HWJobContext context, Map infoDetail) {
        String method = "client.startDownloadRep";
        Map<String, String> info = context.USER_KEY_DEVICE.getWithError();
        String sign = context.SESSION_CLIENT_SIGN.getWithError();
        String hcrId = context.SESSION_CLIENT_HCRID.getWithError();

        String net = info.get("net");
        String ts = String.valueOf(System.currentTimeMillis());
        String iv = genIVKey();
        String salt = genSalt();
        String userId = genUid(iv, context);

        String dlType = "0";

        String appid = (String) infoDetail.get("appid");
        String detailId = (String) infoDetail.get("detailId");
        String pkg = (String) infoDetail.get("package");
        String trace = (String) infoDetail.get("trace");
        String downurl = (String) infoDetail.get("downurl");

        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", appid);
        params.put("clientPackage", CLIENT_PACKAGE);
        params.put("cno", CNO);
        params.put("code", CODE);
        params.put("detailId", detailId);
        params.put("dlType", dlType);
        params.put("hcrId", hcrId);
        params.put("iv", iv);
        params.put("method", method);
        params.put("net", net);
        params.put("pkgName", pkg);
        params.put("salt", salt);
        params.put("serviceType", "0");
        params.put("sign", sign);
        params.put("trace", trace);
        params.put("ts", ts);
        params.put("url", downurl);
        params.put("userId", userId);
        params.put("ver", VER);
        String body = genBody(params);
        String nsp_key = genNspKey("encryptApi2", ts, body, userId, context);
        String data = body + "&nsp_key=" + nsp_key;
        params.put("nsp_key", nsp_key);
        return data;
    }

    public static String getDownloadResultRep(HWJobContext context, Map detailInfo, long startTime, long endTime) {
        String method = "client.downloadResultRep";
        Map<String, String> info = context.USER_KEY_DEVICE.getWithError();
        String sign = context.SESSION_CLIENT_SIGN.getWithError();
        String hcrId = context.SESSION_CLIENT_HCRID.getWithError();
        String ts = String.valueOf(System.currentTimeMillis());

        String net = info.get("net");
        String iv = genIVKey();
        String salt = genSalt();
        String userId = genUid(iv, context);

        String dlType = "0";
        String downResult = "0";
        String serviceType = "0";
        // 开始时间|结束时间|文件大小|下载结果|网络类型
        // String expand = "1458289132329|1458289134856|1940186|0|WIFI/|";
        // long endTime = System.currentTimeMillis();
        // long startTime = endTime - 10;

        String appid = (String) detailInfo.get("appid");
        String detailId = (String) detailInfo.get("detailId");
        String pkg = (String) detailInfo.get("package");
        String trace = (String) detailInfo.get("trace");
        String downurl = (String) detailInfo.get("downurl");
        Long size = (Long) detailInfo.get("size");

        String expand = downRelustExpand(String.valueOf(startTime), String.valueOf(endTime), String.valueOf(size), downResult, "WIFI/", downurl);

        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", appid);
        params.put("clientPackage", CLIENT_PACKAGE);
        params.put("cno", CNO);
        params.put("code", CODE);
        params.put("detailId", detailId);
        params.put("dlType", dlType);
        params.put("downResult", downResult);
        params.put("expand", expand);
        params.put("hcrId", hcrId);
        params.put("iv", iv);
        params.put("method", method);
        params.put("net", net);
        params.put("pkgName", pkg);
        params.put("salt", salt);
        params.put("serviceType", serviceType);
        params.put("sign", sign);
        params.put("trace", trace);
        params.put("ts", ts);
        params.put("userId", userId);
        params.put("ver", VER);
        String body = genBody(params);
        String nsp_key = genNspKey("encryptApi2", ts, body, userId, context);
        String data = body + "&nsp_key=" + nsp_key;
        params.put("nsp_key", nsp_key);
        return data;
    }

    public static String getInstallResultRep(HWJobContext context, String pkgName) {
        String method = "client.installResultRep";
        Map<String, String> info = context.USER_KEY_DEVICE.getWithError();
        String sign = context.SESSION_CLIENT_SIGN.getWithError();
        String hcrId = context.SESSION_CLIENT_HCRID.getWithError();
        String ts = String.valueOf(System.currentTimeMillis());
        String net = info.get("net");
        String iv = genIVKey();
        String salt = genSalt();
        String userId = genUid(iv, context);

        String fileSize = "0";
        String installResult = "0";
        String installType = "2";
        String serviceType = "0";

        Map<String, String> params = new HashMap<String, String>();
        params.put("clientPackage", CLIENT_PACKAGE);
        params.put("cno", CNO);
        params.put("code", CODE);
        params.put("fileSize", fileSize);
        params.put("hcrId", hcrId);
        params.put("installResult", installResult);
        params.put("installType", installType);
        params.put("iv", iv);
        params.put("method", method);
        params.put("net", net);
        params.put("pkgName", pkgName);
        params.put("salt", salt);
        params.put("serviceType", serviceType);
        params.put("sign", sign);
        params.put("ts", ts);
        params.put("userId", userId);
        params.put("ver", VER);
        // params.put("nsp_key", nsp_key);
        String body = genBody(params);
        String nsp_key = genNspKey("encryptApi2", ts, body, userId, context);
        String data = body + "&nsp_key=" + nsp_key;
        params.put("nsp_key", nsp_key);
        return data;
    }

    // 7.2.1 新接口
    public static String getNewHotSearchList(HWJobContext context, String entyTabId) {
        String method = "client.getNewHotSearchList";

        Map<String, String> info = context.USER_KEY_DEVICE.getWithError();
        String sign = context.SESSION_CLIENT_SIGN.getWithError();
        String hcrId = context.SESSION_CLIENT_HCRID.getWithError();
        String ts = String.valueOf(System.currentTimeMillis());

        String trace = entyTabId;

        String net = info.get("net");
        String iv = genIVKey();
        String salt = genSalt();
        String userId = genUid(iv, context);

        String serviceType = "0";
        Map<String, String> params = new HashMap<String, String>();
        params.put("clientPackage", CLIENT_PACKAGE);
        params.put("cno", CNO);
        params.put("code", CODE);
        params.put("hcrId", hcrId);
        params.put("iv", iv);
        params.put("method", method);
        params.put("net", net);
        params.put("salt", salt);
        params.put("serviceType", serviceType);
        params.put("sign", sign);
        params.put("trace", trace);
        params.put("ts", ts);
        params.put("userId", userId);
        params.put("ver", VER);
        // params.put("nsp_key", nsp_key);
        String body = genBody(params);
        String nsp_key = genNspKey("storeApi2", ts, body, userId, context);
        String data = body + "&nsp_key=" + nsp_key;
        return data;
    }

    private static String downRelustExpand(String url, String startTime, String endTime, String size, String reslut, String net) {
        StringBuilder expandSB = new StringBuilder();
        expandSB.append(startTime).append("|");
        expandSB.append(endTime).append("|");
        expandSB.append(size).append("|");
        expandSB.append(reslut).append("|");
        expandSB.append(net).append("|");

        String subsource = findSubsource(url, "subsource");
        if (null != subsource) {
            expandSB.append(subsource).append("|");
        }
        return expandSB.toString();
    }

    private static String findSubsource(String url, String source) {
        if (url == null) {
            return null;
        }
        Matcher matcher = Pattern.compile("\\w*" + source + "\\s*=(\\w*)").matcher(url);
        return matcher.find() ? matcher.group(1) : null;
    }

    public static String upgradeParamsData(List<Map<String, String>> params, String iv, HWJobContext context) {
        String paramsJson = JSONValue.toJSONString(params);
        StringBuilder sb = new StringBuilder("json={\"params\":");
        sb.append(paramsJson).append("}");
        //
        String json = sb.toString();
        try {
            byte[] secretKeyArr = getSecretKey(context).getBytes("utf-8");
            byte[] ivByteArr = BaseStringUtils.decoding(iv);
            return AESUtil.aesCipherBase64(json, secretKeyArr, ivByteArr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String genIVKey() {
        byte[] bArr = new byte[16];
        new SecureRandom().nextBytes(bArr);
        return BaseStringUtils.encoding(bArr);
    }

    public static String genSalt() {
        return String.valueOf(new SecureRandom().nextLong());
    }

    public static String genSubid(String iv, HWJobContext context) {
        String subid = null;
        Map<String, String> info = context.USER_KEY_DEVICE.getWithError();
        try {
            String imsi = info.get("imsi");
            byte[] secretKeyArr = getSecretKey(context).getBytes("utf-8");
            byte[] ivByteArr = BaseStringUtils.decoding(iv);
            subid = AESUtil.aesCipherBase64(imsi, secretKeyArr, ivByteArr);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subid;
    }

    public static String genUid(String iv, HWJobContext context) {
        Map<String, String> info = context.USER_KEY_DEVICE.getWithError();
        String imeiAndUid = info.get("imei");
        String secretKey = getSecretKey(context);
        byte[] ivByteArr = BaseStringUtils.decoding(iv);
        String uid = null;
        try {
            uid = AESUtil.encrypASCIIKey(imeiAndUid, secretKey, ivByteArr);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return uid;
    }

    public static String genBody(Map<String, String> params) {
        String[] paramKeysArr = new String[params.size()];
        params.keySet().toArray(paramKeysArr);
        Arrays.sort(paramKeysArr);

        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < paramKeysArr.length; j++) {
            String value = params.get(paramKeysArr[j]);
            if (value != null) {
                sb.append(paramKeysArr[j]);
                sb.append("=");
                sb.append(StringUtils.URLEncoder(value));
                sb.append("&");
            }
        }
        int len = sb.length();
        if (len > 0 && sb.charAt(len - 1) == '&') {
            sb.deleteCharAt(len - 1);
        }
        return sb.toString();
    }

    public static String genNspKey(String storeApi, String ts, String body, String userid, HWJobContext context) {
        String appKey = getAppKey(storeApi, ts, userid, context);
        if (appKey == null) {
            return null;
        }

        if ("gbClientApi".equals(storeApi)) {
            appKey = SecretUtil.hmacSha256Base64(body, appKey);
        } else {
            appKey = SecretUtil.hmacSha1Base64(body, appKey);
        }
        return appKey != null ? StringUtils.URLEncoder(appKey.trim()) : appKey;
    }

    public static String getAppKey(String storeApi, String ts, String userid, HWJobContext context) {
        return storeApi.equals("storeApi3") ? AESUtil.sha256Digest(userid + ts + "&") : getSignSecretKey(context);
    }

    // ////////////////////////////////////////////////////////////////////
    public static String getSignSecretKey(HWJobContext context) {
        String signSecretKey = context.SESSION_SIGN_SECRET_KEY.get();
        if (signSecretKey == null /* || signSecretKey.length() <= 32 */) {
            while (signSecretKey == null || signSecretKey.length() < 16) {
                signSecretKey = String.valueOf(new SecureRandom().nextLong());
            }
            context.SESSION_SIGN_SECRET_KEY.set(signSecretKey);
            context.SESSION_SIGN_ENCRYPT_KEY.set(null);
        }
        return signSecretKey;
    }

    public static String getSecretKey(HWJobContext context) {
        String secretKey = context.SESSION_SECRET_KEY.get();
        if (secretKey == null /* || secretKey.length() <= 32 */) {
            while (secretKey == null || secretKey.length() < 16) {
                secretKey = String.valueOf(new SecureRandom().nextLong());
            }
            context.SESSION_SECRET_KEY.set(secretKey);
            context.SESSION_ENCRYPT_KEY.set(null);
        }
        return secretKey;
    }

    public static String getSignEncryptKey(HWJobContext context) {
        String signEncryptKey = context.SESSION_SIGN_ENCRYPT_KEY.get();
        if (signEncryptKey == null) {
            String signSecretKey = getSignSecretKey(context);
            String encodedKey = "30819F300D06092A864886F70D010101050003818D00308189028181008F757553E3217531CF77B6FA1F5A9548256FED127CB14CA89402057350F4C4F2FC17E4B7FE3420BDBB598BC7F3D01F5F85B81C6F618913A372C3F3E656D31A5B1517B695E176124EEBB9BA18BF29611CCC70AB91CFB64352824442CBDBBF359FE8CAB635F0566A7E1819664968358E38A1AA1247231BFEB6807E3154265081FD0203010001";
            try {
                signEncryptKey = EncryptKeyUtils.publicKeyEncryptData(signSecretKey.getBytes("UTF-8"), encodedKey);
                context.SESSION_SIGN_ENCRYPT_KEY.set(signEncryptKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return signEncryptKey;
    }

    public static String getEncryptKey(HWJobContext context) {
        String encryptKey = context.SESSION_ENCRYPT_KEY.get();
        if (encryptKey == null) {
            String secretKey = getSecretKey(context);
            String encodedKey = "30819F300D06092A864886F70D010101050003818D00308189028181008F757553E3217531CF77B6FA1F5A9548256FED127CB14CA89402057350F4C4F2FC17E4B7FE3420BDBB598BC7F3D01F5F85B81C6F618913A372C3F3E656D31A5B1517B695E176124EEBB9BA18BF29611CCC70AB91CFB64352824442CBDBBF359FE8CAB635F0566A7E1819664968358E38A1AA1247231BFEB6807E3154265081FD0203010001";
            try {
                encryptKey = EncryptKeyUtils.publicKeyEncryptData(secretKey.getBytes("UTF-8"), encodedKey);
                context.SESSION_ENCRYPT_KEY.set(encryptKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return encryptKey;
    }
}
