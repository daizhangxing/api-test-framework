package cn.bestsign.openapi.service.impl;



import cn.bestsign.openapi.domain.OpenapiCasedetail;
import cn.bestsign.openapi.domain.OpenapiInterface;
import cn.bestsign.openapi.mapper.OpenapiCasedetailMapper;
import cn.bestsign.openapi.service.IOpenapiCasedetailService;
import cn.bestsign.openapi.service.IOpenapiInterfaceService;

import cn.bestsign.utils.comon.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用例详情Service业务层处理
 *
 * @author bestsign
 * @date 2019-10-10
 */
@Service
public class OpenapiCasedetailServiceImpl implements IOpenapiCasedetailService {

    @Autowired
    private OpenapiCasedetailMapper openapiCasedetailMapper;

    @Autowired
    private IOpenapiInterfaceService openapiInterfaceService;


    @Transactional
    public int deleteCaseListByInterfaceIds(String[] interfaceIds) {



        int result = openapiCasedetailMapper.deleteOpenapiCasedetailByInterfaceIds(interfaceIds);


        return result;

    }


    /**
     * 查询用例详情
     *
     * @param caseId 用例详情ID
     * @return 用例详情
     */
    @Override
    public OpenapiCasedetail selectOpenapiCasedetailById(Long caseId) {
        return openapiCasedetailMapper.selectOpenapiCasedetailById(caseId);
    }


    @Override
    public List<OpenapiCasedetail> selectOpenapiCaseListByInterfaceId(Long interfaceId) {

        OpenapiInterface openapiInterface = openapiInterfaceService.selectOpenapiInterfaceById(interfaceId);
        String interfaceName = openapiInterface.getInterfaceName();
        List<OpenapiCasedetail> openapiCasedetails = openapiCasedetailMapper.selectOpenapiCaseListByInterfaceId(interfaceId);


        for (int i = 0; i < openapiCasedetails.size(); i++) {
            OpenapiCasedetail openapiCasedetail = openapiCasedetails.get(i);
            openapiCasedetail.setInterfaceName(interfaceName);
        }

        return openapiCasedetails;
    }

    /**
     * 查询用例详情列表
     *
     * @param openapiCasedetail 用例详情
     * @return 用例详情
     */
    @Override
    public List<OpenapiCasedetail> selectOpenapiCasedetailList(OpenapiCasedetail openapiCasedetail) {

        List<OpenapiCasedetail> openapiCasedetailList = openapiCasedetailMapper.selectOpenapiCasedetailList(openapiCasedetail);

        for (int i = 0; i < openapiCasedetailList.size(); i++) {
            OpenapiCasedetail casedetail = openapiCasedetailList.get(i);

            Long interfaceId = casedetail.getInterfactId();

            OpenapiInterface openapiInterface = openapiInterfaceService.selectOpenapiInterfaceById(interfaceId);
            String interfaceName = openapiInterface.getInterfaceName();
            casedetail.setInterfaceName(interfaceName);

        }



        return openapiCasedetailList;
    }

    /**
     * 新增用例详情
     *
     * @param openapiCasedetail 用例详情
     * @return 结果
     */
    @Override
    public int insertOpenapiCasedetail(OpenapiCasedetail openapiCasedetail) {
        return openapiCasedetailMapper.insertOpenapiCasedetail(openapiCasedetail);
    }

    /**
     * 修改用例详情
     *
     * @param openapiCasedetail 用例详情
     * @return 结果
     */
    @Override
    public int updateOpenapiCasedetail(OpenapiCasedetail openapiCasedetail) {
        return openapiCasedetailMapper.updateOpenapiCasedetail(openapiCasedetail);
    }

    /**
     * 删除用例详情对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteOpenapiCasedetailByIds(String ids) {
        return openapiCasedetailMapper.deleteOpenapiCasedetailByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除用例详情信息
     *
     * @param caseId 用例详情ID
     * @return 结果
     */
    public int deleteOpenapiCasedetailById(Long caseId) {
        return openapiCasedetailMapper.deleteOpenapiCasedetailById(caseId);
    }
}
