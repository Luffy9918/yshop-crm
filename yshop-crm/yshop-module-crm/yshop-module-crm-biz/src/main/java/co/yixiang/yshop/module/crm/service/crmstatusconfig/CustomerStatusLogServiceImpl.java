package co.yixiang.yshop.module.crm.service.crmstatusconfig;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.module.crm.controller.admin.crmstatusconfig.vo.StatusLogPageReqVO;
import co.yixiang.yshop.module.crm.dal.dataobject.crmstatusconfig.CustomerStatusLogDO;
import co.yixiang.yshop.module.crm.dal.mysql.crmstatusconfig.CustomerStatusLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * 客户状态变更日志 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
@Slf4j
public class CustomerStatusLogServiceImpl implements CustomerStatusLogService {

    @Resource
    private CustomerStatusLogMapper customerStatusLogMapper;

    @Override
    public Long recordStatusChange(Long customerId, String oldStatus, String newStatus,
                                   Long operatorId, String operatorName, String changeReason) {
        CustomerStatusLogDO log = CustomerStatusLogDO.builder()
                .customerId(customerId)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .operatorId(operatorId)
                .operatorName(operatorName)
                .changeReason(changeReason)
                .build();
        customerStatusLogMapper.insert(log);
        return log.getId();
    }

    @Override
    public List<CustomerStatusLogDO> getStatusLogByCustomerId(Long customerId) {
        return customerStatusLogMapper.selectListByCustomerId(customerId);
    }

    @Override
    public PageResult<CustomerStatusLogDO> getStatusLogPage(StatusLogPageReqVO pageReqVO) {
        return customerStatusLogMapper.selectPage(pageReqVO);
    }

    @Override
    public CustomerStatusLogDO getStatusLog(Long id) {
        return customerStatusLogMapper.selectById(id);
    }
}