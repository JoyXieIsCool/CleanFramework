package xyz.joeyxie.framework.proxy;

import xyz.joeyxie.framework.util.LogUtil;

import java.lang.reflect.Method;

/**
 * 切面代理类
 * Created by joey on 2016/1/30.
 */
public abstract class AspectProxy implements Proxy {

    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result = null;

        Class<?> cls = proxyChain.getTargetClass();
        Method method = proxyChain.getTargetMethod();
        Object[] params = proxyChain.getMethodParams();

        begin();
        try {
            // 如果返回true则进行拦截，执行切面逻辑
            if (intercept(cls, method, params)) {

                // 执行前置切面逻辑
                before(cls, method, params);

                /**
                 * 调用代理链中的下一个Proxy接口实现类去执行切面逻辑，因此代理链中的每一个
                 * Proxy实现类的before()和after()方法的执行顺序是类似栈的，即如果一个
                 * Proxy类的before()方法在其它的Proxy类的before()之前执行，那它的after()
                 * 方法就会在这些Proxy类的after()方法执行完之后才被执行
                 */
                result = proxyChain.doProxyChain();

                // 执行后置切面逻辑
                after(cls, method, params, result);
            } else {
                // intercept()方法返回false说明不需要执行切面逻辑，直接执行代理链中的下一个切面
                result = proxyChain.doProxyChain();
            }
        } catch (Exception e) {
            LogUtil.error("proxy failure", e);
            error(cls, method, params, e);
            throw e;
        } finally {
            end();
        }

        return result;
    }


    // 交由子类实现
    public void begin() {
    }

    // 子类覆盖该方法添加具体实现
    public boolean intercept(Class<?> cls, Method method, Object[] params) throws Throwable{
        return true;
    }

    // 被代理方法执行前的前置逻辑，由子类实现
    public void before(Class<?> cls, Method method, Object[] params) throws Throwable {
    }

    // 被代理方法执行后的后置逻辑，由子类实现
    public void after(Class<?> cls, Method method, Object[] params, Object result) throws Throwable {
    }

    // 代理方法执行出错时的回调处理方法，由子类实现
    public void error(Class<?> cls, Method method, Object[] params, Throwable e) {
    }

    // 代理方法执行完毕后(after方法执行后或者抛出异常error方法执行之后)的最后处理逻辑，即无论如何都会执行的方法，总是在最后被执行
    public void end(){
    }

}
