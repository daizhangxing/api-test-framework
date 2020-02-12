package cn.bestsign.openapi.service.impl;


import cn.bestsign.openapi.domain.OpenapiCasedetail;
import cn.bestsign.openapi.domain.OpenapiInterface;
import cn.bestsign.openapi.domain.OpenapiProject;
import cn.bestsign.openapi.mapper.OpenapiInterfaceMapper;
import cn.bestsign.openapi.mapper.OpenapiProjectMapper;
import cn.bestsign.openapi.service.IOpenapiCasedetailService;
import cn.bestsign.openapi.service.IOpenapiInterfaceService;
import cn.bestsign.openapi.service.IOpenapiProjectService;
import cn.bestsign.utils.comon.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 项目Service业务层处理
 * 
 * @author bestsign
 * @date 2019-10-10
 */
@Service
public class OpenapiProjectServiceImpl implements IOpenapiProjectService {
    @Autowired
    private OpenapiProjectMapper openapiProjectMapper;

    @Autowired
    private IOpenapiInterfaceService openapiInterfaceService;

    @Autowired
    private IOpenapiCasedetailService openapiCasedetailService;

    @Autowired
    private OpenapiInterfaceMapper openapiInterfaceMapper;

    /**
     * 查询项目
     * 
     * @param projectId 项目ID
     * @return 项目
     */
    @Override
    public OpenapiProject selectOpenapiProjectById(Long projectId)
    {
        return openapiProjectMapper.selectOpenapiProjectById(projectId);
    }

    /**
     * 查询项目列表
     * 
     * @param openapiProject 项目
     * @return 项目
     */
    @Override
    public List<OpenapiProject> selectOpenapiProjectList(OpenapiProject openapiProject)
    {
        List<OpenapiProject> openapiProjectList = openapiProjectMapper.selectOpenapiProjectList(openapiProject);
        for (int i = 0; i < openapiProjectList.size(); i++) {
            int projectCaseNum = 0;
            OpenapiProject project = openapiProjectList.get(i);
            List<OpenapiInterface> openapiInterfaces = openapiInterfaceMapper.selectOpenapiInterfaceListByProjectIdLong(project.getProjectId());
            for (int j = 0; j < openapiInterfaces.size(); j++) {
                OpenapiInterface openapiInterface = openapiInterfaces.get(j);

                //得到某个接口的case总数
                List<OpenapiCasedetail> openapiCasedetails = openapiCasedetailService.selectOpenapiCaseListByInterfaceId(openapiInterface.getInterfaceId());
                int interfaceCaseNum  = openapiCasedetails.size();
                projectCaseNum = projectCaseNum+interfaceCaseNum;

            }

            project.setCaseNum(projectCaseNum);
            project.setInterfaceNum(openapiInterfaces.size());



        }

        return openapiProjectList;
    }


    /**
     * 新增项目
     * 
     * @param openapiProject 项目
     * @return 结果
     */
    @Override
    public int insertOpenapiProject(OpenapiProject openapiProject)
    {
        return openapiProjectMapper.insertOpenapiProject(openapiProject);
    }

    /**
     * 修改项目
     * 
     * @param openapiProject 项目
     * @return 结果
     */
    @Override
    public int updateOpenapiProject(OpenapiProject openapiProject)
    {
        return openapiProjectMapper.updateOpenapiProject(openapiProject);
    }

    /**
     * 删除项目对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteOpenapiProjectByIds(String ids)
    {
        return openapiProjectMapper.deleteOpenapiProjectByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除项目信息
     * 
     * @param projectId 项目ID
     * @return 结果
     */
    public int deleteOpenapiProjectById(Long projectId)
    {
        return openapiProjectMapper.deleteOpenapiProjectById(projectId);
    }
}
