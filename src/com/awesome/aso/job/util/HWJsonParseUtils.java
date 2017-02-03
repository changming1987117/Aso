package com.awesome.aso.job.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class HWJsonParseUtils {

    public static boolean hasNext(JSONObject result) {
        if (result == null || !result.containsKey("hasNextPage")) {
            return false;
        }
        long hasNextPage = (long) result.get("hasNextPage");
        return hasNextPage == 1;
    }

    public static JSONArray getAllLayoutDataDataListByLayoutName(JSONObject result, String layoutName) {
        JSONArray values = new JSONArray();
        JSONArray layoutData = (JSONArray) result.get("layoutData");
        for (int i = 0; i < layoutData.size(); i++) {
            JSONObject jsonObj = (JSONObject) layoutData.get(i);
            String layoutNameValue = (String) jsonObj.get("layoutName");
            if (layoutName.equals(layoutNameValue)) {
                values.addAll((JSONArray) jsonObj.get("dataList"));
            }
        }
        return values;
    }

    public static JSONArray getAllLayoutDataByLayoutName(JSONObject result, String layoutName) {
        JSONArray values = new JSONArray();
        JSONArray layoutData = (JSONArray) result.get("layoutData");
        for (int i = 0; i < layoutData.size(); i++) {
            JSONObject jsonObj = (JSONObject) layoutData.get(i);
            String layoutNameValue = (String) jsonObj.get("layoutName");
            if (layoutName.equals(layoutNameValue)) {
                values.add(jsonObj);
            }
        }
        return values;
    }

    public static JSONArray getDataListByLayoutName(JSONObject obj, String layoutNameValue) throws ParseException {
        JSONArray layoutData = (JSONArray) obj.get("layoutData");
        for (int i = 0; i < layoutData.size(); i++) {
            JSONObject jsonObj = (JSONObject) layoutData.get(i);
            String layoutName = (String) jsonObj.get("layoutName");
            if (layoutNameValue.equals(layoutName)) {
                return (JSONArray) jsonObj.get("dataList");
            }
        }
        return null;
    }

    public static Map<String, String> getTabInfo(JSONObject detailInfoReslut) {
        JSONArray tabInfo = (JSONArray) detailInfoReslut.get("tabInfo");
        if (tabInfo != null) {
            Map<String, String> tabs = new HashMap<String, String>();
            for (int i = 0; i < tabInfo.size(); i++) {
                JSONObject jsonObj = (JSONObject) tabInfo.get(i);
                String tabName = (String) jsonObj.get("tabName");
                String tabId = (String) jsonObj.get("tabId");
                tabs.put(tabName, tabId);
            }
            return tabs;
        }
        return null;
    }

    public static JSONObject findTargetApp(JSONArray resultList, String targetPkgName) {
        if (resultList == null) {
            return null;
        }
        for (int i = 0; i < resultList.size(); i++) {
            JSONObject tempInfo = (JSONObject) resultList.get(i);
            String packageName = (String) tempInfo.get("package");
            if (targetPkgName.equals(packageName)) {
                return tempInfo;
            }
        }
        return null;
    }

    /**
     * 转化华为推广链接为普通链接
     * 
     * @param detailInfo
     */
    public static void moverAppDetailAdInfo(JSONObject detailInfo) {
        detailInfo.put("detailId", moverDetailIdAdInfo((String) detailInfo.get("detailId")));
        detailInfo.put("downurl", moverDownloadUrlAdInfo((String) detailInfo.get("downurl")));
    }

    public static String moverDetailIdAdInfo(String uri) {
        if (uri.contains("HiAd")) {
            return uri.substring(0, uri.lastIndexOf("__") + 2).replace("HiAd", "autoList");
        }
        return uri;
    }

    public static String moverDownloadUrlAdInfo(String downloadUrl) {
        if (downloadUrl.contains("HiAd")) {
            String oldExtendStr = "";
            String newExendStr = "";
            Pattern p = Pattern.compile("extendStr=([^&]*)(&|$)");
            Matcher matcher = p.matcher(downloadUrl);
            if (matcher.find()) {
                oldExtendStr = matcher.group(1);
                newExendStr = oldExtendStr.substring(oldExtendStr.indexOf("%3B") + 3);
            }
            return downloadUrl.replace("source=HiAd", "source=autoList").replace(oldExtendStr, newExendStr);
        }
        return downloadUrl;
    }

}
