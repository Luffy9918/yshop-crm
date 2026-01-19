package co.yixiang.yshop.module.crm.controller.admin.crmstatusconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(description = "管理后台 - 客户状态配置创建/更新 Request VO")
@Data
public class StatusConfigSaveReqVO {

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "状态编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "new")
    @NotBlank(message = "状态编码不能为空")
    private String statusCode;

    @Schema(description = "状态名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "新线索")
    @NotBlank(message = "状态名称不能为空")
    private String statusName;

    @Schema(description = "状态类型", example = "normal")
    private String statusType;

    @Schema(description = "颜色类型", example = "primary")
    private String colorType;

    @Schema(description = "CSS样式类")
    private String cssClass;

    @Schema(description = "排序号", example = "1")
    private Integer sortOrder;

    @Schema(description = "是否启用", example = "1")
    private Integer isEnabled;

}