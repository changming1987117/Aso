package com.awesome.aso.job.task;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.awesome.aso.job.log.LoggerHelper;
import com.awesome.aso.job.util.CommUtils;

/**
 * 异步任务执行器
 */
public class AsyncTaskExecutor implements ITaskExecutor {

    private ExecutorService executorService = Executors.newCachedThreadPool();
    private SimpleTaskExecutor perTaskExcutor;

    public AsyncTaskExecutor() {
        super();
    }

    @Override
    public void run(List<Map<String, String>> info, float conRate) throws IllegalArgumentException {
        if (perTaskExcutor != null) {
            perTaskExcutor.stop("");
        }
        perTaskExcutor = new SimpleTaskExecutor();
        executorService.submit(new RunTask(perTaskExcutor, info, conRate));
    }

    @Override
    public void stop(String taskId) {
        // executorService.submit(new StopTask(taskExecutor));
        if (perTaskExcutor != null) {
            perTaskExcutor.stop("");
            perTaskExcutor = null;
        }
    }

    private static class RunTask implements Runnable {

        private final ITaskExecutor taskExecutor;

        private final List<Map<String, String>> info;
        private final float conRate;

        public RunTask(ITaskExecutor taskExecutor, List<Map<String, String>> info, float conRate) {
            this.taskExecutor = taskExecutor;
            this.info = info;
            this.conRate = conRate;
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            int runHours = CommUtils.getCurrHours();
            LoggerHelper.debug(runHours + "点:  任务开始执行。");
            try {
                taskExecutor.run(info, conRate);
            } catch (Exception e) {
                e.printStackTrace();
                LoggerHelper.debug(runHours + "点:  任务启动失败  ||-_- !");
            }
            LoggerHelper.debug(runHours + "点:  所有任务执行完毕。 耗时： " + (System.currentTimeMillis() - start));
        }

    }

}
