package cn.bestsign.dataprovider;


import cn.bestsign.mapping.InterfaceName;
import cn.bestsign.openapi.domain.OpenapiCasedetail;
import cn.bestsign.openapi.domain.OpenapiInterface;
import org.testng.annotations.DataProvider;


import java.lang.reflect.Method;
import java.util.List;


public class DataproviderClass {

    @DataProvider(name = "getSaasApiCaseParam")
    public static Object[][] getCaseParam(Method method) {

        //获取测试方法的简易方法名
        String methodName = method.getName();

        //获取方法名
        String interfaceName = InterfaceName.getInterfaceName().getInterfaceNameMapping().get(methodName);

        //获取数据库中方法名对应的接口ID
        OpenapiInterface openapiInterface = CaseUtilsInit.getOpenapiInterfaceService().selectOpenapiInterfaceByName(interfaceName);

        //根据接口ID获取该接口的所有相关用例
        List<OpenapiCasedetail> openapiCasedetails = CaseUtilsInit.getOpenapiCasedetailService().selectOpenapiCaseListByInterfaceId(openapiInterface.getInterfaceId());


        String[][] params = new String[openapiCasedetails.size()][];
        for (int i = 0; i < openapiCasedetails.size(); i++) {

            OpenapiCasedetail casedetail = openapiCasedetails.get(i);

            String[] caseParams = new String[4];
            caseParams[0] = casedetail.getCaseName();
            caseParams[1] = casedetail.getCaseInput();
            caseParams[2] = casedetail.getExpectOutput();
            caseParams[3] = String.valueOf(casedetail.getCaseId());

            params[i] = caseParams;
        }

        return params;

    }

}
