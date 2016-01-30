package xyz.joeyxie.framework.helper;

import xyz.joeyxie.framework.annotation.Controller;
import xyz.joeyxie.framework.annotation.Service;
import xyz.joeyxie.framework.util.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * 获取应用的基础包名下所有被注解的类
 * Created by joey on 2016/1/7.
 */
public class ClassHelper {
    /**
     * 存放Class对象的集合
     */
    private static final Set<Class<?>> CLASS_SET;

    static {
        String basePackage = ConfigHelper.getAppBasePackage();
        CLASS_SET = ClassUtil.getClassSet(basePackage);
    }

    /**
     * 获取应用包名下所有类的Class对象的集合
     */
    public static Set<Class<?>> getClassSet() {
        return CLASS_SET;
    }

    /**
     * 获取应用包名下所有被@Service注解标记的类
     */
    public static Set<Class<?>> getServiceClassSet() {
        Set<Class<?>> serviceClasses = new HashSet<Class<?>>();
        for (Class<?> cls : CLASS_SET) {
            if (cls.isAnnotationPresent(Service.class)) {
                serviceClasses.add(cls);
            }
        }

        return serviceClasses;
    }

    /**
     * 获取应用包名下的所有被@Controller注解标记的类
     */
    public static Set<Class<?>> getControllerClassSet() {
        Set<Class<?>> controllerClasses = new HashSet<Class<?>>();
        for (Class<?> cls : CLASS_SET) {
            if (cls.isAnnotationPresent(Controller.class)) {
                controllerClasses.add(cls);
            }
        }

        return controllerClasses;
    }

    /**
     * 获取应用包名下所有的Bean类，包括Service和Controller
     */
    public static Set<Class<?>> getBeanClassSet() {
        Set<Class<?>> beanClasses = new HashSet<Class<?>>();
        beanClasses.addAll(getServiceClassSet());
        beanClasses.addAll(getControllerClassSet());

        return beanClasses;
    }

    /**
     * 获取应用包名下某个父类(或接口)的所有子类(或者实现类)
     */
    public static Set<Class<?>> getClassSetBySuper(Class<?> superClass) {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> cls : CLASS_SET) {

            // 如果 cls 的对象可以赋值给 superClass 的对象，且它们不等，则说明 cls 是superClass的子类或实现类
            if (superClass.isAssignableFrom(cls) && !superClass.equals(cls)) {
                classSet.add(cls);
            }
        }

        return classSet;
    }

    /**
     * 获取应用包名下带有指定注解的所有类的Class
     */
    public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> annotationClass) {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> cls : CLASS_SET) {

            // 如果 cls 上出现了 annotationClass 注解则添加该 cls 到集合中
            if (cls.isAnnotationPresent(annotationClass)) {
                classSet.add(cls);
            }
        }

        return classSet;
    }
}
