package com.awesome.aso.job;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import com.awesome.aso.job.data.IDataSoure;
import com.awesome.aso.job.hw.HWJobContext;
import com.awesome.aso.job.plan.ITaskPlan;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.XThis;

public class ASOShellEngine {
    public static String BSH_FIELD_DATA_SOURE = "dataSoure";
    public static String BSH_FIELD_TASK_PLAN = "taskPlan";
    public static String BSH_FIELD_TARGET_PACKAGE = "targetPackage";
    public static String BSH_FIELD_JOB = "asoJob";
    private static ASOShellEngine engine;

    private Interpreter interpreter;

    private ITaskPlan taskPlan;

    private IDataSoure dataSoure;

    private String targetPackage;

    private XThis methodJob;

    private ASOShellEngine(String shellFile) throws FileNotFoundException, IOException, EvalError {
        super();
        interpreter = new Interpreter();
        interpreter.source(shellFile);
        taskPlan = (ITaskPlan) interpreter.get(BSH_FIELD_TASK_PLAN);
        dataSoure = (IDataSoure) interpreter.get(BSH_FIELD_DATA_SOURE);
        targetPackage = (String) interpreter.get(BSH_FIELD_TARGET_PACKAGE);
        methodJob = (XThis) interpreter.get(BSH_FIELD_JOB);
    }

    public IDataSoure getDataSoure() {
        return dataSoure;
    }

    public String getTargetPackage() {
        return targetPackage;
    }

    public XThis getMethodJob() {
        return methodJob;
    }

    public ITaskPlan getTaskPlan() {
        return taskPlan;
    }

    public Object invokeJob(HWJobContext context,AtomicBoolean isOver) throws EvalError {
        return methodJob.invokeMethod("run", new Object[] { context, isOver});
    }

    public static ASOShellEngine getInstance() {
        if (engine == null) {
            throw new RuntimeException("engine not init!!");
        }
        return engine;
    }

    public static synchronized void initEngine(String shellFile) throws FileNotFoundException, IOException, EvalError {
        if (engine == null) {
            engine = new ASOShellEngine(shellFile);
        }
    }

}
