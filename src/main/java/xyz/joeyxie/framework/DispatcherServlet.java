package xyz.joeyxie.framework;

import org.apache.commons.lang3.StringUtils;
import xyz.joeyxie.framework.bean.Handler;
import xyz.joeyxie.framework.bean.ModelAndView;
import xyz.joeyxie.framework.bean.Parameter;
import xyz.joeyxie.framework.bean.ResponseData;
import xyz.joeyxie.framework.helper.BeanHelper;
import xyz.joeyxie.framework.helper.ConfigHelper;
import xyz.joeyxie.framework.helper.ControllerHelper;
import xyz.joeyxie.framework.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求转发器
 * Created by joey on 2016/1/17.
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        // 初始化相关的 Helper 类
        HelperLoader.init();

        // 获取 ServletContext 对象，用于注册 Servlet
        ServletContext servletContext = config.getServletContext();

        // 注册处理 JSP 的 Servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");

        // 注册处理静态资源的默认 Servlet
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath());
    }


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取请求方法(小写)与请求路径
        String requestMethod = req.getMethod().toLowerCase(),
                requestPath = req.getPathInfo();

        // 获取 Action 处理器
        Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);

        if (null != handler) {
            // 获取 Controller 类， 并从 Bean 容器中获取该类的对象
            Class<?> controllerClass = handler.getControllerClass();
            Object controllerBean = BeanHelper.getBean(controllerClass);

            // 创建请求参数对象
            Map<String, Object> paramMap = new HashMap<String, Object>();
            Enumeration<String> paramNames = req.getParameterNames();

            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();
                String paramValue = req.getParameter(paramName);
                paramMap.put(paramName, paramValue);
            }

            // TODO: 搞清楚为什么要读取InputStream，ServletRegistration是干嘛的
            String body = CodecUtil.decodeURL(StreamUtil.getString(req.getInputStream()));
            if (StringUtil.isNotEmpty(body)) {

                // 把请求中的文本按照&分隔开, 得到一个键值对的数组，每个元素是一个key=value的字符串
                String[] params = StringUtils.split(body, "&");
                if (ArrayUtil.isNotEmpty(params)) {

                    // 遍历每一个请求参数
                    for (String param : params) {
                        // 把key=value分隔开
                        String[] array = StringUtils.split(param, "=");
                        if (ArrayUtil.isNotEmpty(array) && 2 == array.length) {
                            String paramName = array[0], paramValue = array[1];
                            paramMap.put(paramName, paramValue);
                        }
                    }
                }
            }

            Parameter param = new Parameter(paramMap);

            // 调用 Action 方法
            Method actionMethod = handler.getActionMethod();
            Object result = ReflectionUtil.invokeMethod(controllerBean, actionMethod, param);

            // 根据 Action 方法返回值的类型决定进行页面跳转还是直接返回数据
            if (result instanceof ModelAndView) {

                // 返回 JSP 页面
                ModelAndView view = (ModelAndView) result;
                String dispatchPath = view.getViewPath();

                if (StringUtil.isNotEmpty(dispatchPath)) {
                    if (dispatchPath.startsWith("/")) {
                        // 直接转发
                        resp.sendRedirect(req.getContextPath() + dispatchPath);
                    } else {
                        Map<String, Object> model = view.getModel();
                        for (Map.Entry<String, Object> entry : model.entrySet()) {

                            // 遍历 ModelAndView 中的数据模型，把它们添加到request中
                            req.setAttribute(entry.getKey(), entry.getValue());
                        }

                        req.getRequestDispatcher(ConfigHelper.getAppJspPath() + dispatchPath).forward(req, resp);
                    }
                }
            } else if (result instanceof ResponseData) {

                // 如果是返回数据则直接把数据写到 Response 中
                ResponseData responseData = (ResponseData) result;
                Object model = responseData.getResponseData();

                if (null != model) {
                    // 暂时只处理返回数据是 JSON 的情况, 且采用 UTF-8 编码
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    PrintWriter writer = resp.getWriter();
                    String jsonString = JsonUtil.toJson(model);

                    writer.write(jsonString);
                    writer.flush();
                    writer.close();
                }
            }
        }

    }
}
