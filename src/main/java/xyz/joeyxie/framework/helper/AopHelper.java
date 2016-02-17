package xyz.joeyxie.framework.helper;

import xyz.joeyxie.framework.annotation.Aspect;
import xyz.joeyxie.framework.annotation.Service;
import xyz.joeyxie.framework.proxy.AspectProxy;
import xyz.joeyxie.framework.proxy.Proxy;
import xyz.joeyxie.framework.proxy.ProxyFactory;
import xyz.joeyxie.framework.proxy.TransactionProxy;
import xyz.joeyxie.framework.util.LogUtil;
import xyz.joeyxie.framework.util.ReflectionUtil;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * 实现AOP，用于获取需要代理的目标类和被拦截的切面类实例，同通过ProxyFactory的createProxy方法来创建代理对象
 * Created by joey on 2016/1/31.
 */
public class AopHelper {

    static {
        try {
            Map<Class<?>, Set<Class<?>>> proxyMap = createProxyToTargetClassesSetMap();
            Map<Class<?>, List<Proxy>> targetMap = createTargetToProxyChainMap(proxyMap);

            for (Map.Entry<Class<?>, List<Proxy>> targetEntry : targetMap.entrySet()) {
                // 获取需要被代理的目标类
                Class<?> targetClass = targetEntry.getKey();

                // 获取目标类的所有拦截器接口的实现类，即目标类的所有横切逻辑类
                List<Proxy> proxyList = targetEntry.getValue();

                // 创建动态代理类，采用链式代理添加切面逻辑
                Object proxyClass = ProxyFactory.createProxy(targetClass, proxyList);

                // 将目标类的Class和它的动态代理类对象添加到 Bean 容器中
                BeanHelper.addBean(targetClass, proxyClass);
            }
        } catch (Exception e) {
            LogUtil.error("AOP failure", e);
        }
    }

    /**
     * 获取 @Aspect 注解中定义的要被代理的类的Class集合
     */
    private static Set<Class<?>> createTargetClassSet(Aspect aspect) throws Exception {
        Set<Class<?>> targetClassSet = new HashSet<Class<?>>();

        // 获取注解中定义的要被代理的类的Class
        Class<? extends Annotation> annotation = aspect.value();

        // 注解不能为空，且不能代理Aspect自己，否则会死循环
        if (null != annotation && !annotation.equals(Aspect.class)) {
            targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));
        }

        return targetClassSet;
    }


    /**
     * 获取切面类(即被@Aspect注解的类)与目标代理类集合之间的映射关系，一个切面类可以对应一个或多个目标类。
     * 切面类必须继承 AspectProxy 抽象类，同时要带有 @Aspect 注解才能将切面逻辑切入到目标类中。
     */
    private static Map<Class<?>, Set<Class<?>>> createProxyToTargetClassesSetMap() throws Exception {
        Map<Class<?>, Set<Class<?>>> proxyMap = new HashMap<Class<?>, Set<Class<?>>>();

        addAspectProxy(proxyMap);
        addTransactionProxy(proxyMap);

        return proxyMap;
    }

    /**
     * 创建切面代理和被它代理类的集合之间的的映射关系，并保存到proxyMap中
     * @param proxyMap
     */
    private static void addAspectProxy(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception{
        // 从 Bean 容器中获取 AspectProxy 类的子类，即所有实现了切面逻辑的类
        Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);

        for (Class<?> proxyClass : proxyClassSet) {

            if (!proxyClass.isAnnotationPresent(Aspect.class)) {

                // 如果该类没有@Aspect注解，则忽略
                continue;
            }

            Aspect aspect = proxyClass.getAnnotation(Aspect.class);
            Set<Class<?>> targetClassSet = createTargetClassSet(aspect);

            // 把切面逻辑类和目标代理类集合的映射关系添加到Map中
            proxyMap.put(proxyClass, targetClassSet);
        }
    }

    /**
     * 创建事务代理和service类集合之间的映射关系，并保存到proxyMap中
     * @param proxyMap
     */
    private static void addTransactionProxy(Map<Class<?>, Set<Class<?>>> proxyMap) {
        // 获取所有被 @Service 注解标记的类
        Set<Class<?>> serviceClassSet = ClassHelper.getClassSetByAnnotation(Service.class);

        // 添加事务代理类和service类结合之间的映射
        proxyMap.put(TransactionProxy.class, serviceClassSet);
    }

    /**
     * 创建目标类和它的代理链之间的映射，每个被代理目标类的Class都对应于一个List，其中存放的是所有横切逻辑的Proxy接口的实现类。
     * @param proxyMap 切面类(即被@Aspect注解的类)与所有被它代理的类的映射
     */
    public static Map<Class<?>, List<Proxy>> createTargetToProxyChainMap(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
        Map<Class<?>, List<Proxy>> targetMap = new HashMap<Class<?>, List<Proxy>>();

        for (Map.Entry<Class<?>, Set<Class<?>>> proxyEntry : proxyMap.entrySet()) {
            // 获取切面类的Class
            Class<?> proxyClass = proxyEntry.getKey();

            // 获取所有被该切面代理的目标类的集合
            Set<Class<?>> targetClassSet = proxyEntry.getValue();

            // 反转映射关系，对于每一个目标类，查找它对应的所有切面类
            for (Class<?> targetClass : targetClassSet) {

                // 创建切面类的实例
                Proxy proxy = (Proxy) ReflectionUtil.newInstance(proxyClass);

                if (targetMap.containsKey(targetClass)) {
                    targetMap.get(targetClass).add(proxy);
                } else {
                    List<Proxy> proxyList = new ArrayList<Proxy>();
                    proxyList.add(proxy);
                    targetMap.put(targetClass, proxyList);
                }
            }
        }

        return targetMap;
    }

}
