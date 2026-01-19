package co.yixiang.yshop.module.crm.controller.admin.crmassignrule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 分配规则 Response VO")
@Data
public class AssignRuleRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "规则名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "上海地区规则")
    private String ruleName;

    @Schema(description = "条件类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "city")
    private String conditionType;

    @Schema(description = "条件值", example = "SH001")
    private String conditionValue;

    @Schema(description = "分配类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "staff")
    private String assignType;

    @Schema(description = "目标业务员ID", example = "1")
    private Long targetStaffId;

    @Schema(description = "转化率权重", example = "0.4")
    private BigDecimal weightConversion;

    @Schema(description = "工作量权重", example = "0.3")
    private BigDecimal weightWorkload;

    @Schema(description = "地域权重", example = "0.2")
    private BigDecimal weightRegion;

    @Schema(description = "专业度权重", example = "0.1")
    private BigDecimal weightExpertise;

    @Schema(description = "优先级", example = "10")
    private Integer priority;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "备注", example = "优先分配给资深业务员")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime updateTime;

}