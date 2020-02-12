import cn.bestsign.dataprovider.CaseUtilsInit;
import cn.bestsign.openapi.domain.OpenapiInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@ContextConfiguration(locations = {"classpath:mybatis/*.xml", "classpath:mapper/openapi/*.xml", "classpath:application-context.xml"})
public class DemoTest2 extends AbstractTestNGSpringContextTests {


    private static Logger logger = LoggerFactory.getLogger(DemoTest2.class);


    @Test(dataProvider = "getSaasApiCaseParam", dataProviderClass = cn.bestsign.dataprovider.DataproviderClass.class)
    public void regTest(String caseName, String resquestBody, String expectOutput, String caseId) throws IOException, InterruptedException {

        System.err.println(caseName);

    }

    @Test
    public void test2(){

        String interfaceName = "/user/reg/";

        OpenapiInterface openapiInterface = CaseUtilsInit.getOpenapiInterfaceService().selectOpenapiInterfaceByName(interfaceName);

        System.err.println(openapiInterface);

    }


    @Test
    public void test3() {



    }

    List<Item> items(){
        return Arrays.asList(
                new Item("Item 1", "$19.99", Arrays.asList(new Feature("New!"), new Feature("Awesome!"))),
                new Item("Item 2", "$29.99", Arrays.asList(new Feature("Old."), new Feature("Ugly.")))
        );
    }

    static class Item {
        Item(String name, String price, List<Feature> features) {
            this.name = name;
            this.price = price;
            this.features = features;
        }
        String name, price;
        List<Feature> features;
    }

    static class Feature {
        Feature(String description) {
            this.description = description;
        }
        String description;
    }

}