package co.yixiang.yshop.module.crm.dal.mysql.crmassignrule;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.mybatis.core.mapper.BaseMapperX;
import co.yixiang.yshop.framework.mybatis.core.query.LambdaQueryWrapperX;
import co.yixiang.yshop.module.crm.controller.admin.crmassignrule.vo.AssignRulePageReqVO;
import co.yixiang.yshop.module.crm.dal.dataobject.crmassignrule.AssignRuleDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 线索分配规则 Mapper
 *
 * @author yshop
 */
@Mapper
public interface AssignRuleMapper extends BaseMapperX<AssignRuleDO> {

    default PageResult<AssignRuleDO> selectPage(AssignRulePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AssignRuleDO>()
                .likeIfPresent(AssignRuleDO::getRuleName, reqVO.getRuleName())
                .eqIfPresent(AssignRuleDO::getConditionType, reqVO.getConditionType())
                .eqIfPresent(AssignRuleDO::getAssignType, reqVO.getAssignType())
                .eqIfPresent(AssignRuleDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(AssignRuleDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(AssignRuleDO::getPriority)
                .orderByDesc(AssignRuleDO::getId));
    }

}