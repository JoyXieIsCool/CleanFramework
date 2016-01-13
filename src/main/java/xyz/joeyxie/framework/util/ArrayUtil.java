package xyz.joeyxie.framework.util;

/**
 * Created by joey on 2016/1/13.
 */
public class ArrayUtil {
    /**
     * 判断数组是否为空
     */
    public static boolean isNotEmpty(Object[] array) {
        return null != array && 0 != array.length;
    }

    /**
     * 判断数组是否非空
     */
    public static boolean isEmpty(Object[] array) {
        return null == array || 0 == array.length;
    }
}
