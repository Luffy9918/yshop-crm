package co.yixiang.yshop.module.crm.service.assign;

import co.yixiang.yshop.module.crm.dal.dataobject.crmassignrule.AssignRuleDO;

/**
 * 客户分配策略接口
 *
 * @author yshop
 */
public interface AssignStrategy {

    /**
     * 执行客户分配
     *
     * @param customerId 客户ID
     * @param rule 分配规则
     * @return 分配的业务员ID
     */
    Long assign(Long customerId, AssignRuleDO rule);

    /**
     * 获取策略类型
     *
     * @return 策略类型代码
     */
    String getType();
}