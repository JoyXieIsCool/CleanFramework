package xyz.joeyxie.framework.helper;

import xyz.joeyxie.framework.annotation.DI;
import xyz.joeyxie.framework.util.ArrayUtil;
import xyz.joeyxie.framework.util.CollectionUtil;
import xyz.joeyxie.framework.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 实现依赖注入功能的类<br/>
 * Created by joey on 2016/1/13.
 */
public class IocHelper {

    static {
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if (CollectionUtil.isNotEmpty(beanMap)) {
            // 遍历Bean Map
            for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
                // 获取Bean的Class和实例
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();

                // 获取Bean的类定义中声明的所有成员变量,包括private的
                Field[] beanFields = beanClass.getDeclaredFields();
                if (ArrayUtil.isNotEmpty(beanFields)) {
                    // 遍历Bean的每一个成员变量，判断是否需要依赖注入
                    for (Field beanField : beanFields) {
                        // 如果当前的成员变量带有@Inject注解，则需要注入
                        if (beanField.isAnnotationPresent(DI.class)) {
                            // 在Bean容器中获取需要注入的实例
                            Class<?> beanFieldClass = beanField.getType();
                            Object beanFieldInstance = BeanHelper.getBean(beanFieldClass);
                            if (null != beanFieldInstance) {
                                // 通过反射初始化BeanField的值
                                ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
                            }
                        }
                    }
                }
            }
        }

    }
}
