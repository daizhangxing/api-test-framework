import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import framework.ApiObjectMethodModel;
import framework.RequestUtils;
import org.testng.annotations.Test;
import service.baseapi.BaseApi;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dai on 2020/1/22.
 */
public class DemoTest3 extends BaseApi {



    @Test
    public void test() {

        HashMap<String, Object> data = new HashMap<>();
        data.put("repo", true);
        data.put("name", "123");
        String templatePath = this.getClass().getResource("/service/userParams").getPath();
        System.out.println("1-" + this.getClass().getClassLoader().getResource("").getPath());//直接到test-class
        System.out.println("2-" + this.getClass().getResource("").getPath());//直接到包
        System.out.println("3-" + this.getClass().getResource("/service/userParams").getPath());//到classes
        System.out.println("4-" + this.getClass().getResource("/service/userParams").getPath());
//        System.out.println(this.getClass().getClassLoader().getResource("/service/userParams").getPath());//null

        Class<RequestUtils> utilsClass = RequestUtils.class;
        URL resource = utilsClass.getResource("/service/userParams");
        String path111 = resource.getPath();
        System.err.println(path111);

        System.out.println("++" + (this.getClass().getResource("").getPath() + this.getClass().getCanonicalName().replace('.', '/')));
        System.out.println("++" + ("/" + this.getClass().getCanonicalName().replace('.', '/')));
        String path = "/" + this.getClass().getCanonicalName();

        String testPath = "/" + this.getClass().getCanonicalName().replace('.', '/') + ".params";

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = 0; i < stackTrace.length; i++) {
            StackTraceElement stackTraceElement = stackTrace[i];
            System.err.println(stackTraceElement.getMethodName());

        }

        System.err.println(path);


        String body = RequestUtils.template(testPath, data);


        System.out.println(body);
    }


    @Test
    public void test34() {

        String s = "{\"contractId\":\"2217302723118759939\",\"account\":\"sso_dev_1%40b.cn\"}";

        RequestUtils.jsonToMap(s);

    }
}
