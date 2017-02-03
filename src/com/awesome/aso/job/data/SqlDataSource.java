package com.awesome.aso.job.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.awesome.aso.job.util.DeviceUtils;

public class SqlDataSource implements IDataSoure {
    public static String DRIVER_NAME = "com.mysql.jdbc.Driver";

    static {
        try {
            Class.forName(DRIVER_NAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection conn;
    private Statement statement = null;
    private ResultSet result = null;

    public SqlDataSource(String host, String dbName, String user, String password) {
        super();
        String uri = getConnUri(host, dbName, user, password);
        try {
            this.conn = DriverManager.getConnection(uri);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("datasource init error");
        }
    }

    private String getConnUri(String host, String dbName, String user, String password) {
        return "jdbc:mysql://" + host + "/" + dbName + "?user=" + user + "&password=" + password
                + "&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false";
    }

    public synchronized List<Map<String, String>> queryWholeDeviceFilterByPkgLimit(String pkgName, int limit) {
        String sql = "SELECT imei,imsi,model,device,version_release,res FROM t_user_device_info WHERE imei NOT IN (SELECT imei FROM t_task_cache WHERE target_pkg = '%s' AND is_download = 1) LIMIT %s";
        sql = String.format(sql, pkgName, limit);
        query(sql);
        List<Map<String, String>> infos = new ArrayList<Map<String, String>>();
        try {
            if (result != null && result.first()) {
                while (!result.isAfterLast()) {
                    infos.add(DeviceUtils.convCons(result));
                    result.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResult();
            result = null;
        }
        return infos;
    }

    public synchronized  List<Map<String, String>> queryInstallAppInfo(String imei) {
        String sql = "SELECT ia.* FROM t_user_device_app_assoc daa JOIN t_user_installed_app ia ON ia.app_id = daa.app_id WHERE daa.imei = '%s'";
        sql = String.format(sql, imei);
        query(sql);
        List<Map<String, String>> infos = new ArrayList<Map<String, String>>();
        try {
            if (result != null && result.first()) {
                while (!result.isAfterLast()) {
                    infos.add(DeviceUtils.converInstalledAppToHW(result));
                    result.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResult();
            result = null;
        }
        return infos;
    }

    public synchronized  boolean insertRecord(String imei, String targetPkg, int isDownload) {
        // String sql =
        // "insert into t_task_cache (imei, target_pkg, ip, proxy_from, is_download, device_info, installed_app, create_time) "
        // + "values ('%s','%s','%s','%s','%s','%s','%s',now())";
        String sql = "insert into t_task_cache (imei, target_pkg, is_download, create_time) values ('%s', '%s', %s, now())";
        sql = String.format(sql, imei, targetPkg, isDownload);
        return execSQL(sql);
    }

    public void query(String sql) {
        if (conn == null) {
            return;
        }
        try {
            statement = conn.createStatement();
            result = statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // closeResult();
        }
    }

    public void closeResult() {
        try {
            if (result != null) {
                result.close();
                result = null;
            }
            if (statement != null) {
                statement.close();
                statement = null;
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public boolean execSQL(String sql) {
        boolean execResult = false;
        if (conn == null) {
            return execResult;
        }

        Statement statement = null;
        try {
            statement = conn.createStatement();
            if (statement != null) {
                execResult = statement.execute(sql);
            }
        } catch (SQLException e) {
            execResult = false;
        }
        return execResult;
    }
}
