package com.awesome.aso.job.plan;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.awesome.aso.job.data.IDataSoure;
import com.awesome.aso.job.log.LoggerHelper;
import com.awesome.aso.job.task.AsyncTaskExecutor;
import com.awesome.aso.job.task.ITaskExecutor;
import com.awesome.aso.job.util.CommUtils;

public class EveryDayHourlyTimerPlan extends TimerTask implements ITaskPlan {
    private Timer timer = new Timer();
    // 执行任务。
    private ITaskExecutor executor = new AsyncTaskExecutor();
    // 任务数量计算。
    private EveryDayHourlyIncrConRateCalcu calcu;

    private int runCycle = 0;

    private IDataSoure dataSoure;

    private String pkg;

    public EveryDayHourlyTimerPlan(String pkg, IDataSoure dataSoure, EveryDayHourlyIncrConRateCalcu calcu) {
        super();
        this.pkg = pkg;
        this.calcu = calcu;
        this.dataSoure = dataSoure;
    }

    @Override
    public void run() {
        try {
            // 当前是几点
            int hour = CommUtils.getCurrHours();
            int day = runCycle / 24;
            // 当前时间要执行任务Job数量
            int count = calcu.getNumberByDayHours(day, hour);
            LoggerHelper.debug("运行 数量：： " + count);
            List<Map<String, String>> devices = dataSoure.queryWholeDeviceFilterByPkgLimit(pkg, count);
            LoggerHelper.debug("查询到数量 ：： " + devices.size());
            //
            executor.run(devices, calcu.getConversionRate());
            LoggerHelper.debug("任务提交结束。。。");
            //
            runCycle++;
            // 执行完毕
            if (runCycle >= calcu.getRunCycle()) {
                stop();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            // LoggerHelper.error("程序异常退出", ex);
            System.exit(-1);
        }
    }

    @Override
    public void start() {
        LoggerHelper.debug("启动 ： \n" + calcu);
        timer.schedule(this, CommUtils.getHoursPlus(1), CommUtils.ONE_HOURS);
//        timer.schedule(this, CommUtils.getSecondPlus(5), 300 * 1000);
    }

    @Override
    public void stop() {
        timer.cancel();
        // executor.stop();
    }
}
