package co.yixiang.yshop.module.crm.controller.admin.crmassign.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 客户分配 Request VO")
@Data
public class CustomerAssignReqVO {

    @Schema(description = "客户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "客户ID不能为空")
    private Long customerId;

    @Schema(description = "业务员ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "业务员ID不能为空")
    private Long staffId;

    @Schema(description = "分配原因", example = "按地区自动分配")
    private String reason;

}