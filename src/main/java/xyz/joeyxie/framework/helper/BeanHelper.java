package xyz.joeyxie.framework.helper;

import xyz.joeyxie.framework.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Bean 操作助手类, 可以存储Bean实例,相当于一个Bean容器，并可以根据Bean的Class对象获取对应的实例
 * Created by joey on 2016/1/12.
 */
public class BeanHelper {
    /**
     * 定义 Bean 的Class与实例的映射
     */
    private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<Class<?>, Object>();

    static {
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
        for (Class<?> beanClass : beanClassSet) {
            Object obj = ReflectionUtil.newInstance(beanClass);
            BEAN_MAP.put(beanClass, obj);
        }
    }

    /**
     * 获取存储 Bean 映射的Map对象
     */
    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    /**
     * 根据Class对象获取映射中的 Bean 实例
     */
    public static <T> T getBean(Class<?> cls) {
        if (!BEAN_MAP.containsKey(cls)) {
            throw new RuntimeException("can not get bean by class: " + cls);
        }
        return (T) BEAN_MAP.get(cls);
    }
}
