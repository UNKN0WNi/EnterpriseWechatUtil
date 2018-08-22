package com.mobisummer.emp.util;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * json工具类
 *
 * @author zero
 */
public final class JsonUtil {

  private static JsonFactory jsonFactory = new JsonFactory();

  private static ObjectMapper mapper = null;

  private static TypeFactory t = null;

  static {
    jsonFactory.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    jsonFactory.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    mapper = new ObjectMapper(jsonFactory);
  }

  /**
   * @Description: 返回默认的类型工厂
   * @author zero
   */
  public static TypeFactory getTypeFactory() {
    if (t == null) {
      t = TypeFactory.defaultInstance();
    }
    return t;
  }

  /**
   * 获取jackson json lib的ObjectMapper对象
   *
   * @author zero
   */
  public static ObjectMapper getMapper() {
    return mapper;
  }

  /**
   * 获取jackson json lib的JsonFactory对象
   *
   * @author zero
   */
  public static JsonFactory getJsonFactory() {
    return jsonFactory;
  }

  /**
   * 将json转成java bean
   *
   * @author zero
   */
  public static <T> T toBean(String json, Class<T> clazz) {
    T rtv = null;
    try {
      rtv = mapper.readValue(json, clazz);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new IllegalArgumentException("convert json to java bean error", ex);
    }
    return rtv;
  }


  /**
   * @Description: 将json转化为list集合
   * @author zero
   */
  public static <T> List<T> toListBean(String json, Class<T> clazz) {
    List<T> list = null;
    try {
      list =
        mapper.readValue(json, getTypeFactory().constructCollectionType(ArrayList.class, clazz));
    } catch (Exception ex) {
      throw new IllegalArgumentException("convert json to java list bean error", ex);
    }
    return list;
  }

  /**
   * 将java bean转成json
   *
   * @author zero
   */
  public static String toJson(Object bean) {
    String rtv = null;
    try {
      rtv = mapper.writeValueAsString(bean);
    } catch (Exception ex) {
      throw new IllegalArgumentException("convert java bean to json error", ex);
    }
    return rtv;
  }
}