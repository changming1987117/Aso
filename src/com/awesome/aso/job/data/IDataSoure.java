package com.awesome.aso.job.data;

import java.util.List;
import java.util.Map;

public interface IDataSoure {
    public List<Map<String, String>> queryWholeDeviceFilterByPkgLimit(String pkgName, int limit);

    public List<Map<String, String>> queryInstallAppInfo(String imei);

    public boolean insertRecord(String imei, String targetPkg, int isDownload);
}
