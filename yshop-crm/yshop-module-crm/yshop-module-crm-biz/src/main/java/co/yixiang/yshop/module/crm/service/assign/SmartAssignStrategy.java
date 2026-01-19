package co.yixiang.yshop.module.crm.service.assign;

import co.yixiang.yshop.framework.security.core.util.SecurityFrameworkUtils;
import co.yixiang.yshop.module.crm.dal.dataobject.crmassignrule.AssignRuleDO;
import co.yixiang.yshop.module.crm.dal.dataobject.crmcustomer.CrmCustomerDO;
import co.yixiang.yshop.module.crm.dal.mysql.crmcustomer.CrmCustomerMapper;
import co.yixiang.yshop.module.system.api.user.AdminUserApi;
import co.yixiang.yshop.module.system.api.user.dto.AdminUserRespDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 智能评分分配策略
 *
 * @author yshop
 */
@Component
@Slf4j
public class SmartAssignStrategy implements AssignStrategy {

    @Resource
    private AdminUserApi adminUserApi;

    @Resource
    private CrmCustomerMapper customerMapper;

    @Override
    public Long assign(Long customerId, AssignRuleDO rule) {
        log.info("使用智能评分策略分配客户 {}", customerId);

        // 1. 获取客户信息（用于地域匹配）
        CrmCustomerDO customer = customerMapper.selectById(customerId);
        if (customer == null) {
            log.warn("客户不存在，使用目标业务员: {}", rule.getTargetStaffId());
            return rule.getTargetStaffId();
        }

        // 2. 获取当前用户的下属作为候选业务员
        Long currentUserId = SecurityFrameworkUtils.getLoginUserId();
        List<Long> staffIds = adminUserApi.getUserListBySubordinateIds(currentUserId);

        if (staffIds == null || staffIds.isEmpty()) {
            log.warn("当前用户没有下属业务员，使用目标业务员: {}", rule.getTargetStaffId());
            return rule.getTargetStaffId();
        }

        // 3. 获取权重配置
        BigDecimal weightConversion = rule.getWeightConversion() != null
                ? rule.getWeightConversion()
                : new BigDecimal("0.4");
        BigDecimal weightWorkload = rule.getWeightWorkload() != null
                ? rule.getWeightWorkload()
                : new BigDecimal("0.3");
        BigDecimal weightRegion = rule.getWeightRegion() != null
                ? rule.getWeightRegion()
                : new BigDecimal("0.2");
        BigDecimal weightExpertise = rule.getWeightExpertise() != null
                ? rule.getWeightExpertise()
                : new BigDecimal("0.1");

        // 4. 获取业务员详情列表
        List<AdminUserRespDTO> staffList = adminUserApi.getUserList(staffIds);
        if (staffList == null || staffList.isEmpty()) {
            return rule.getTargetStaffId();
        }

        // 5. 计算每个业务员的综合得分
        List<StaffScore> staffScores = staffList.stream()
                .map(staff -> calculateScore(staff, customer, weightConversion, weightWorkload, weightRegion, weightExpertise))
                .collect(Collectors.toList());

        // 6. 选择得分最高的业务员
        StaffScore selected = staffScores.stream()
                .max(Comparator.comparingDouble(StaffScore::getFinalScore))
                .orElse(null);

        if (selected == null) {
            return rule.getTargetStaffId();
        }

        log.info("智能评分选择业务员 {}，综合得分: {}", selected.getStaffId(), selected.getFinalScore());
        return selected.getStaffId();
    }

    /**
     * 计算业务员综合得分
     */
    private StaffScore calculateScore(AdminUserRespDTO staff, CrmCustomerDO customer,
                                       BigDecimal weightConversion, BigDecimal weightWorkload,
                                       BigDecimal weightRegion, BigDecimal weightExpertise) {
        StaffScore score = new StaffScore();
        score.setStaffId(staff.getId());

        // 1. 转化率得分 (临时实现：随机值 0.6-0.9)
        // 实际应从合同/客户统计数据计算
        double conversionScore = 0.6 + Math.random() * 0.3;

        // 2. 工作量得分 (客户数越少得分越高)
        Long customerCount = customerMapper.selectCount(new LambdaQueryWrapper<CrmCustomerDO>()
                .eq(CrmCustomerDO::getOwnerUserId, staff.getId()));
        double workloadScore = customerCount > 0 ? (100.0 / (customerCount + 10)) : 1.0;

        // 3. 地域得分 (城市匹配)
        double regionScore = 0.5; // 默认中等匹配
        // 实际可以根据客户城市与业务员负责区域的匹配度计算

        // 4. 专业度得分 (临时实现：基于创建时间/经验的简单评分)
        double expertiseScore = 0.7; // 默认良好
        // 实际可以基于业务员的历史业绩、行业经验等计算

        // 5. 综合得分 = 各项权重 * 各项得分
        double finalScore = weightConversion.doubleValue() * conversionScore +
                weightWorkload.doubleValue() * workloadScore +
                weightRegion.doubleValue() * regionScore +
                weightExpertise.doubleValue() * expertiseScore;

        score.setConversionScore(conversionScore);
        score.setWorkloadScore(workloadScore);
        score.setRegionScore(regionScore);
        score.setExpertiseScore(expertiseScore);
        score.setFinalScore(BigDecimal.valueOf(finalScore).setScale(4, RoundingMode.HALF_UP).doubleValue());

        log.debug("业务员 {} 评分 - 转化率:{}, 工作量:{}, 地域:{}, 专业度:{}, 综合:{}",
                staff.getId(), conversionScore, workloadScore, regionScore, expertiseScore, finalScore);

        return score;
    }

    @Override
    public String getType() {
        return "smart";
    }

    @Data
    private static class StaffScore {
        private Long staffId;
        private double conversionScore;
        private double workloadScore;
        private double regionScore;
        private double expertiseScore;
        private double finalScore;
    }
}