package cn.bestsign.openapi.service;



import cn.bestsign.openapi.domain.OpenapiInterface;

import java.util.List;

/**
 * 接口Service接口
 * 
 * @author bestsign
 * @date 2019-10-10
 */
public interface IOpenapiInterfaceService 
{
    /**
     * 查询接口
     * 
     * @param interfaceId 接口ID
     * @return 接口
     */
     OpenapiInterface selectOpenapiInterfaceById(Long interfaceId);


    OpenapiInterface selectOpenapiInterfaceByName(String interfaceName);







    /**
     * 查询接口列表
     * 
     * @param openapiInterface 接口
     * @return 接口集合
     */
    public List<OpenapiInterface> selectOpenapiInterfaceList(OpenapiInterface openapiInterface);


    /**
     *根据projectId获取接口列表
     * @param projectid
     * @return
     */
    public List<OpenapiInterface> selectOpenapiInterfaceListByProjectIdLong(Long projectid);

    /**
     *根据projectId获取接口列表
     * @param openapiInterface
     * @return
     */
    public List<OpenapiInterface> selectOpenapiInterfaceListByProjectId(OpenapiInterface openapiInterface);


    /**
     * 新增接口
     * 
     * @param openapiInterface 接口
     * @return 结果
     */
    public int insertOpenapiInterface(OpenapiInterface openapiInterface);

    /**
     * 修改接口
     * 
     * @param openapiInterface 接口
     * @return 结果
     */
    public int updateOpenapiInterface(OpenapiInterface openapiInterface);

    /**
     * 批量删除接口
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteOpenapiInterfaceByIds(String ids);

    /**
     * 删除接口信息
     * 
     * @param interfaceId 接口ID
     * @return 结果
     */
    public int deleteOpenapiInterfaceById(Long interfaceId);


    public int deleteInterfaceByprojectId(String projectIds);
}
