package co.yixiang.yshop.module.crm.controller.admin.crmstatusconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 状态变更日志创建 Request VO")
@Data
public class StatusLogSaveReqVO {

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "客户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "客户ID不能为空")
    private Long customerId;

    @Schema(description = "原状态", example = "new")
    private String oldStatus;

    @Schema(description = "新状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "contacted")
    private String newStatus;

    @Schema(description = "操作人ID", example = "1")
    private Long operatorId;

    @Schema(description = "操作人姓名", example = "张三")
    private String operatorName;

    @Schema(description = "变更原因", example = "客户已联系")
    private String changeReason;

}