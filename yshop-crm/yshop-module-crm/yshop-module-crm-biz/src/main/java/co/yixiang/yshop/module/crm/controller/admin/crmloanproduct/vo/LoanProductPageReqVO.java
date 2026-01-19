package co.yixiang.yshop.module.crm.controller.admin.crmloanproduct.vo;

import co.yixiang.yshop.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static co.yixiang.yshop.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 贷款产品分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LoanProductPageReqVO extends PageParam {

    @Schema(description = "产品名称", example = "上海经营贷")
    private String productName;

    @Schema(description = "产品编码", example = "SH-JYD-001")
    private String productCode;

    @Schema(description = "城市编码", example = "SH001")
    private String cityCode;

    @Schema(description = "最低额度", example = "100000")
    private BigDecimal minAmount;

    @Schema(description = "最高额度", example = "5000000")
    private BigDecimal maxAmount;

    @Schema(description = "状态", example = "1")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}