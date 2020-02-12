import cn.bestsign.openapi.service.IOpenapiCasedetailService;
import cn.bestsign.utils.bestsign.ProUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import framework.ApiObjectMethodModel;
import framework.ApiObjectModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;

@ContextConfiguration(locations = {"classpath:mybatis/*.xml","classpath:mapper/openapi/*.xml","classpath:application-context.xml"})
public class DemoTest extends AbstractTestNGSpringContextTests{


    private static Logger logger = LoggerFactory.getLogger(DemoTest.class);

    @Autowired
    IOpenapiCasedetailService openapiCasedetailService;

    @Test
    public void test() {

        System.err.println(openapiCasedetailService.selectOpenapiCaseListByInterfaceId(1L));
        System.out.println(openapiCasedetailService.selectOpenapiCasedetailById(2L));
        logger.info("demo test");
    }



    @Test
    public void test3(){

        System.err.println(ProUtils.getValue("host", "/developer.properties"));
    }


    @Test
    public void test2() throws FileNotFoundException {

        String path = "/Users/dai/dzx/work/huogewozi/SaasApi/src/main/resources/service/contract/api/ContractApi.yaml";

        InputStream inputStream = new FileInputStream(new File(path));


        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        ApiObjectModel model = new ApiObjectModel();
        try {
            model = mapper.readValue(inputStream, ApiObjectModel.class);
//                InputStream in = ClassLoader.class.getResourceAsStream(path);
//                model = mapper.readValue(in,ApiObjectModel.class);
            HashMap<String, ApiObjectMethodModel> methods = model.getMethods();

            Iterator<String> it = methods.keySet().iterator();

            while(it.hasNext()){

                String name = it.next();


                ApiObjectMethodModel apiObjectMethodModel = methods.get(name);

                System.err.println(apiObjectMethodModel.toString());
            }
            System.err.println(model);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}