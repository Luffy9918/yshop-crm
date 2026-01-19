package co.yixiang.yshop.module.crm.controller.admin.crmstatistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 统计总览 Response VO
 *
 * @author yshop
 */
@Schema(description = "管理后台 - 统计总览 Response VO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsOverviewVO {

    @Schema(description = "总客户数", example = "5234")
    private Long totalCustomers;

    @Schema(description = "今日新增客户", example = "45")
    private Long newCustomersToday;

    @Schema(description = "跟进中客户数", example = "1234")
    private Long followingCustomers;

    @Schema(description = "本月成交数", example = "56")
    private Long dealsThisMonth;

    @Schema(description = "转化率(%)", example = "12.5")
    private BigDecimal conversionRate;

}