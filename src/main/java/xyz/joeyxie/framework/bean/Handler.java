package xyz.joeyxie.framework.bean;

import java.lang.reflect.Method;

/**
 * 封装处理请求的Action的信息，包括处理请求对应的Controller类和方法
 * Created by joey on 2016/1/16.
 */
public class Handler {

    // 处理一个Request的Controller类
    private Class<?> controllerClass;

    // Controller中处理一个Request的方法,即标注了@Action注解的方法
    private Method actionMethod;

    public Handler(Class<?> controllerClass, Method actionMethod) {
        this.controllerClass = controllerClass;
        this.actionMethod = actionMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public void setControllerClass(Class<?> controllerClass) {
        this.controllerClass = controllerClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }

    public void setActionMethod(Method actionMethod) {
        this.actionMethod = actionMethod;
    }
}
