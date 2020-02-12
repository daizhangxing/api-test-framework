package cn.bestsign.openapi.domain;



import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 用例详情对象 openapi_casedetail
 * 
 * @author bestsign
 * @date 2019-10-10
 */
@Getter
@Setter
@ToString
public class OpenapiCasedetail {
    private static final long serialVersionUID = 1L;

    /** 用例Id,主键 */
    private Long caseId;

    /** 用例名称 */

    private String caseName;

    /** 用例输入参数 */

    private String caseInput;

    /** 预期结果 */

    private String expectOutput;

    /** 实际结果 */

    private String actualOutput;

    /** 接口id */

    private Long interfactId;

    /** 创建时间 */

    private Date gmtCreate;

    /** 修改时间 */

    private Date gmtModified;

    /** 用例描述 */

    private String description;

    /** 创建者 */

    private String creator;

    /** 运行结果 */

    private String issucess;

    private String interfaceName;



}
