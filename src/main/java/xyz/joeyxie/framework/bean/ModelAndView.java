package xyz.joeyxie.framework.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于Action方法返回的视图模型对象，保存Action方法需要跳转的页面以及页面需要的数据模型
 * Created by joey on 2016/1/17.
 */
public class ModelAndView {

    // 视图路径
    private String viewPath;

    // 模型数据
    private Map<String, Object> model;

    public ModelAndView(String viewPath) {
        this.viewPath = viewPath;
        model = new HashMap<String, Object>();
    }


    /**
     * 添加键值对到数据模型中，并返回当前对象
     * @return 当前 ModelAndView 对象
     */
    public ModelAndView addModel(String key, Object value) {
        model.put(key, value);
        return this;
    }

    public String getViewPath() {
        return viewPath;
    }

    public Map<String, Object> getModel() {
        return model;
    }
}
