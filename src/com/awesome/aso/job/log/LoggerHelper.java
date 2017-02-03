package com.awesome.aso.job.log;

import java.io.ByteArrayOutputStream;

import com.awesome.aso.job.log.ILogger.LEVEL;

public class LoggerHelper {
    public static ILogger RUNNING_LOGGER = new LoggerImp(LEVEL.DEBUG);

    public static void debug(String message) {
        RUNNING_LOGGER.debug(message);
    }

    public static void info(String message) {
        RUNNING_LOGGER.info(message);
    }

    public static void warn(String message) {
        RUNNING_LOGGER.warn(message);
    }

    public static void error(String message) {
        RUNNING_LOGGER.error(message);
    }

    public static void error(String message, Exception ex) {
        RUNNING_LOGGER.error(message + "\n" + exceptionStackTrace(ex));
    }

    public static String exceptionStackTrace(Exception ex) {
        String expMessage = "";
        try {
            ByteArrayOutputStream buf = new java.io.ByteArrayOutputStream();
            ex.printStackTrace(new java.io.PrintWriter(buf, true));
            expMessage = buf.toString();
            buf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return expMessage;
    }
}
