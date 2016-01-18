package xyz.joeyxie.framework.helper;

import xyz.joeyxie.framework.annotation.Action;
import xyz.joeyxie.framework.bean.Handler;
import xyz.joeyxie.framework.bean.Request;
import xyz.joeyxie.framework.util.ArrayUtil;
import xyz.joeyxie.framework.util.CollectionUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 控制器助手类，保存所有Controller方法和Request之间的映射关系
 * Created by joey on 2016/1/16.
 */
public class ControllerHelper {

    // 保存请求与Controller处理方法之间的映射关系
    private static final Map<Request, Handler> ACTION_MAP = new HashMap<Request, Handler>();

    static {

        // 从 Bean 容器中拿到它管理的所有Controller类的集合
        Set<Class<?>> controllerClassesSet = ClassHelper.getControllerClassSet();

        if (CollectionUtil.isNotEmpty(controllerClassesSet)) {

            // 遍历这些Controller类
            for (Class<?> controllerClass : controllerClassesSet) {

                // 获取Controller类中所有定义的方法
                Method[] methods = controllerClass.getDeclaredMethods();
                if (ArrayUtil.isNotEmpty(methods)) {

                    // 遍历这些方法，判断是否有@Action注解，有的话则添加对应的映射关系到ACTION_MAP中
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(Action.class)) {

                            // 从注解中获取 URL 映射规则
                            Action action = method.getAnnotation(Action.class);
                            String mapping = action.value();

                            if (mapping.matches("\\w+:/\\w*")) {

                                String[] array = mapping.split(":");
                                // 数组长度必须为2，一个元素表示请求类型，另一个表示响应的路径
                                if (ArrayUtil.isNotEmpty(array) && 2 == array.length) {

                                    String requestMethod = array[0], requestPath = array[1];
                                    // 用小写格式保存响应方法
                                    Request request = new Request(requestMethod.toLowerCase(), requestPath);
                                    Handler handler = new Handler(controllerClass, method);

                                    // 将映射关系添加到ACTION_MAP中保存
                                    ACTION_MAP.put(request, handler);
                                }
                            }
                        }
                    }
                }
            }
        }
    } // 结束static初始化块


    /**
     * 根据请求类型和对应路径，查找相应的处理方法的Handler对象
     */
    public static Handler getHandler(String requestMethod, String requestPath) {
        Request request = new Request(requestMethod, requestPath);
        return ACTION_MAP.get(request);
    }
}
