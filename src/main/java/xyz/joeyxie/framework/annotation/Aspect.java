package xyz.joeyxie.framework.annotation;

import java.lang.annotation.*;

/**
 * 切面注解类
 * Created by joey on 2016/1/29.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

    /**
     * 注解
     */
    Class<? extends Annotation> value();
}
