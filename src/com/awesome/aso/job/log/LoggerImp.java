package com.awesome.aso.job.log;

import org.apache.log4j.Logger;

public class LoggerImp implements ILogger {

    public Logger LOGGER = Logger.getLogger("running");

    private final LEVEL CURR_LEVEL;

    public LoggerImp(LEVEL level) {
        CURR_LEVEL = level;
    }

    @Override
    public void error(String msg) {
        log(LEVEL.ERROR, msg);
    }

    @Override
    public void warn(String msg) {
        log(LEVEL.WARN, msg);
    }

    @Override
    public void info(String msg) {
        log(LEVEL.INFO, msg);
    }

    @Override
    public void debug(String msg) {
        log(LEVEL.DEBUG, msg);
    }

    @Override
    public void log(LEVEL level, String msg) {
        if (CURR_LEVEL.ordinal() > level.ordinal()) {
            // 当前log级别比输出log级别要高，不输出log。
            return;
        }
        switch (level) {
        case ERROR:
            LOGGER.error(msg);
            break;
        case WARN:
            LOGGER.warn(msg);
            break;
        case INFO:
            LOGGER.info(msg);
            break;
        case DEBUG:
            LOGGER.debug(msg);
            break;

        default:
            break;
        }
    }
}
