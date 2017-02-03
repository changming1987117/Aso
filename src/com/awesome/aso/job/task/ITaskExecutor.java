package com.awesome.aso.job.task;

import java.util.List;
import java.util.Map;

public interface ITaskExecutor {

    public void run(List<Map<String, String>> info, float conRate) throws IllegalArgumentException;

    public void stop(String taskId);

}
