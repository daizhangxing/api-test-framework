package service.contract2.testcase;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import service.baseapi.BaseTest;
import service.contract.api.ContractApi;

import java.io.IOException;

/**
 * Created by dai on 2020/2/5.
 */
public class DemoTest  extends BaseTest{

    @Test(dataProvider = "getSaasApiCaseParam", dataProviderClass = cn.bestsign.dataprovider.DataproviderClass.class)
    public void demo1(String caseName, String resquestBody, String expectOutput, String caseId) throws IOException, InterruptedException {

        ContractApi contractApi = new ContractApi();

        String testPath = "/" + this.getClass().getCanonicalName().replace('.', '/') + ".params";
        System.err.println("!!!" + testPath);


//        Response contract = contractApi.getContractIntegration(resquestBody,testPath);
        Response contract = contractApi.getContractIntegration(resquestBody);
        contract.statusCode();
        contract.print();

    }


}
