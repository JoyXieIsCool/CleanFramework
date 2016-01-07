package xyz.joeyxie.framework.util;

import java.util.Collection;
import java.util.Map;

/**
 * Created by joey on 2016/1/6.
 */
public class CollectionUtil {

    /**
     * 判断Collection是否为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return null == collection || collection.isEmpty();
    }

    /**
     * 判断Collection是否非空
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 判断Map是否为空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return null == map || map.isEmpty();
    }

    /**
     * 判断Map是否非空
     */
    public static boolean isNotEmpty(Map<?,?> map) {
        return !isEmpty(map);
    }
}
