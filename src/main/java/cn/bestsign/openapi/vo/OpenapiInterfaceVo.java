package cn.bestsign.openapi.vo;


import cn.bestsign.openapi.domain.OpenapiInterface;

/**
 * 接口对象 openapi_interface
 * 
 * @author bestsign
 * @date 2019-10-10
 */
public class OpenapiInterfaceVo extends OpenapiInterface {
    private static final long serialVersionUID = 1L;

   private String projectName;


    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
