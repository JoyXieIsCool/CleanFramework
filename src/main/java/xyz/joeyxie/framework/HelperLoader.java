package xyz.joeyxie.framework;

import xyz.joeyxie.framework.helper.BeanHelper;
import xyz.joeyxie.framework.helper.ClassHelper;
import xyz.joeyxie.framework.helper.ControllerHelper;
import xyz.joeyxie.framework.helper.IocHelper;
import xyz.joeyxie.framework.util.ClassUtil;

/**
 * 集中加载Helper类，从而让它们的static块初始化
 * Created by joey on 2016/1/16.
 */
public class HelperLoader {

    public static void init() {
        Class<?>[] classList = {
                ClassHelper.class,
                BeanHelper.class,
                IocHelper.class,
                ControllerHelper.class
        };

        for (Class<?> cls : classList) {
            ClassUtil.loadClass(cls.getName(), true);
        }
    }
}
