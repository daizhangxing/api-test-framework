package cn.bestsign.dataprovider;

import cn.bestsign.openapi.service.IOpenapiCasedetailService;
import cn.bestsign.openapi.service.IOpenapiInterfaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by dai on 2020/1/19.
 */
@Component
public class CaseUtilsInit {

    @Autowired
    private IOpenapiCasedetailService iOpenapiCasedetailService;

    @Autowired
    private IOpenapiInterfaceService iOpenapiInterfaceService;

    private   static IOpenapiCasedetailService openapiCasedetailService;

    private  static  IOpenapiInterfaceService openapiInterfaceService;


    @PostConstruct
    public void init(){
        openapiCasedetailService = iOpenapiCasedetailService;

        openapiInterfaceService = iOpenapiInterfaceService;
    }


    public static IOpenapiCasedetailService getOpenapiCasedetailService(){

        return openapiCasedetailService;
    }

    public static IOpenapiInterfaceService getOpenapiInterfaceService(){

        return openapiInterfaceService;
    }
}
