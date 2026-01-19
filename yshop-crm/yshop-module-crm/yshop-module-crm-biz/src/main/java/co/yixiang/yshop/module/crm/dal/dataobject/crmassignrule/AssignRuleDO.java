package co.yixiang.yshop.module.crm.dal.dataobject.crmassignrule;

import com.baomidou.mybatisplus.annotation.*;
import co.yixiang.yshop.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;

/**
 * 线索分配规则 DO - 智能分配规则
 *
 * @author yshop
 */
@TableName("crm_assign_rule")
@KeySequence("crm_assign_rule_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignRuleDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 条件类型: city=按城市 dataSource=按数据源 all=全部
     */
    private String conditionType;

    /**
     * 条件值 (城市编码/数据源ID)
     */
    private String conditionValue;

    /**
     * 分配类型: staff=指定业务员 balance=负载均衡 smart=智能评分
     */
    private String assignType;

    /**
     * 目标业务员ID (assign_type=staff时使用)
     */
    private Long targetStaffId;

    /**
     * 转化率权重
     */
    private BigDecimal weightConversion;

    /**
     * 工作量权重
     */
    private BigDecimal weightWorkload;

    /**
     * 地域权重
     */
    private BigDecimal weightRegion;

    /**
     * 专业度权重
     */
    private BigDecimal weightExpertise;

    /**
     * 优先级 (数字越大优先级越高)
     */
    private Integer priority;

    /**
     * 状态: 1=启用 0=禁用
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

}