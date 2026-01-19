package co.yixiang.yshop.module.crm.controller.admin.crmstatusconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 客户状态配置 Response VO")
@Data
public class StatusConfigRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "状态编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "new")
    private String statusCode;

    @Schema(description = "状态名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "新线索")
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

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime updateTime;

}