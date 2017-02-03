package com.awesome.aso.job.log;

public interface ILogger {

    public static enum LEVEL {
        DEBUG, INFO, WARN, ERROR,
    }

    public void error(String msg);

    public void warn(String msg);

    public void info(String msg);

    public void debug(String msg);

    public void log(LEVEL level, String msg);
}
