package co.yixiang.yshop.module.crm.controller.admin.crmassign.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 自动分配 Request VO")
@Data
public class CustomerAutoAssignReqVO {

    @Schema(description = "客户ID", example = "1")
    private Long customerId;

    @Schema(description = "条件类型", example = "city")
    private String conditionType;

    @Schema(description = "条件值", example = "SH001")
    private String conditionValue;

}