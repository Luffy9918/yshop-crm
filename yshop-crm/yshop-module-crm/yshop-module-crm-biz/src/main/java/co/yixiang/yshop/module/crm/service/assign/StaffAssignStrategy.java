package co.yixiang.yshop.module.crm.service.assign;

import co.yixiang.yshop.module.crm.dal.dataobject.crmassignrule.AssignRuleDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 指定业务员分配策略
 *
 * @author yshop
 */
@Component
@Slf4j
public class StaffAssignStrategy implements AssignStrategy {

    @Override
    public Long assign(Long customerId, AssignRuleDO rule) {
        log.info("使用指定业务员策略分配客户 {} 到业务员 {}", customerId, rule.getTargetStaffId());
        return rule.getTargetStaffId();
    }

    @Override
    public String getType() {
        return "staff";
    }
}