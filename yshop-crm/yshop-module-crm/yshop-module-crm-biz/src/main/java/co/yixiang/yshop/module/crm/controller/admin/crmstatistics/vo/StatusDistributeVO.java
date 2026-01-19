package co.yixiang.yshop.module.crm.controller.admin.crmstatistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 状态分布 Response VO
 *
 * @author yshop
 */
@Schema(description = "管理后台 - 状态分布 Response VO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusDistributeVO {

    @Schema(description = "状态名称", example = "跟进中")
    private String status;

    @Schema(description = "状态编码", example = "following")
    private String statusCode;

    @Schema(description = "客户数量", example = "1500")
    private Long count;

    @Schema(description = "百分比(%)", example = "28.6")
    private BigDecimal percent;

}