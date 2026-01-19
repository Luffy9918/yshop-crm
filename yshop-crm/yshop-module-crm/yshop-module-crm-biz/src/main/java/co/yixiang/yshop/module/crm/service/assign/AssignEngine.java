package co.yixiang.yshop.module.crm.service.assign;

import co.yixiang.yshop.module.crm.dal.dataobject.crmassignrule.AssignRuleDO;
import co.yixiang.yshop.module.crm.service.crmassignrule.AssignRuleService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 客户分配引擎
 *
 * @author yshop
 */
@Service
@Slf4j
public class AssignEngine {

    @Resource
    private AssignRuleService assignRuleService;

    @Resource
    private StaffAssignStrategy staffAssignStrategy;

    @Resource
    private BalanceAssignStrategy balanceAssignStrategy;

    @Resource
    private SmartAssignStrategy smartAssignStrategy;

    /**
     * 根据规则分配客户
     *
     * @param customerId 客户ID
     * @param conditionType 条件类型 (city/dataSource/all)
     * @param conditionValue 条件值
     * @return 分配的业务员ID
     */
    public Long assignCustomer(Long customerId, String conditionType, String conditionValue) {
        // 获取适用的分配规则（按优先级排序）
        List<AssignRuleDO> rules = assignRuleService.getEnabledAssignRules();

        // 筛选匹配的规则
        AssignRuleDO matchedRule = null;
        for (AssignRuleDO rule : rules) {
            if (isRuleMatch(rule, conditionType, conditionValue)) {
                matchedRule = rule;
                break; // 使用第一个匹配的规则（已按优先级排序）
            }
        }

        if (matchedRule == null) {
            log.warn("未找到匹配的分配规则，客户ID: {}, 条件类型: {}, 条件值: {}", customerId, conditionType, conditionValue);
            return null;
        }

        // 根据规则类型选择策略
        AssignStrategy strategy = getStrategy(matchedRule.getAssignType());
        if (strategy == null) {
            log.error("不支持的分配策略类型: {}", matchedRule.getAssignType());
            return null;
        }

        // 执行分配
        Long staffId = strategy.assign(customerId, matchedRule);
        log.info("客户 {} 分配给业务员 {}, 使用策略: {}", customerId, staffId, matchedRule.getAssignType());
        return staffId;
    }

    /**
     * 判断规则是否匹配
     */
    private boolean isRuleMatch(AssignRuleDO rule, String conditionType, String conditionValue) {
        if (rule.getStatus() == null || rule.getStatus() != 1) {
            return false; // 规则未启用
        }

        String ruleConditionType = rule.getConditionType();
        if ("all".equals(ruleConditionType)) {
            return true; // 全部匹配
        }

        if (conditionType == null) {
            return false;
        }

        if (conditionType.equals(ruleConditionType)) {
            // 精确匹配条件类型
            if (rule.getConditionValue() == null || rule.getConditionValue().isEmpty()) {
                return true; // 条件值为空表示该类型下都匹配
            }
            return conditionValue != null && conditionValue.equals(rule.getConditionValue());
        }

        return false;
    }

    /**
     * 根据类型获取策略
     */
    private AssignStrategy getStrategy(String assignType) {
        return switch (assignType) {
            case "staff" -> staffAssignStrategy;
            case "balance" -> balanceAssignStrategy;
            case "smart" -> smartAssignStrategy;
            default -> null;
        };
    }
}