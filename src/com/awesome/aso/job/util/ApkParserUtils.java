package com.awesome.aso.job.util;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dongliu.apk.parser.ApkParser;
import net.dongliu.apk.parser.bean.ApkMeta;
import net.dongliu.apk.parser.bean.ApkSignStatus;
import net.dongliu.apk.parser.bean.CertificateMeta;

import com.awesome.aso.job.hw.core.FileUtil;

public class ApkParserUtils {

    public static Map<String, String> downloadAndParseAPPInfo(String url) throws Exception {
        String apkFile = CommUtils.findFileName(url);
        if (!new File(apkFile).exists()) {
            System.out.println(apkFile+" APK 不存在");
            if (!HttpRequestUtils.downloadFile(url, apkFile)) {
                return null;
            }
        }
        return parser(apkFile);
    }

    public static Map<String, String> parser(String apkFile) throws Exception {
        Map<String, String> app = new HashMap<String, String>();
        String md5 = FileUtil.getFileMD5(apkFile);
        String hashCode = FileUtil.getHashCode(new File(apkFile));
        app.put("partHash", hashCode);
        app.put("md5Hash", md5);
        //
        try (ApkParser apkParser = new ApkParser(new File(apkFile))) {
            ApkMeta apkMeta = apkParser.getApkMeta();
            // app.setAppName(apkMeta.getName());
            app.put("isPre", "0");
            app.put("package", apkMeta.getPackageName());
            app.put("versionCode", apkMeta.getVersionCode().toString());
            app.put("targetSdkVersion", apkMeta.getTargetSdkVersion());
            app.put("oldVersion", apkMeta.getVersionName());
            ApkSignStatus signStatus = apkParser.verifyApk();
            if (signStatus == ApkSignStatus.signed) {
                List<CertificateMeta> certs = apkParser.getCertificateMetaList();
                if (certs != null && certs.size() > 0) {
                    CertificateMeta certificateMeta = certs.get(0);
                    String signature = certificateMeta.toCharsString();
                    // app.set= ss;
                    String ss = com.awesome.aso.job.hw.core.Utils.crc32Encoding(signature);
                    app.put("s", ss);
                }
            }
        }
        return app;
    }
}
