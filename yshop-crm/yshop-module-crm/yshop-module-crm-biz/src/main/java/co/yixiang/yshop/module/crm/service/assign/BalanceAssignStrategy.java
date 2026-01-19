package co.yixiang.yshop.module.crm.service.assign;

import co.yixiang.yshop.framework.security.core.util.SecurityFrameworkUtils;
import co.yixiang.yshop.module.crm.dal.dataobject.crmassignrule.AssignRuleDO;
import co.yixiang.yshop.module.crm.dal.mysql.crmcustomer.CrmCustomerMapper;
import co.yixiang.yshop.module.system.api.user.AdminUserApi;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 负载均衡分配策略
 *
 * @author yshop
 */
@Component
@Slf4j
public class BalanceAssignStrategy implements AssignStrategy {

    @Resource
    private AdminUserApi adminUserApi;

    @Resource
    private CrmCustomerMapper customerMapper;

    @Override
    public Long assign(Long customerId, AssignRuleDO rule) {
        log.info("使用负载均衡策略分配客户 {}", customerId);

        // 1. 获取当前用户的下属作为候选业务员
        Long currentUserId = SecurityFrameworkUtils.getLoginUserId();
        List<Long> staffIds = adminUserApi.getUserListBySubordinateIds(currentUserId);

        if (staffIds == null || staffIds.isEmpty()) {
            log.warn("当前用户没有下属业务员，使用目标业务员: {}", rule.getTargetStaffId());
            return rule.getTargetStaffId();
        }

        // 2. 统计每个业务员的当前客户数
        Map<Long, Long> customerCountMap = staffIds.stream()
                .collect(Collectors.toMap(
                        staffId -> staffId,
                        staffId -> customerMapper.selectCount(new LambdaQueryWrapper<co.yixiang.yshop.module.crm.dal.dataobject.crmcustomer.CrmCustomerDO>()
                                .eq(co.yixiang.yshop.module.crm.dal.dataobject.crmcustomer.CrmCustomerDO::getOwnerUserId, staffId))
                ));

        // 3. 选择客户数最少的业务员
        Long selectedStaffId = customerCountMap.entrySet().stream()
                .min(Comparator.comparingLong(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(rule.getTargetStaffId());

        log.info("负载均衡选择业务员 {}，当前客户数: {}", selectedStaffId, customerCountMap.get(selectedStaffId));
        return selectedStaffId;
    }

    @Override
    public String getType() {
        return "balance";
    }
}