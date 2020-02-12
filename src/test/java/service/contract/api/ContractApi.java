package service.contract.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import framework.RequestUtils;
import io.restassured.response.Response;
import service.baseapi.BaseApi;

import java.io.FileNotFoundException;
import java.util.HashMap;

/**
 * Created by dai on 2020/1/20.
 */
public class ContractApi extends BaseApi {

    public Response getContract(String contractId, String account) throws FileNotFoundException {
        HashMap<String, Object> params = new HashMap<>();
        params.put("contractId", contractId);
        params.put("account", account);
        //设置参数
        setParams(params);

        return parseSteps();

    }


    public Response getContratList(String contractId, String account) throws FileNotFoundException {
        HashMap<String, Object> params = new HashMap<>();
        params.put("contractId", contractId);
        params.put("account", account);
        params.put("x", 4);
        //设置参数
        setParams(params);

        return parseSteps();

    }

    public Response getContractIntegration(String stringRequest){

        HashMap<String, Object> params = RequestUtils.jsonToMap(stringRequest);

        setParams(params);

        return parseSteps();

    }

//    public Response getContractIntegration(String stringRequest,String jsonPath){
//
//        HashMap<String, Object> params = RequestUtils.jsonToMap(stringRequest);
//
//        String template = RequestUtils.template(jsonPath, params);
//
//        System.out.println("template="+template);
//
//        setParams(params);
////        setPostBodyRaw(template);
//
//
//        return parseSteps();
//
//    }

    //get方法
    public Response demoGet(String stringRequest){

        HashMap<String, Object> params = RequestUtils.jsonToMap(stringRequest);

        setParams(params);

        return parseSteps();

    }

    //post方法
    public Response demoPost(String stringRequest,String jsonPath){

        HashMap<String, Object> params = RequestUtils.jsonToMap(stringRequest);

        String template = RequestUtils.template(jsonPath, params);

        setPostBodyRaw(template);

        return parseSteps();

    }








}