package xyz.joeyxie.framework.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 用于流操作的工具类
 * Created by joey on 2016/1/17.
 */
public class StreamUtil {

    /**
     * 获取输入流中的字符串
     */
    public static String getString(InputStream in) {
        return getString(in, null);
    }

    public static String getString(InputStream in, String charsetName) {
        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader reader;

            if (StringUtil.isEmpty(charsetName)) {
                // 如果没有指定编码格式则采用默认方式读取流
                reader = new BufferedReader(new InputStreamReader(in));
            } else {
                // 如果指定了编码格式则使用指定的编码来读取流
                reader = new BufferedReader(new InputStreamReader(in, charsetName));
            }

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (Exception e) {
            LogUtil.error("get string failed", e);
            throw new RuntimeException(e);
        }

        return builder.toString();
    }
}
