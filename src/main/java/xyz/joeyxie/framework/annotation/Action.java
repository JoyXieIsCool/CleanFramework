package xyz.joeyxie.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Action方法注解，用在方法上，标记该方法作为一个处理请求的方法
 * Created by joey on 2016/1/7.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {
    /**
     * 存储请求的类型和映射路径
     */
    String value();
}
