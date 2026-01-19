package co.yixiang.yshop.module.crm.controller.admin.crmstatistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 数据源ROI Response VO
 *
 * @author yshop
 */
@Schema(description = "管理后台 - 数据源ROI Response VO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataSourceRoiVO {

    @Schema(description = "数据源ID", example = "1")
    private Long dataSourceId;

    @Schema(description = "数据源名称", example = "A+G11.9")
    private String dataSourceName;

    @Schema(description = "客户数量", example = "1500")
    private Long customerCount;

    @Schema(description = "成交数量", example = "200")
    private Long dealCount;

    @Schema(description = "成交金额", example = "5000000")
    private BigDecimal dealAmount;

    @Schema(description = "获客成本", example = "15000")
    private BigDecimal acquireCost;

    @Schema(description = "ROI(倍)", example = "32.3")
    private BigDecimal roi;

}