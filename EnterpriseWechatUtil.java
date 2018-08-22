package com.mobisummer.emp.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EnterpriseWechatUtil {

  private static final String TOKEN_API = "https://qyapi.weixin.qq.com/cgi-bin/gettoken";

  private static final String SEND_MSG_API =
    "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s";

  public static String ACCESS_TOKEN = null;

  public static Date EXPIRE_DATE = null;

  public static final String UTF_8 = "utf-8";

  @SuppressWarnings("unchecked")
  public synchronized static String getToken(String corpid, String secret) {
    //		if (ACCESS_TOKEN != null) {
    //			if (EXPIRE_DATE != null && EXPIRE_DATE.after(new Date())) {
    //				return ACCESS_TOKEN;
    //			}
    //		}
    StringBuilder url = new StringBuilder(256);
    url.append(TOKEN_API);
    url.append("?corpid=").append(corpid);
    url.append("&");
    url.append("corpsecret=").append(secret);
    HttpResult result = HttpUtil.get(url.toString());
    String json = result.getContent();
    Map<String, Object> map = JsonUtil.toBean(json, Map.class);
    //		long expireIn = Long.parseLong(map.get("expires_in") + "");
    ACCESS_TOKEN = map.get("access_token") + "";
    //		Date cur = new Date();
    //		EXPIRE_DATE = new Date(cur.getTime() + (expireIn - 10 * 60) * 1000);
    return ACCESS_TOKEN;
  }

  public static void sendMsg(String corpid,
                             String secret,
                             String agentId,
                             String userId,
                             String content) throws UnsupportedEncodingException {
    String token = getToken(corpid, secret);
    System.out.println(token);
    String url = String.format(SEND_MSG_API, token);
    Map<String, Object> data = new HashMap<String, Object>();
    data.put("touser", userId);    //企业号中的用户帐号，在zabbix用户Media中配置，如果配置不正常，将按部门发送。
    data.put("msgtype", "text");  //消息类型
    data.put("agentid", agentId);  //企业号中的应用id
    Map<String, String> contentMap = new HashMap<>();
    String msg = "";
    if (content.length() < 2048) {
      msg = content.toString();
    } else {
      msg = content.toString().substring(0, 2040);
    }
    contentMap.put("content", msg);
    data.put("text", contentMap);
    data.put("safe", "0");

    Map<String, String> headers = new HashMap<String, String>();
    headers.put("Content-type", "application/json;charset=UTF-8");
    HttpUtil.post(url, data, headers);
  }

  //	public static void main(String[] args) throws UnsupportedEncodingException {
  //		String secret = "pvcLJ7XcsGp6CGrqq3zUXFE8svEmV__kGlwQBVN5Q_Q";
  //		String corpid = "ww470155697cae052e";
  //		String agentId = "1000018";
  //		String userId = "Fire|Siling|CuiWeiHua";
  //		String content = "再测试下";
  //		sendMsg(corpid, secret, agentId, userId, content);
  //	}
}

