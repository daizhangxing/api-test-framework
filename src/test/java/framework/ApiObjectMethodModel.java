package framework;

import cn.bestsign.utils.bestsign.CalSign;
import cn.bestsign.utils.bestsign.ProUtils;
import cn.bestsign.utils.bestsign.Token;
import cn.bestsign.utils.comon.StringUtils;
import com.alibaba.fastjson.JSONObject;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.MapUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.useRelaxedHTTPSValidation;

@Getter
@Setter
public class ApiObjectMethodModel {

    //外部传入的map
    public HashMap<String, Object> params;
    //请求map，放在url中的参数
    public HashMap<String, Object> query;
    //请求头
    public HashMap<String, Object> header;
    //请求的消息体，post需要使用
    private HashMap<String, Object> postBody;
    private String postBodyRaw;
    //方法类型
    private String method = "";

    //请求的urlPath
    private String requestUrlPath = "";

    //算sign的path,因为get请求和post不同
    private String signUrlPath = "";
    //真正的请求的urlPath，例如/api/contracts/{contractId}，
    // 如果把模板中的{contractId}替换成真的值，那么第二个用例就无法替换，
    //所以需要一个真实的requestPath来表示
    private String realRequestPath = "";

    //get方法真实的请求参数化
    private HashMap<String, Object> realQuery;


    //请求地址
    public static String host = ProUtils.getDefaultPathValue("host");
    public static String clientId = ProUtils.getDefaultPathValue("clientId");
    public static String clientSecret = ProUtils.getDefaultPathValue("clientSecret");
    public static String privateKey = ProUtils.getDefaultPathValue("privateKey");


    public Response run() {
        RequestSpecification request = given();
        useRelaxedHTTPSValidation();
        request.header("Content-type", "application/json; charset=UTF-8");

        if (query != null) {
            realQuery = new HashMap<>();
            //不能这么赋值，两者会指向同一个内存地址，改变realQuery，
            // 导致query也会改变，那么模板就会改变，导致之后的用例无法时候用
//            realQuery = query;
            query.entrySet().forEach(entry -> {
                //赋值给get请求真实的参数 realQuery
                realQuery.put(entry.getKey(), repalce(entry.getValue().toString()));
            });
        }

        if (header != null) {
            query.entrySet().forEach(entry -> {
                request.header(entry.getKey(), repalce(entry.getValue().toString()));
            });
        }

        if (postBody != null) {

            JSONObject jsonPostBody = new JSONObject();
            for(Map.Entry<String,Object> entry: postBody.entrySet()){

                jsonPostBody.put(entry.getKey(),entry.getValue());
            }

            postBodyRaw = repalce(jsonPostBody.toJSONString());
        }

        if (postBodyRaw != null) {
            //一种是postBody，一种是模板传进来
            System.out.println("~~~~~~~~~~~~~~~~~"+postBodyRaw);
            request.body(postBodyRaw);
        }

        //将url设置
        if ("get".equalsIgnoreCase(method)) {

            Map<String, String> stringStringMap = setUrlPath(requestUrlPath);

            realRequestPath = stringStringMap.get("realRequestPath");
            signUrlPath = stringStringMap.get("signUrlPath");
        } else {
            signUrlPath = requestUrlPath;
            realRequestPath = requestUrlPath;

        }

        if (realQuery != null) {
            realQuery.entrySet().forEach(entry -> {
                System.out.println("entry.getValue()=" + entry.getValue());
                request.queryParam(entry.getKey(), entry.getValue());
            });
        }



        String timeStamp = CalSign.getTimeStamp();
        String token = Token.getTokenClass().getToken(host, clientId, clientSecret);


        String requestBody = null;
        if (!MapUtils.isEmpty(postBody)) {

            JSONObject jsonObject = new JSONObject();
            for (Map.Entry<String, Object> entry : postBody.entrySet()) {

                jsonObject.put(entry.getKey(), entry.getValue());
            }

            requestBody = jsonObject.toJSONString();
        }

        String sign = CalSign.calcSign(timeStamp, requestBody, signUrlPath, clientId, privateKey);

        JSONObject headers = RequestUtils.setPostHeaders(clientId, timeStamp, sign, token);
        request.headers(headers);

        System.out.println("Request URI=" + host + realRequestPath);
        return request.urlEncodingEnabled(false)
                .when().log().all().request(method, host + realRequestPath)
                .then().log().all().extract().response();
    }


