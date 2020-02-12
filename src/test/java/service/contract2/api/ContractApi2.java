package service.contract2.api;

import framework.RequestUtils;
import io.restassured.response.Response;
import service.baseapi.BaseApi;

import java.util.HashMap;

/**
 * Created by dai on 2020/2/5.
 */

public class ContractApi2 extends BaseApi{

    public Response getContractIntegration(String stringRequest){

        HashMap<String, Object> params = RequestUtils.jsonToMap(stringRequest);

        setParams(params);

        return parseSteps();

    }
}
