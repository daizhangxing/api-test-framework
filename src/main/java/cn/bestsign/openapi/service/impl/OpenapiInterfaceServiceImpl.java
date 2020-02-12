package cn.bestsign.openapi.service.impl;


import cn.bestsign.openapi.domain.OpenapiCasedetail;
import cn.bestsign.openapi.domain.OpenapiInterface;
import cn.bestsign.openapi.domain.OpenapiProject;
import cn.bestsign.openapi.mapper.OpenapiInterfaceMapper;
import cn.bestsign.openapi.service.IOpenapiCasedetailService;
import cn.bestsign.openapi.service.IOpenapiInterfaceService;
import cn.bestsign.openapi.service.IOpenapiProjectService;

import cn.bestsign.utils.comon.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 接口Service业务层处理
 *
 * @author bestsign
 * @date 2019-10-10
 */
@Service
public class OpenapiInterfaceServiceImpl implements IOpenapiInterfaceService {


    @Autowired
    private OpenapiInterfaceMapper openapiInterfaceMapper;

    @Autowired
    private IOpenapiCasedetailService openapiCasedetailService;

    @Autowired
    private IOpenapiProjectService openapiProjectService;

    @Override
    public OpenapiInterface selectOpenapiInterfaceByName(String interfaceName) {
        return openapiInterfaceMapper.selectOpenapiInterfaceByName(interfaceName);
    }

    @Transactional
    public int deleteInterfaceByprojectId(String projectIds) {

        int case_result = 0;
        int interface_result = 0;

        String separator = ",";

        String[] projectIdList;

        if(projectIds.contains(separator)){


            projectIdList  = projectIds.split(",");

        }else {

            projectIdList = new String[1];
            projectIdList[0] = projectIds;
        }


        for (String projectId : projectIdList) {

            List<String> interfaceIdList = new ArrayList<>();

            List<OpenapiInterface> openapiInterfaces = openapiInterfaceMapper.selectOpenapiInterfaceListByProjectIdLong(Long.valueOf(projectId));

            Iterator<OpenapiInterface> iterator = openapiInterfaces.iterator();
            while (iterator.hasNext()) {

                OpenapiInterface next = iterator.next();
                interfaceIdList.add(String.valueOf(next.getInterfaceId()));
            }

            if(interfaceIdList.size()>0){

                String[] interfaceIds = interfaceIdList.toArray(new String[interfaceIdList.size()]);

                //删除与list中接口相关联的所有用例
                case_result = openapiCasedetailService.deleteCaseListByInterfaceIds(interfaceIds);
                //删除interfacceids中的接口
                interface_result = openapiInterfaceMapper.deleteOpenapiInterfaceByIds(interfaceIds);
            }




        }


        return interface_result;
    }


    @Override
    public List<OpenapiInterface> selectOpenapiInterfaceListByProjectIdLong(Long projectid) {
        return openapiInterfaceMapper.selectOpenapiInterfaceListByProjectIdLong(projectid);
    }

    @Override
    public List<OpenapiInterface> selectOpenapiInterfaceListByProjectId(OpenapiInterface openapiInterface) {

        return openapiInterfaceMapper.selectOpenapiInterfaceListByProjectId(openapiInterface);

    }


    /**
     * 查询接口
     *
     * @param interfaceId 接口ID
     * @return 接口
     */
    @Override
    public OpenapiInterface selectOpenapiInterfaceById(Long interfaceId) {
        return openapiInterfaceMapper.selectOpenapiInterfaceById(interfaceId);
    }

    /**
     * 查询接口列表
     *
     * @param openapiInterface 接口
     * @return 接口
     */
    @Override
    public List<OpenapiInterface> selectOpenapiInterfaceList(OpenapiInterface openapiInterface) {

        List<OpenapiInterface> list = openapiInterfaceMapper.selectOpenapiInterfaceList(openapiInterface);

        for (int i = 0; i < list.size(); i++) {
            OpenapiInterface oepnapi_interface = list.get(i);
            //得到某个接口的case总数
            List<OpenapiCasedetail> openapiCasedetails = openapiCasedetailService.selectOpenapiCaseListByInterfaceId(oepnapi_interface.getInterfaceId());
            int caseNum  = openapiCasedetails.size();
            //得到项目名称
            OpenapiProject openapiProject = openapiProjectService.selectOpenapiProjectById(oepnapi_interface.getProjectId());
            String projectName = openapiProject.getProjectName();

            oepnapi_interface.setProjectName(projectName);
            oepnapi_interface.setCaseNum(caseNum);

        }
        return list;
    }

    /**
     * 新增接口
     *
     * @param openapiInterface 接口
     * @return 结果
     */
    @Override
    public int insertOpenapiInterface(OpenapiInterface openapiInterface) {
        return openapiInterfaceMapper.insertOpenapiInterface(openapiInterface);
    }

    /**
     * 修改接口
     *
     * @param openapiInterface 接口
     * @return 结果
     */
    @Override
    public int updateOpenapiInterface(OpenapiInterface openapiInterface) {
        return openapiInterfaceMapper.updateOpenapiInterface(openapiInterface);
    }

    /**
     * 删除接口对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteOpenapiInterfaceByIds(String ids) {

        //删除接口相关联的用例
        int i = openapiCasedetailService.deleteCaseListByInterfaceIds(Convert.toStrArray(ids));

        return openapiInterfaceMapper.deleteOpenapiInterfaceByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除接口信息
     *
     * @param interfaceId 接口ID
     * @return 结果
     */
    public int deleteOpenapiInterfaceById(Long interfaceId) {
        return openapiInterfaceMapper.deleteOpenapiInterfaceById(interfaceId);
    }
}
