package xyz.joeyxie.framework.aspect;

import xyz.joeyxie.framework.annotation.Aspect;
import xyz.joeyxie.framework.annotation.Controller;
import xyz.joeyxie.framework.proxy.AspectProxy;
import xyz.joeyxie.framework.util.LogUtil;

import java.lang.reflect.Method;

/**
 * 拦截 Controller 的所有方法
 * Created by joey on 2016/1/30.
 */
@Aspect(Controller.class)
public class ControllerAspect extends AspectProxy{

    private long begin;

    @Override
    public void before(Class<?> cls, Method method, Object[] params) throws Throwable {
        LogUtil.debug("---------- begin ----------");
        LogUtil.debug(String.format("class: %s,", cls.getName()));
        LogUtil.debug(String.format("method: %s", method.getName()));
        begin = System.currentTimeMillis();
    }

    @Override
    public void after(Class<?> cls, Method method, Object[] params, Object result) throws Throwable {
        LogUtil.debug(String.format("time: %dms", System.currentTimeMillis() - begin));
        LogUtil.debug("---------- end ----------");
    }
}
