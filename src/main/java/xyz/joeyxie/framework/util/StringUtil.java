package xyz.joeyxie.framework.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by joey on 2016/1/6.
 */
public class StringUtil {
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isEmpty(String str) {
        return null == str || 0 == str.length();
    }
}
