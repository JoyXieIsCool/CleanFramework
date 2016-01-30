package xyz.joeyxie.framework.helper;

import xyz.joeyxie.framework.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Bean 操作助手类, 可以存储Bean实例,相当于一个Bean容器，并可以根据Bean的Class对象获取对应的实例<br/>
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
     *
     * @param cls 需要获取对象的类型，如果是接口或者基类且Bean Map中不存在该类的映射，则查找该Class的实现类或子类
     */
    public static <T> T getBean(Class<?> cls) {
        if (BEAN_MAP.containsKey(cls)) {
            // 如果容器中有该类和其对象的映射，则直接返回保存的实例
            return (T) BEAN_MAP.get(cls);
        } else {
            // 如果容器中不存在该类，则尝试查找是否有它的子类或者实现类
            for (Map.Entry<Class<?>, Object> beanEntry : BEAN_MAP.entrySet()) {

                // 如果该entry的Class可以赋值给cls，则说明它是cls的子类或实现类，返回该entry中保存的对象
                if (cls.isAssignableFrom(beanEntry.getKey())) {
                    return (T) beanEntry.getValue();
                }
            }
        }

        // 如果容器中不存在cls类，也不存在该类的实现类或子类，则抛出异常
        throw new RuntimeException("can not get bean by class: " + cls);

    }


    /**
     * 添加Class和它对应的实例到容器中
     */
    public static void addBean(Class<?> cls, Object object) {
        BEAN_MAP.put(cls, object);
    }
}
