package co.yixiang.yshop.module.crm.controller.admin.crmstatistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 客户趋势 Response VO
 *
 * @author yshop
 */
@Schema(description = "管理后台 - 客户趋势 Response VO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTrendVO {

    @Schema(description = "日期", example = "2024-01-15")
    private LocalDate date;

    @Schema(description = "新增客户数", example = "25")
    private Long newCustomerCount;

    @Schema(description = "成交客户数", example = "5")
    private Long dealCount;

}