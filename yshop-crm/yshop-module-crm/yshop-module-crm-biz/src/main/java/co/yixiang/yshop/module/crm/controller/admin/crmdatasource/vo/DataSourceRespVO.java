package co.yixiang.yshop.module.crm.controller.admin.crmdatasource.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 数据源 Response VO")
@Data
public class DataSourceRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "数据源编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "A+G11.9")
    private String sourceCode;

    @Schema(description = "数据源名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "A+渠道-11月数据")
    private String sourceName;

    @Schema(description = "所属渠道", example = "A+")
    private String channel;

    @Schema(description = "获客成本", example = "5000.00")
    private BigDecimal acquireCost;

    @Schema(description = "获客时间", example = "2024-01-01")
    private LocalDate acquireTime;

    @Schema(description = "地区", example = "上海")
    private String region;

    @Schema(description = "备注", example = "测试数据源")
    private String remark;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime updateTime;

}