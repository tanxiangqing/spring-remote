package org.storevm.framework.remote.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Jack
 * @version 1.0.0
 */
@Slf4j
public class JsonUtils {
    /**
     * 将对象序列化为JSON格式的字符串
     *
     * @param o JavaBean对象
     * @return JSON格式的字符串
     */
    public static String toJSONString(Object o) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // 在反序列化时忽略在 json 中存在但 Java 对象不存在的属性
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            // 在序列化时日期格式默认为 yyyy-MM-dd'T'HH:mm:ss.SSSZ
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            // 在序列化时忽略值为 null 的属性
            // mapper.setSerializationInclusion(Include.NON_NULL);
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException ex) {
            log.error("to parse Json occurred exception[JsonUtils.toJSONString]", ex);
        }
        return null;
    }

    /**
     * 将JSON格式的字符串反序列化成指定类型的Javabean对象
     *
     * @param text JSON格式的字符串
     * @return Javabean对象
     */
    public static <T> T parse(String text, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // 在反序列化时忽略在 json 中存在但 Java 对象不存在的属性
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(text, clazz);
        } catch (IOException ex) {
            log.error("to parse Json occurred exception[JsonUtils.parse]", ex);
        }
        return null;
    }

    /**
     * 将JSON格式的字符串反序列化成指定类型的Javabean对象集合
     *
     * @param text  JSON字符串，例如："[{"id":"1"},{"id":"2"}]"
     * @param clazz 集合中元素的类型
     * @return 对象集合
     */
    public static <T> List<T> parseToList(String text, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        try {

            JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, clazz);
            // 在反序列化时忽略在 json 中存在但 Java 对象不存在的属性
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(text, javaType);
        } catch (IOException ex) {
            log.error("to parse Json occurred exception[JsonUtils.parseToList]", ex);
        }
        return null;
    }

    public static <T> List<T> parseToList(InputStream src, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        try {

            JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, clazz);
            // 在反序列化时忽略在 json 中存在但 Java 对象不存在的属性
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(src, javaType);
        } catch (IOException ex) {
            log.error("to parse Json occurred exception[JsonUtils.parseToList]", ex);
        }
        return null;
    }
}
