package service.baseapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import framework.ApiObjectModel;
import io.restassured.response.Response;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by dai on 2020/1/20.
 */

public class BaseApi {

    public static ApiObjectModel  model = new ApiObjectModel();
    private HashMap<String, Object> params;
    private String postBodyRaw;

    public Response parseSteps() {
        //获得调用parseSteps方法的方法
        String method = Thread.currentThread().getStackTrace()[2].getMethodName();
        System.out.println(method);
//当有多个测试api时候这个方法会有问题，无法加载其他yaml文件
//        if(model.getMethods().entrySet().isEmpty()) {
        //改成当没有这个方法模型时，需要去加载对应的yaml文件
        if(!model.getMethods().keySet().contains(method)) {

            System.out.println("po model first load");
            String path = "/" + this.getClass().getCanonicalName().replace('.', '/') + ".yaml";

            System.out.println("path="+path);

            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            try {
                model = mapper.readValue(BaseApi.class.getResourceAsStream(path), ApiObjectModel.class);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return (Response) model.run(method, params,postBodyRaw);
//        return (Response) model.run(method, params);


    }

    public void setParams(HashMap<String, Object> data){
        params=data;
    }

    public void setPostBodyRaw(String request){
        this.postBodyRaw=request;
    }




}