    //如果请求头，请求体里面有包含${contractId}，那么params中的key->${key}匹配之前提到的参数，那么就替换
    public String repalce(String raw) {
        for (Map.Entry<String, Object> kv : params.entrySet()) {
            String matcher = "${" + kv.getKey() + "}";
            if (raw.contains(matcher)) {
                System.out.println(kv);
                raw = raw.replace(matcher, kv.getValue().toString());

            }
        }
        return raw;
    }


    /**
     * get请求时/api/contracts/{contractId} 输入，然后将params中和get请求的变量匹配时，就进行替换，
     * 但是不能更改requestPath,不然下个用例进来就无法替换换
     * 然后将匹配的变量放入List并返回
     *
     * @param raw
     * @return
     */
    public Map<String, Object> getRepalceMap(String raw) {

        HashMap<String, Object> map = new HashMap<>();

        List<String> list = new LinkedList<>();

        for (Map.Entry<String, Object> kv : params.entrySet()) {
            String matcher = "{" + kv.getKey() + "}";
            if (raw.contains(matcher)) {
                //将urlPath中匹配的请求参数中的{xxx}字符串 放入list
                list.add(kv.getKey());

                //将替换后的String字符串换放入map中
                String urlPath = raw.replace(matcher, kv.getValue().toString());

                map.put("urlPath", urlPath);
            }
        }
        map.put("repeat", list);
        return map;
    }

    //处理get请求的真实请求url和算sign的Url
    protected Map<String, String> setUrlPath(String path) {

        HashMap<String, String> map = new HashMap<>();

        if (StringUtils.isEmpty(path)) {

            throw new RuntimeException("请求urlPath为空");
        }

        Map<String, Object> replaceMap = getRepalceMap(path);
        //将urlPath中{contractId}之类的替换成真实值,不能用requestUrlPath，不然下一个用例就无法替换成真实值！！！
        String realRequestPath = (String) replaceMap.get("urlPath");

        replaceMap.remove("urlPath");

        //拿到被替换换真实值的key，比如contractId
        List<String> repeatKeyList = (List<String>) replaceMap.get("repeat");

        //将这些key从query中删除
        for (int i = 0; i < repeatKeyList.size(); i++) {
            String repeat = repeatKeyList.get(i);

            if (realQuery.containsKey(repeat)) {

                realQuery.remove(repeat);
            }
        }

        //get方法算sign将参数也放入url中，所以要用signUrlPath，真实的请求url则为realRequestPath
        String signUrlPath = RequestUtils.getUrlPath(realRequestPath, realQuery);

        map.put("realRequestPath", realRequestPath);
        map.put("signUrlPath", signUrlPath);

//        System.err.println(requestUrlPath);

        return map;
    }

    public Response run(HashMap<String, Object> params) {
        this.params = params;
        return run();
    }

    public Response run(HashMap<String, Object> params,String jsonStringRequest) {
        this.params = params;
        this.postBodyRaw = jsonStringRequest;
        return run();
    }


    @Override
    public String toString() {
        return "ApiObjectMethodModel{" +
                "params=" + params +
                ", query=" + query +
                ", header=" + header +
                ", postBody=" + postBody +
                ", postBodyRaw='" + postBodyRaw + '\'' +
                ", method='" + method + '\'' +
                ", requestUrlPath='" + requestUrlPath + '\'' +
                '}';
    }


}
