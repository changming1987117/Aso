package com.awesome.aso.job.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DeviceUtils {
    public static final String[] DENSITYS = { "240", "320", "480", "640" };
    public static final String[] NET = { "0", "1" }; // 0：wifi， 1:移动网络
    public static final long[] MEMORY = { 1024 * 1000, 1024 * 2000, 1024 * 3000, 1024 * 4000 };

    public static Map<String, String> convCons(ResultSet result) throws SQLException {
        Map<String, String> hwDevice = new HashMap<String, String>();
        hwDevice.put("imei", result.getString(result.findColumn("imei")));
        hwDevice.put("imsi", result.getString(result.findColumn("imsi")) == null ? "00000000000000" : result.getString(result.findColumn("imsi")));
        hwDevice.put("model", result.getString(result.findColumn("model")) == null ? "" : result.getString(result.findColumn("model")));
        hwDevice.put("product", result.getString(result.findColumn("device")) == null ? "" : result.getString(result.findColumn("device")));
        hwDevice.put("mcc", DeviceUtils.parserMCC(hwDevice.get("imsi")));
        hwDevice.put("mnc", DeviceUtils.parserMNC(hwDevice.get("imsi")));
        hwDevice.put("gms_support", "0");
        hwDevice.put("locale", "cn");
        hwDevice.put("net", NET[CommUtils.randomInterval(0, NET.length - 1)]);
        hwDevice.put("game_provider", "0");
        hwDevice.put("is_sub_user", "0");
        hwDevice.put("max_mem", String.valueOf(MEMORY[CommUtils.randomInterval(0, MEMORY.length - 1)]));
        hwDevice.put("density", DENSITYS[CommUtils.randomInterval(0, DENSITYS.length - 1)]);
        hwDevice.put("os_version", "5.1.1");
        // hwDevice.put("os_version", deviceInfo.getVersionRelease()
        // == null ? "" : deviceInfo.getVersionRelease());
        hwDevice.put("resolution", result.getString(result.findColumn("res")) == null ? "" : result.getString(result.findColumn("res")).replace("x", "_"));
        // System.out.println(devicesInfo.toString());
        return hwDevice;
    }

    public static Map<String, String> converInstalledAppToHW(ResultSet result) throws SQLException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("md5Hash", "null");
        map.put("partHash", "null");
        map.put("oldVersion", result.getString(result.findColumn("version_name")) == null ? "null" : result.getString(result.findColumn("version_name")));
        map.put("package", result.getString(result.findColumn("package")) == null ? "null" : result.getString(result.findColumn("package")));
        map.put("s",
                result.getString(result.findColumn("package")) == null ? "null" : com.awesome.aso.job.hw.core.Utils.crc32Encoding(result.getString(result
                        .findColumn("package"))));
        map.put("isPre", result.getString(result.findColumn("type")));
        map.put("targetSdkVersion", result.getString(result.findColumn("target_sdk_version")));
        map.put("versionCode", result.getString(result.findColumn("version_code")));
        return map;
    }

    public static Map<String, String> defalutHWDevInfo() {
        Map<String, String> hwDevice = new HashMap<String, String>();
        hwDevice.put("imei", "133524166941004");
        hwDevice.put("imsi", "00000000000000");
        hwDevice.put("model", "Mate7");
        hwDevice.put("product", "Mate7");
        hwDevice.put("mcc", "000");
        hwDevice.put("mnc", "00");
        hwDevice.put("gms_support", "0");
        hwDevice.put("locale", "cn");
        hwDevice.put("net", NET[CommUtils.randomInterval(0, NET.length - 1)]);
        hwDevice.put("game_provider", "0");
        hwDevice.put("is_sub_user", "0");
        hwDevice.put("max_mem", String.valueOf(MEMORY[CommUtils.randomInterval(0, MEMORY.length - 1)]));
        hwDevice.put("density", DENSITYS[CommUtils.randomInterval(0, DENSITYS.length - 1)]);
        hwDevice.put("os_version", "5.1.1");
        hwDevice.put("resolution", "1920_1080");
        return hwDevice;
    }

    public static String parserMCC(String imsi) {
        if (imsi != null && imsi.length() > 5) {
            return imsi.substring(0, 3);
        }
        return "000";
    }

    public static String parserMNC(String imsi) {
        if (imsi != null && imsi.length() > 5) {
            return imsi.substring(3, 5);
        }
        return "00";
    }
}
