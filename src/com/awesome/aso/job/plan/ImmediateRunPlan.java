package com.awesome.aso.job.plan;

import java.util.List;
import java.util.Map;

import com.awesome.aso.job.data.IDataSoure;
import com.awesome.aso.job.log.LoggerHelper;
import com.awesome.aso.job.task.AsyncTaskExecutor;
import com.awesome.aso.job.task.ITaskExecutor;

public class ImmediateRunPlan implements ITaskPlan {
    // 执行任务。
    private ITaskExecutor executor = new AsyncTaskExecutor();
    // 构造任务。
    // // 数据源
    // private IDataSource dataSource;
    // 执行数量
    // 转换率
    private IDataSoure dataSoure;

    private String pkg;
    private int number;
    private float conversionRate;

    // 数据源

    public ImmediateRunPlan(String pkg, IDataSoure dataSoure, int number, float conversionRate) {
        super();
        this.conversionRate = conversionRate;
        this.number = number;
        this.dataSoure = dataSoure;
    }

    @Override
    public void start() {
        LoggerHelper.debug("运行 数量：： " + number);
        List<Map<String, String>> devices = dataSoure.queryWholeDeviceFilterByPkgLimit(pkg, number);
        LoggerHelper.debug("查询到数量 ：： " + devices.size());
        //
        executor.run(devices, conversionRate);
        LoggerHelper.debug("任务提交结束。。。");
    }

    @Override
    public void stop() {

    }

}
