package framework;

import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class ApiObjectModel {

    public   HashMap<String, ApiObjectMethodModel> methods = new HashMap<>();

    //获取对应方法的方法模型
    public ApiObjectMethodModel getMethod(String method) {
        return methods.get(method);
    }

    public Response run(String method) {
        return getMethod(method).run();
    }

    //得到对应方法的模型，然后调用该模型的run方法，即ApiObjectMethodModel中的run方法
    public Response run(String method, HashMap<String, Object> params) {

        return getMethod(method).run(params);
    }

    public Response run(String method, HashMap<String, Object> params,String request) {

        return getMethod(method).run(params,request);
    }


}
