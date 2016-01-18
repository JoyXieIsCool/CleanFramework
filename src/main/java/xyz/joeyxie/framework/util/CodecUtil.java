package xyz.joeyxie.framework.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 编码与解码操作工具类
 * Created by joey on 2016/1/17.
 */
public class CodecUtil {

    /**
     * 将 URL 进行编码, 指定编码格式
     */
    public static String encodeURL(String source, String charsetName) {
        String target;
        try {
            target = URLEncoder.encode(source, charsetName);
        } catch (UnsupportedEncodingException e) {
            LogUtil.error("encode URL failed", e);
            throw new RuntimeException(e);
        }

        return target;
    }

    /**
     * 将 URL 进行编码，默认采用 UTF-8 格式
     */
    public static String encodeURL(String source) {
        return encodeURL(source, "UTF-8");
    }

    /**
     * 将 UTL 进行解码，指定编码格式
     */
    public static String decodeURL(String source, String charsetName) {
        String target;
        try {
            target = URLDecoder.decode(source, charsetName);
        } catch (UnsupportedEncodingException e) {
            LogUtil.error("decode URL failed", e);
            throw new RuntimeException(e);
        }

        return target;
    }

    /**
     * 将 URL 进行解码，默认采用 UTF-8 格式
     */
    public static String decodeURL(String source) {
        return decodeURL(source, "UTF-8");
    }
}
