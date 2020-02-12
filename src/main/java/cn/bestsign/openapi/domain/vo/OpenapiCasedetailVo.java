package cn.bestsign.openapi.domain.vo;


import cn.bestsign.openapi.domain.OpenapiCasedetail;
import lombok.Getter;
import lombok.Setter;


/**
 * Created by dai on 2019/12/5.
 */
@Getter
@Setter
public class OpenapiCasedetailVo extends OpenapiCasedetail {

    private String methodName;
    private String method;
    private String developerId;
    private String host;
    private String pem;
    private String distHost;

    private String message;



}
