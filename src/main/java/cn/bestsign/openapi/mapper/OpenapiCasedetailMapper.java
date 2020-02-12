package cn.bestsign.openapi.mapper;

import cn.bestsign.openapi.domain.OpenapiCasedetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用例详情Mapper接口
 * 
 * @author bestsign
 * @date 2019-10-10
 */

public interface OpenapiCasedetailMapper 
{
    /**
     * 查询用例详情
     * 
     * @param caseId 用例详情ID
     * @return 用例详情
     */
    public OpenapiCasedetail selectOpenapiCasedetailById(Long caseId);

    /**
     * 查询用例详情列表
     * 
     * @param openapiCasedetail 用例详情
     * @return 用例详情集合
     */
    public List<OpenapiCasedetail> selectOpenapiCasedetailList(OpenapiCasedetail openapiCasedetail);


    /**
     * 根据接口Id查询其中所有的用例
     * @param interfaceId
     * @return
     */
    public List<OpenapiCasedetail> selectOpenapiCaseListByInterfaceId(Long interfaceId);

    /**
     * 新增用例详情
     * 
     * @param openapiCasedetail 用例详情
     * @return 结果
     */
    public int insertOpenapiCasedetail(OpenapiCasedetail openapiCasedetail);

    /**
     * 修改用例详情
     * 
     * @param openapiCasedetail 用例详情
     * @return 结果
     */
    public int updateOpenapiCasedetail(OpenapiCasedetail openapiCasedetail);

    /**
     * 删除用例详情
     * 
     * @param caseId 用例详情ID
     * @return 结果
     */
    public int deleteOpenapiCasedetailById(Long caseId);

    /**
     * 批量删除用例详情
     * 
     * @param caseIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteOpenapiCasedetailByIds(String[] caseIds);


    /**
     * 批量删除相关接口的所有用例
     *
     * @param interfaceids 需要删除的数据接口id
     * @return 结果
     */
    public int deleteOpenapiCasedetailByInterfaceIds(String[] interfaceids);
}
