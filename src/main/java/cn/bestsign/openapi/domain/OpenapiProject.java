package cn.bestsign.openapi.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 项目对象 openapi_project
 * 
 * @author bestsign
 * @date 2019-10-10
 */

@Getter
@Setter
@ToString
public class OpenapiProject{
    private static final long serialVersionUID = 1L;

    /** 项目id，主键 */
    private Long projectId;

    /** 项目名称 */

    private String projectName;

    /** 项目描述 */

    private String description;

    /** 创建者 */

    private String creator;

    private int interfaceNum;

    private int caseNum;



}
