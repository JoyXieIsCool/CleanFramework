package xyz.joeyxie.framework;

import xyz.joeyxie.framework.helper.*;
import xyz.joeyxie.framework.util.ClassUtil;

/**
 * 集中加载Helper类，从而让它们的static块初始化
 * Created by joey on 2016/1/16.
 */
public class HelperLoader {

    public static void init() {
        Class<?>[] classList = {
                ClassHelper.class,      // 1. 获取应用包名下的所有类的Class对象
                BeanHelper.class,       // 2. 为Class集合中的每一个类初始化一个对象，并添加到 Bean 容器中保存
                AopHelper.class,        // 3. 增加切面逻辑，为一些需要生成动态代理类的类生成动态代理对象并添加映射关系到 Bean 容器中
                IocHelper.class,        // 4. 根据注解为 Bean 容器中管理的对象实现依赖注入
                ControllerHelper.class  // 5. 根据Controller类中的注解生成<请求, 处理类方法>的映射关系，并保存到容器中
        };

        // 加载数组中所有的类
        for (Class<?> cls : classList) {
            ClassUtil.loadClass(cls.getName(), true);
        }
    }
}
