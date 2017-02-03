package com.awesome.aso.job.hw;

import java.util.List;
import java.util.Map;

import com.awesome.aso.job.JobContext;
import com.awesome.aso.job.util.DeviceUtils;

@SuppressWarnings("rawtypes")
public class HWJobContext extends JobContext {
    public static final String HW_MARKET_API_URI = "http://hispaceclt.hicloud.com/hwmarket/api/";
    public static final String URL_STOREAPI3 = HW_MARKET_API_URI + "storeApi3";
    public static final String URL_ENCRYPTAPI2 = HW_MARKET_API_URI + "encryptApi2";
    public static final String URL_STOREAPI2 = HW_MARKET_API_URI + "storeApi2";

    private static final long serialVersionUID = 1L;

    public HWJobContext(String uniqueId) {
        super(uniqueId);
    }

    // 设备相关参数
    public final ContextValue<Map<String, String>> USER_KEY_DEVICE = new ContextValue<Map<String, String>>(DeviceUtils.defalutHWDevInfo());
    public final ContextValue<List<Map<String, String>>> USER_KEY_INSTALL_LIST = new ContextValue<List<Map<String, String>>>();

    // 执行过程中产生的session数据
    public final ContextValue<String> SESSION_CLIENT_SIGN = new ContextValue<String>();
    public final ContextValue<String> SESSION_CLIENT_HCRID = new ContextValue<String>();
    public final ContextValue<String> SESSION_SECRET_KEY = new ContextValue<String>();
    public final ContextValue<String> SESSION_ENCRYPT_KEY = new ContextValue<String>();
    public final ContextValue<String> SESSION_SIGN_SECRET_KEY = new ContextValue<String>();
    public final ContextValue<String> SESSION_SIGN_ENCRYPT_KEY = new ContextValue<String>();
    // Map<String: name,String : tabid>
    public final ContextValue<Map<String, String>> SESSION_TAB_INFO = new ContextValue<Map<String, String>>();
    // 分类列表中目标分类
    public final ContextValue<Map> SESSION_CATEGORY_TARGET_DETAIL = new ContextValue<Map>();
    // 目标APP详情
    public final ContextValue<Map> SESSION_TARGET_DETAIL = new ContextValue<Map>();
    //
    public final ContextValue<Long> SESSION_DOWNLOAD_START_TIME = new ContextValue<Long>();

}
