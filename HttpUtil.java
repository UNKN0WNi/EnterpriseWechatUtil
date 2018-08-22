package com.mobisummer.emp.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpUtil {

  public static final String DEFAULT_CHARSET = "utf-8";

  public static HttpResult get(String url, Map<String, String> headers, String charset) {
    HttpResult result = new HttpResult();
    StringBuilder content = new StringBuilder(1024);
    BufferedReader in = null;
    try {
      URL realUrl = new URL(url);
      HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
      if (headers != null) {
        for (String header : headers.keySet()) {
          connection.setRequestProperty(header, headers.get(header));
        }
      }
      connection.connect();
      int responseCode = connection.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) {
        in = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));
        String line;
        while ((line = in.readLine()) != null) {
          content.append(line);
        }
      }

      Map<String, String> respHeaders = new HashMap<String, String>();
      if (connection.getHeaderFields() != null) {
        for (String key : connection.getHeaderFields().keySet()) {
          String value = connection.getHeaderField(key);
          respHeaders.put(key, value);
        }
      }
      System.out.println("status:" + responseCode);
      System.out.println("result:" + content);
      result.setContent(content.toString());
      result.setStatus(responseCode);
      result.setHeaders(respHeaders);

    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      try {
        if (in != null) {
          in.close();
        }
      } catch (Exception e2) {
      }
    }
    return result;

  }

  public static HttpResult post(String url,
                                Object body,
                                Map<String, String> headers,
                                String charset) {
    DataOutputStream out = null;
    BufferedReader in = null;
    HttpResult result = new HttpResult();
    StringBuilder content = new StringBuilder(1024);
    try {
      System.out.println("------post url------" + url);
      URL realUrl = new URL(url);
      HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
      if (headers != null) {
        for (String header : headers.keySet()) {
          conn.setRequestProperty(header, headers.get(header));
          System.out.println(
            "------post header------[key=" + header + ", value=" + headers.get(header) + "]");
        }
      }
      conn.setRequestMethod("POST");
      conn.setDoOutput(true);
      conn.setDoInput(true);
      if (body != null) {
        String json = JsonUtil.toJson(body);
        System.out.println("------post body------" + json);
        out = new DataOutputStream(conn.getOutputStream());

        out.write(json.getBytes());
        out.flush();
      }

      int responseCode = conn.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) {
        in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
        String line;
        while ((line = in.readLine()) != null) {
          content.append(line);
        }
      }

      Map<String, String> respHeaders = new HashMap<String, String>();
      for (String key : conn.getHeaderFields().keySet()) {
        String value = conn.getHeaderField(key);
        respHeaders.put(key, value);
      }
      result.setContent(content.toString());
      result.setStatus(responseCode);
      System.out.println("result:" + responseCode);
      System.out.println("result:" + content);
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      try {
        if (out != null) {
          out.close();
        }
        if (in != null) {
          in.close();
        }
      } catch (IOException ex) {
      }
    }
    return result;
  }


  public static HttpResult get(String url, Map<String, String> headers) {
    return get(url, headers, DEFAULT_CHARSET);
  }

  public static HttpResult get(String url) {
    return get(url, null);
  }

  public static HttpResult post(String url, Object body, Map<String, String> headers) {
    return post(url, body, headers, DEFAULT_CHARSET);
  }

  public static HttpResult post(String url, Object body) {
    return post(url, body, null);
  }

  public static HttpResult post(String url) {
    return post(url, null);
  }
}  