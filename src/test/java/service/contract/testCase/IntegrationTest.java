package service.contract.testCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import service.baseapi.BaseTest;
import service.contract.api.ContractApi;

import java.io.IOException;

import static com.sun.xml.internal.ws.dump.LoggingDumpTube.Position.Before;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

/**
 * Created by dai on 2020/2/3.
 */
public class IntegrationTest extends BaseTest {

    @BeforeTest
    public void test(){



    }

    @Test(dataProvider = "getSaasApiCaseParam", dataProviderClass = cn.bestsign.dataprovider.DataproviderClass.class)
    public void demo(String caseName, String resquestBody, String expectOutput, String caseId) throws IOException, InterruptedException {

        ContractApi contractApi = new ContractApi();

        String testPath = "/" + this.getClass().getCanonicalName().replace('.', '/') + ".params";
        System.err.println("!!!"+testPath);

//        Response contract = contractApi.getContractIntegration(resquestBody,testPath);
        Response contract = contractApi.getContractIntegration(resquestBody);

       contract.print();

        contract.then().body("message",equalTo("请求成功")).
                body("data.receivers.receiverId",hasItems("2217302724351885315","2217302724377051141"));

//        contract.then().assertThat().body("asdasd",hasItems())
////        contract.then().body("data.receivers.receiverId",hasItems("2217302724351885315","2217302724377051141"));
//
//        ResponseBody body = contract.body();
//        JsonPath jsonPath = body.jsonPath();
//        Object message = jsonPath.get("message");
//        System.err.println(message);

        JSONObject jsonObject = JSON.parseObject(expectOutput);

        for (JSONObject.Entry<String,Object> entry:jsonObject.entrySet()){

            System.out.println("~~~~~");
            contract.then().assertThat().body(entry.getKey(),equalTo(entry.getValue()));
            contract.then().assertThat().body("asdasd", Matchers.emptyOrNullString());
        }


    }




}
