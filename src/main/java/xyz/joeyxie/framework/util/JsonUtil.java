package xyz.joeyxie.framework.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * JSON 处理工具类, 采用Jackson实现
 * Created by joey on 2016/1/17.
 */
public class JsonUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 将 POJO 转为 JSON
     */
    public static <T> String toJson(T obj) {
        String jsonString;
        try {
            jsonString = OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LogUtil.error("convert POJO to JSON failed", e);
            throw new RuntimeException(e);
        }

        return jsonString;
    }

    /**
     * 将 JSON 转为 POJO
     */
    public static <T> T fromJson(String jsonString, Class<T> pojoType) {
        T pojo;
        try {
            pojo = OBJECT_MAPPER.readValue(jsonString, pojoType);
        } catch (IOException e) {
            LogUtil.error("convert JSON to POJO failed", e);
            throw new RuntimeException(e);
        }

        return pojo;
    }
}
