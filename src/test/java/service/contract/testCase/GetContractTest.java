package service.contract.testCase;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import service.contract.api.ContractApi;

import java.io.FileNotFoundException;

/**
 * Created by dai on 2020/1/20.
 */
public class GetContractTest {


    @Test
    public void test() throws FileNotFoundException {

        String contractId = "2217302723118759939";
        String account = "sso_dev_1%40b.cn";

        ContractApi contractApi = new ContractApi();
        Response contract = contractApi.getContract(contractId, account);

        String result = contract.toString();

    }


    @Test
    public void test2() throws FileNotFoundException {

        String contractId = "2217302723118759939";
        String account = "sso_dev_1%40b.cn";

        ContractApi contractApi = new ContractApi();

        Response contract = contractApi.getContratList(contractId, account);
        System.err.println("!!!!!!getContratList!!!!!!!"+contract.toString());
        Response contract1 = contractApi.getContract("11111111111", "2222222222222");
        System.err.println("~~~~~~~~~~~~~~getContratList!!!!!!!"+contract.toString());
        Response contrac2 = contractApi.getContratList("3333333333", "4444444444444");

    }

    @Test
    public void test3() throws FileNotFoundException {

        String contractId = "2217302723118759939";
//        String contractId = "~";
        String account = "123123";
//        String account = "sso_dev_1%40b.cn";
        Integer x = 4;

        ContractApi contractApi = new ContractApi();

        Response contract1 = contractApi.getContratList(contractId, account);


    }
}
