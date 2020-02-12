package cn.bestsign.openapi.mapper;



import cn.bestsign.openapi.domain.OpenapiProject;

import java.util.List;

/**
 * 项目Mapper接口
 * 
 * @author bestsign
 * @date 2019-10-10
 */
public interface OpenapiProjectMapper 
{
    /**
     * 查询项目
     * 
     * @param projectId 项目ID
     * @return 项目
     */
    public OpenapiProject selectOpenapiProjectById(Long projectId);

    /**
     * 查询项目列表
     * 
     * @param openapiProject 项目
     * @return 项目集合
     */
    public List<OpenapiProject> selectOpenapiProjectList(OpenapiProject openapiProject);

    /**
     * 新增项目
     * 
     * @param openapiProject 项目
     * @return 结果
     */
    public int insertOpenapiProject(OpenapiProject openapiProject);

    /**
     * 修改项目
     * 
     * @param openapiProject 项目
     * @return 结果
     */
    public int updateOpenapiProject(OpenapiProject openapiProject);

    /**
     * 删除项目
     * 
     * @param projectId 项目ID
     * @return 结果
     */
    public int deleteOpenapiProjectById(Long projectId);

    /**
     * 批量删除项目
     * 
     * @param projectIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteOpenapiProjectByIds(String[] projectIds);
}
