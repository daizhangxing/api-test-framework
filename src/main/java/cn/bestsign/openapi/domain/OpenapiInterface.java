package cn.bestsign.openapi.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 接口对象 openapi_interface
 * 
 * @author bestsign
 * @date 2019-10-10
 */

@Getter
@Setter
@ToString
public class OpenapiInterface{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long interfaceId;

    /** 接口名称 */

    private String interfaceName;

    /** 接口调用方式 */

    private String method;

    /** 接口描述 */

    private String interfaceDescription;

    /** 项目id主键 */

    private Long projectId;



    private String projectName;

    private int caseNum;



}
