package xyz.joeyxie.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by joey on 2016/1/8.
 */
public class LogUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogUtil.class);

    public static void error(String msg, Throwable e) {
        LOGGER.error(msg, e);
    }

    public static void info(String msg, Throwable e) {
        LOGGER.info(msg, e);
    }
}
