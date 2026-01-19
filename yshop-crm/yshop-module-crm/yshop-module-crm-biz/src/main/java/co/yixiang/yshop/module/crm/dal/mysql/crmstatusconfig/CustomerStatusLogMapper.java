package co.yixiang.yshop.module.crm.dal.mysql.crmstatusconfig;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.mybatis.core.mapper.BaseMapperX;
import co.yixiang.yshop.framework.mybatis.core.query.LambdaQueryWrapperX;
import co.yixiang.yshop.module.crm.controller.admin.crmstatusconfig.vo.StatusLogPageReqVO;
import co.yixiang.yshop.module.crm.dal.dataobject.crmstatusconfig.CustomerStatusLogDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 客户状态变更日志 Mapper
 *
 * @author yshop
 */
@Mapper
public interface CustomerStatusLogMapper extends BaseMapperX<CustomerStatusLogDO> {

    default PageResult<CustomerStatusLogDO> selectPage(StatusLogPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CustomerStatusLogDO>()
                .eqIfPresent(CustomerStatusLogDO::getCustomerId, reqVO.getCustomerId())
                .eqIfPresent(CustomerStatusLogDO::getOldStatus, reqVO.getOldStatus())
                .eqIfPresent(CustomerStatusLogDO::getNewStatus, reqVO.getNewStatus())
                .eqIfPresent(CustomerStatusLogDO::getOperatorId, reqVO.getOperatorId())
                .betweenIfPresent(CustomerStatusLogDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CustomerStatusLogDO::getId));
    }

    default List<CustomerStatusLogDO> selectListByCustomerId(Long customerId) {
        return selectList(new LambdaQueryWrapperX<CustomerStatusLogDO>()
                .eq(CustomerStatusLogDO::getCustomerId, customerId)
                .orderByDesc(CustomerStatusLogDO::getId));
    }

}