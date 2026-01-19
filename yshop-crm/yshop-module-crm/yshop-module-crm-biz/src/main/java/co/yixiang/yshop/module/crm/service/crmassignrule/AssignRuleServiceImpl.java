package co.yixiang.yshop.module.crm.service.crmassignrule;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.module.crm.controller.admin.crmassignrule.vo.AssignRulePageReqVO;
import co.yixiang.yshop.module.crm.controller.admin.crmassignrule.vo.AssignRuleSaveReqVO;
import co.yixiang.yshop.module.crm.dal.dataobject.crmassignrule.AssignRuleDO;
import co.yixiang.yshop.module.crm.dal.mysql.crmassignrule.AssignRuleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.module.crm.enums.ErrorCodeConstants.ASSIGN_RULE_NOT_EXISTS;

/**
 * 分配规则 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
@Slf4j
public class AssignRuleServiceImpl implements AssignRuleService {

    @Resource
    private AssignRuleMapper assignRuleMapper;

    @Override
    public Long createAssignRule(@Valid AssignRuleSaveReqVO createReqVO) {
        // 插入
        AssignRuleDO assignRule = AssignRuleDO.builder()
                .ruleName(createReqVO.getRuleName())
                .conditionType(createReqVO.getConditionType())
                .conditionValue(createReqVO.getConditionValue())
                .assignType(createReqVO.getAssignType())
                .targetStaffId(createReqVO.getTargetStaffId())
                .weightConversion(createReqVO.getWeightConversion() != null ? createReqVO.getWeightConversion() : new BigDecimal("0.4"))
                .weightWorkload(createReqVO.getWeightWorkload() != null ? createReqVO.getWeightWorkload() : new BigDecimal("0.3"))
                .weightRegion(createReqVO.getWeightRegion() != null ? createReqVO.getWeightRegion() : new BigDecimal("0.2"))
                .weightExpertise(createReqVO.getWeightExpertise() != null ? createReqVO.getWeightExpertise() : new BigDecimal("0.1"))
                .priority(createReqVO.getPriority() != null ? createReqVO.getPriority() : 0)
                .status(createReqVO.getStatus() != null ? createReqVO.getStatus() : 1)
                .remark(createReqVO.getRemark())
                .build();
        assignRuleMapper.insert(assignRule);
        return assignRule.getId();
    }

    @Override
    public void updateAssignRule(@Valid AssignRuleSaveReqVO updateReqVO) {
        // 校验存在
        validateAssignRuleExists(updateReqVO.getId());
        // 更新
        AssignRuleDO updateObj = AssignRuleDO.builder()
                .id(updateReqVO.getId())
                .ruleName(updateReqVO.getRuleName())
                .conditionType(updateReqVO.getConditionType())
                .conditionValue(updateReqVO.getConditionValue())
                .assignType(updateReqVO.getAssignType())
                .targetStaffId(updateReqVO.getTargetStaffId())
                .weightConversion(updateReqVO.getWeightConversion())
                .weightWorkload(updateReqVO.getWeightWorkload())
                .weightRegion(updateReqVO.getWeightRegion())
                .weightExpertise(updateReqVO.getWeightExpertise())
                .priority(updateReqVO.getPriority())
                .status(updateReqVO.getStatus())
                .remark(updateReqVO.getRemark())
                .build();
        assignRuleMapper.updateById(updateObj);
    }

    @Override
    public void deleteAssignRule(Long id) {
        // 校验存在
        validateAssignRuleExists(id);
        // 删除
        assignRuleMapper.deleteById(id);
    }

    @Override
    public AssignRuleDO getAssignRule(Long id) {
        return assignRuleMapper.selectById(id);
    }

    @Override
    public PageResult<AssignRuleDO> getAssignRulePage(AssignRulePageReqVO pageReqVO) {
        return assignRuleMapper.selectPage(pageReqVO);
    }

    @Override
    public List<AssignRuleDO> getEnabledAssignRules() {
        return assignRuleMapper.selectList("status", 1);
    }

    @Override
    public void validateAssignRuleExists(Long id) {
        if (assignRuleMapper.selectById(id) == null) {
            throw exception(ASSIGN_RULE_NOT_EXISTS);
        }
    }
}