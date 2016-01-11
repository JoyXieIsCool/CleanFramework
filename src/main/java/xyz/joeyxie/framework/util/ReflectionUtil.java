package xyz.joeyxie.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by joey on 2016/1/8.
 */
public class ReflectionUtil {

    /**
     * 创建实例
     */
    public static Object newInstance(Class<?> cls) {
        Object instance;
        try {
            instance = cls.newInstance();
        } catch (Exception e) {
            LogUtil.error("new instance failure", e);
            throw new RuntimeException(e);
        }

        return instance;
    }

    /**
     * 调用方法
     */
    public static Object invokeMethod(Object obj, Method method, Object... args) {
        Object result;
        try {
            method.setAccessible(true);
            result = method.invoke(obj, args);
        } catch (Exception e) {
            LogUtil.error("invoke method failure", e);
            throw new RuntimeException(e);
        }

        return result;
    }

    /**
     * 设置成员变量的值
     */
    public static void setField(Object obj, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            LogUtil.error("set field failure", e);
            throw new RuntimeException(e);
        }
    }

}
