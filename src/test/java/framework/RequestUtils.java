package framework;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.apache.commons.collections.MapUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.*;

/**
 * Created by dai on 2020/1/20.
 */
public class RequestUtils {

    protected static String getUrlPath(String urlPath, HashMap<String, Object> map) {

        StringBuilder urlParams = new StringBuilder(urlPath);
        if (MapUtils.isNotEmpty(map)) {
            urlParams.append("?");
            for (Map.Entry<String, Object> me : map.entrySet()) {
                urlParams.append(me.getKey());
                urlParams.append("=");
                urlParams.append(me.getValue() == null ? "" : me.getValue().toString());
                urlParams.append("&");
            }
            urlParams.deleteCharAt(urlParams.length() - 1);
        }

        return urlParams.toString();
    }


    /**
     * 设置头信息方法
     */
    public static JSONObject setPostHeaders(String clientId, String timeStamp, String sign, String token) {

        JSONObject headers = new JSONObject();

        headers.put("bestsign-client-id", clientId);
        headers.put("bestsign-sign-timestamp", timeStamp);
        headers.put("bestsign-signature-type", "RSA256");
        headers.put("bestsign-signature", sign);
        headers.put("Authorization", token);

        return headers;

    }

    public static String getMapToString(Map<String, Object> map) {
        Set<String> keySet = map.keySet();
        //将set集合转换为数组
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        //给数组排序(升序)
        Arrays.sort(keyArray);
        //因为String拼接效率会很低的，所以转用StringBuilder
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keyArray.length; i++) {
            // 参数值为空，则不参与签名 这个方法trim()是去空格
            if ((String.valueOf(map.get(keyArray[i]))).trim().length() > 0) {
                sb.append(keyArray[i]).append(":").append(String.valueOf(map.get(keyArray[i])).trim());
            }
            if (i != keyArray.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();


    }

    /**
     * String转map
     *
     * @param str
     * @return
     */
    public static Map<String, Object> getStringToMap(String str) {
        //根据逗号截取字符串数组
        String[] str1 = str.split(",");
        //创建Map对象
        Map<String, Object> map = new HashMap<>();
        //循环加入map集合
        for (int i = 0; i < str1.length; i++) {
            //根据":"截取字符串数组
            String[] str2 = str1[i].split(":");
            //str2[0]为KEY,str2[1]为值
            map.put(str2[0], str2[1]);
        }
        return map;
    }


    /**
     * mustache模板技术
     *
     * @param templatePath
     * @param data
     * @return
     */
    public static String template(String templatePath, HashMap<String, Object> data) {
        Writer writer = new StringWriter();
        MustacheFactory mf = new DefaultMustacheFactory();
        Class<RequestUtils> requestUtilsClass = RequestUtils.class;

        URL resource = requestUtilsClass.getResource(templatePath);
        Mustache mustache = mf.compile(requestUtilsClass.getResource(templatePath).getPath());
        mustache.execute(writer, data);
        try {
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }


    public static HashMap<String, Object> jsonToMap(String jsonString) {

        JSONObject jsonObject  = JSON.parseObject(jsonString);
        HashMap<String,Object> hashMap = new HashMap<>();
        Set<String> set = jsonObject.keySet();
        for (String key : set) {

            String value = jsonObject.getString(key);
            hashMap.put(key,value);
        }

        return hashMap;
    }
}