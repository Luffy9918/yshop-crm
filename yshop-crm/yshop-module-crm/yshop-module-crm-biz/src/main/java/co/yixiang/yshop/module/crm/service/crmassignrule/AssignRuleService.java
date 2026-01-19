package co.yixiang.yshop.module.crm.service.crmassignrule;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.module.crm.controller.admin.crmassignrule.vo.AssignRulePageReqVO;
import co.yixiang.yshop.module.crm.controller.admin.crmassignrule.vo.AssignRuleSaveReqVO;
import co.yixiang.yshop.module.crm.dal.dataobject.crmassignrule.AssignRuleDO;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 分配规则 Service 接口
 *
 * @author yshop
 */
public interface AssignRuleService {

    /**
     * 创建分配规则
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createAssignRule(@Valid AssignRuleSaveReqVO createReqVO);

    /**
     * 更新分配规则
     *
     * @param updateReqVO 更新信息
     */
    void updateAssignRule(@Valid AssignRuleSaveReqVO updateReqVO);

    /**
     * 删除分配规则
     *
     * @param id 编号
     */
    void deleteAssignRule(Long id);

    /**
     * 获得分配规则
     *
     * @param id 编号
     * @return 分配规则
     */
    AssignRuleDO getAssignRule(Long id);

    /**
     * 获得分配规则分页
     *
     * @param pageReqVO 分页查询
     * @return 分配规则分页
     */
    PageResult<AssignRuleDO> getAssignRulePage(AssignRulePageReqVO pageReqVO);

    /**
     * 获取所有启用的分配规则
     *
     * @return 分配规则列表
     */
    List<AssignRuleDO> getEnabledAssignRules();

    /**
     * 校验分配规则是否存在
     *
     * @param id 规则ID
     */
    void validateAssignRuleExists(Long id);
}