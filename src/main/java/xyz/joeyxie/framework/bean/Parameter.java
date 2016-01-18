package xyz.joeyxie.framework.bean;

import xyz.joeyxie.framework.util.CastUtil;

import java.util.Map;

/**
 * 封装 HttpServletRequest 中的请求参数的类
 * Created by joey on 2016/1/17.
 */
public class Parameter {

    // 存储请求参数的map
    private Map<String, Object> parameterMap;

    public Parameter(Map<String, Object> parameterMap) {
        this.parameterMap = parameterMap;
    }

    /**
     * 根据参数名获取 long 型的参数值
     */
    public long getLongParameter(String parameterName) {
        return CastUtil.castLong(parameterMap.get(parameterName));
    }

    /**
     * 根据参数名获取 int 型的参数值
     */
    public int getIntParameter(String parameterName) {
        return CastUtil.castInt(parameterMap.get(parameterName));
    }

    /**
     * 根据参数名获取 double 型的参数值
     */
    public double getDoubleParameter(String parameterName) {
        return CastUtil.castDouble(parameterMap.get(parameterName));
    }

    /**
     * 根据参数名获取 boolean 型的参数值
     */
    public boolean getBooleanParameter(String parameterName) {
        return CastUtil.castBoolean(parameterMap.get(parameterName));
    }

    /**
     * 获取包含所有参数的map
     */
    public Map<String, Object> getParameterMap() {
        return parameterMap;
    }
}
