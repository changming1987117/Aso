package com.awesome.aso.job.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.awesome.aso.job.log.LoggerHelper;

public class HttpRequestUtils {

    public static String doPostRequest(String urlStr, String data) throws Exception {
        return doPostRequest(urlStr, data, null);
    }

    public static String doPostRequest(String urlStr, String data, String proxyHost, int proxyPort) throws Exception {
        return doPostRequest(urlStr, data, new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)));
    }

    public static String doPostRequest(String urlStr, String data, Proxy proxy) throws Exception {
        //
        int retryCount = 3;
        do {
            URL url = new URL(urlStr);
            HttpURLConnection httpConn = null;
            try {
                httpConn = getUrlConnection(url, proxy);
                httpConn.setRequestMethod("POST");
                httpConn.setUseCaches(false);
                httpConn.setRequestProperty("Connection", "Keep-Alive");
                httpConn.setRequestProperty("User-Agent", "HiSpace_7.0.1.301_generic");
                httpConn.setRequestProperty("Host", "hispaceclt.hicloud.com");
                httpConn.setRequestProperty("Content-Type", "application/x-gzip");
                httpConn.setRequestProperty("Content-Encoding", "gzip");
                httpConn.setDoOutput(true);
                httpConn.setDoInput(true);
                GZIPOutputStream gos = new GZIPOutputStream(httpConn.getOutputStream());
                OutputStreamWriter osw = new OutputStreamWriter(gos);
                osw.write(data);
                osw.flush();
                osw.close();

                int respCode = httpConn.getResponseCode();
                if (respCode == 200) {
                    Map<String, String> header = getHttpResponseHeader(httpConn);
                    InputStream is = httpConn.getInputStream();
                    if ("gzip".equals(header.get("Content-Encoding"))) {
                        is = new GZIPInputStream(is);
                    }
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    reader.close();
                    return result.toString();
                }
            } catch (Exception ex) {
                retryCount--;
            } finally {
                if (httpConn != null) {
                    httpConn.disconnect();
                }
            }
        } while (retryCount > 0);
        return null;
    }

    private static HttpURLConnection getUrlConnection(URL url, Proxy proxy) throws IOException {
        HttpURLConnection httpConn;
        if (proxy != null) {
            httpConn = (HttpURLConnection) url.openConnection(proxy);
        } else {
            httpConn = (HttpURLConnection) url.openConnection();
        }
        httpConn.setConnectTimeout(3 * 1000);
        httpConn.setReadTimeout(10 * 1000);
        return httpConn;
    }

    private static Map<String, String> getHttpResponseHeader(HttpURLConnection http) throws UnsupportedEncodingException {
        Map<String, String> header = new LinkedHashMap<String, String>();
        for (int i = 0;; i++) {
            String mine = http.getHeaderField(i);
            if (mine == null)
                break;
            header.put(http.getHeaderFieldKey(i), mine);
        }
        return header;
    }

    public static boolean downloadFile(String destUrl, String fileName) throws Exception {
        return downloadFile(destUrl, fileName, null, null);
    }

    public static boolean downloadFile(String destUrl, String fileName, String proxyHost, int proxyPort) throws Exception {
        return downloadFile(destUrl, fileName, null, new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)));
    }

    public static boolean downloadFile(String destUrl, String fileName, String range, Proxy proxy) throws IOException {
        int BUFFER_SIZE = 1024 * 30;
        OutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpConn = null;
        URL url = null;
        try {
            url = new URL(destUrl);
            httpConn = getUrlConnection(url, proxy);
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("User-Agent", "HiSpace");
            httpConn.setRequestProperty("Host", url.getHost());
            if (range != null && range != "") {
                httpConn.setRequestProperty("Range", "bytes=" + range);
            }
            httpConn.connect();
            bis = new BufferedInputStream(httpConn.getInputStream());
            if (fileName != null) {
                File file = new File(fileName);
                if (!file.exists()) {
                    if (file.getParentFile() != null && !file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    file.createNewFile();
                }
                fos = new FileOutputStream(fileName);
            } else {
                fos = new ByteArrayOutputStream();
            }
            byte[] buf = new byte[BUFFER_SIZE];
            int size = 0;
            // 保存文件
            while ((size = bis.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }
        } finally {
            if (fos != null) {
                fos.close();
            }
            if (bis != null) {
                bis.close();
            }
            if (httpConn != null) {
                httpConn.disconnect();
            }
        }

        return true;
    }

    public static boolean downloadToCache(String destUrl, String range, Proxy proxy) throws IOException {
        int BUFFER_SIZE = 1024 * 30;
        ByteArrayOutputStream baos = null;
        BufferedOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpConn = null;
        URL url = null;
        try {
            url = new URL(destUrl);
            httpConn = getUrlConnection(url, proxy);
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("User-Agent", "HiSpace");
            httpConn.setRequestProperty("Host", url.getHost());
            if (range != null && range != "") {
                httpConn.setRequestProperty("Range", "bytes=" + range);
            }
            httpConn.connect();
            bis = new BufferedInputStream(httpConn.getInputStream());
            baos = new ByteArrayOutputStream();
            fos = new BufferedOutputStream(baos);
            byte[] buf = new byte[BUFFER_SIZE];
            int size = 0;
            // 保存文件
            while ((size = bis.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }
        } finally {
            if (baos != null) {
                baos.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (bis != null) {
                bis.close();
            }
            if (httpConn != null) {
                httpConn.disconnect();
            }
        }

        return true;
    }

    public static String doGet(String urlStr) throws Exception {
        // http://dev.kuaidaili.com/api/getproxy/?orderid=945697743985035&num=100&b_pcchrome=1&b_pcie=1&b_pcff=1&protocol=1&method=2&an_an=1&an_ha=1&sep=2
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setReadTimeout(3 * 1000);
        conn.setRequestMethod("GET");
        InputStream is = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
            result.append("\n");
        }
        reader.close();

        return result.toString();
    }

    public static String resetProxyEnable(String resetUrl,AtomicBoolean isOver) {
        Pattern pattern = Pattern.compile("^([0-9]{1,3}\\.){3}[0-9]{1,3}");
        String remoteIp = "";
        Matcher matcher = null;
        do {
            try {
                remoteIp = resetProxy(resetUrl);
            } catch (Exception e) {
//                e.printStackTrace();
            }
            if (remoteIp == null) {
                remoteIp = "";
            }
            remoteIp = remoteIp.replaceAll("\n|\r", "");
            matcher = pattern.matcher(remoteIp);
            LoggerHelper.debug("代理重置成功   remote_ip = " + remoteIp + "---");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        } while ((remoteIp == "" || !matcher.find()) && !isOver.get());
        return remoteIp;
    }

    public static String resetProxy(String resetUrl) throws Exception {
        URL url = new URL(resetUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setReadTimeout(60 * 1000);
        conn.setRequestMethod("GET");
        InputStream is = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
            result.append("\n");
        }
        reader.close();
        return result.toString();
    }

    public static boolean checkProxyEnable(String host, int port) {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
        try {
            URL url = new URL("http://www.baidu.com/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
            conn.setConnectTimeout(3 * 1000);
            conn.setReadTimeout(3 * 1000);
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() == 200)
                return true;
        } catch (IOException e) {
            // e.printStackTrace();
        }
        return false;
    }
}
