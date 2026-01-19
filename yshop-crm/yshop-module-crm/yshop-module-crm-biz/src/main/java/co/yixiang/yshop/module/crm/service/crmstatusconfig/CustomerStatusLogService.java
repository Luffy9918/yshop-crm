package co.yixiang.yshop.module.crm.service.crmstatusconfig;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.module.crm.controller.admin.crmstatusconfig.vo.StatusLogPageReqVO;
import co.yixiang.yshop.module.crm.dal.dataobject.crmstatusconfig.CustomerStatusLogDO;

import java.util.List;

/**
 * 客户状态变更日志 Service 接口
 *
 * @author yshop
 */
public interface CustomerStatusLogService {

    /**
     * 记录状态变更
     *
     * @param customerId 客户ID
     * @param oldStatus 原状态
     * @param newStatus 新状态
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @param changeReason 变更原因
     * @return 编号
     */
    Long recordStatusChange(Long customerId, String oldStatus, String newStatus,
                            Long operatorId, String operatorName, String changeReason);

    /**
     * 获取客户的状态变更历史
     *
     * @param customerId 客户ID
     * @return 状态变更历史
     */
    List<CustomerStatusLogDO> getStatusLogByCustomerId(Long customerId);

    /**
     * 获取状态变更日志分页
     *
     * @param pageReqVO 分页查询
     * @return 状态变更日志分页
     */
    PageResult<CustomerStatusLogDO> getStatusLogPage(StatusLogPageReqVO pageReqVO);

    /**
     * 获得状态变更日志
     *
     * @param id 编号
     * @return 状态变更日志
     */
    CustomerStatusLogDO getStatusLog(Long id);
}