package com.awesome.aso.job.task;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import bsh.EvalError;

import com.awesome.aso.job.ASOShellEngine;
import com.awesome.aso.job.hw.HWJobContext;
import com.awesome.aso.job.log.LoggerHelper;
import com.awesome.aso.job.util.CommUtils;

public class SimpleTaskExecutor implements ITaskExecutor {

    private ASOShellEngine engine;

    private AtomicBoolean isOver = new AtomicBoolean();

    public SimpleTaskExecutor() {
        super();
        engine = ASOShellEngine.getInstance();
    }

    @Override
    public void run(List<Map<String, String>> devices, float conRate) throws IllegalArgumentException {
        isOver.set(false);
        int allCount = devices.size();
        int viewCount = (int) Math.ceil(allCount * (1 - conRate));
        int[] viewIndex = CommUtils.randomArray(0, allCount - 1, viewCount);
        Iterator<Map<String, String>> iterator = devices.iterator();
        int retryCount = 0;
        while (iterator.hasNext() && !devices.isEmpty() && !isOver.get()) {
            Map<String, String> devInfo = iterator.next();
            String imei = devInfo.get("imei");
            String targetPkg = engine.getTargetPackage();
            HWJobContext context = createContext(devInfo, Arrays.binarySearch(viewIndex, 0) > -1);
            try {
                LoggerHelper.debug("imei : " + imei + " , 刷单開始!");
                engine.invokeJob(context, isOver);
                iterator.remove();
                retryCount = 0;
                LoggerHelper.debug("imei : " + imei + " , 刷单成功!");
                System.out.println(engine.getDataSoure().insertRecord(imei, targetPkg, 1));
//                engine.getDataSoure().insertRecord(imei, targetPkg, 1);
            } catch (EvalError e) {
                e.printStackTrace();
            }

            if (retryCount > 3) {
                retryCount = 0;
                LoggerHelper.debug("imei : " + imei + " , 刷单失败，已放弃!");
                iterator.remove();
            }

            retryCount++;
        }

    }

    private String genJobId(String imei, boolean download) {
        return imei + "_" + engine.getTargetPackage() + "_" + download;
    }

    public HWJobContext createContext(Map<String, String> devInfo, boolean isDownload) {
        String imei = devInfo.get("imei");
        HWJobContext jobContext = new HWJobContext(genJobId(imei, isDownload));
        jobContext.USER_KEY_DEVICE.set(devInfo);
        List<Map<String, String>> hwInstalledApps = engine.getDataSoure().queryInstallAppInfo(imei);
        jobContext.USER_KEY_INSTALL_LIST.set(hwInstalledApps);
        return jobContext;
    }

    @Override
    public void stop(String taskId) {
        isOver.set(true);
    }

}
