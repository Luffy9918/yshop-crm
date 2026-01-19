package co.yixiang.yshop.module.crm.controller.admin.crmassign.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - 批量客户分配 Request VO")
@Data
public class CustomerBatchAssignReqVO {

    @Schema(description = "客户ID列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "客户ID列表不能为空")
    private List<Long> customerIds;

    @Schema(description = "业务员ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "业务员ID不能为空")
    private Long staffId;

    @Schema(description = "分配原因", example = "批量重新分配")
    private String reason;

}