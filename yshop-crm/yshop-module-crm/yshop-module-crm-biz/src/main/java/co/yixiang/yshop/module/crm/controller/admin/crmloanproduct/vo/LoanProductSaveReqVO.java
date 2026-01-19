package co.yixiang.yshop.module.crm.controller.admin.crmloanproduct.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "管理后台 - 贷款产品创建/更新 Request VO")
@Data
public class LoanProductSaveReqVO {

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "产品名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "上海经营贷")
    @NotBlank(message = "产品名称不能为空")
    private String productName;

    @Schema(description = "产品编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "SH-JYD-001")
    @NotBlank(message = "产品编码不能为空")
    private String productCode;

    @Schema(description = "城市编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "SH001")
    @NotBlank(message = "城市编码不能为空")
    private String cityCode;

    @Schema(description = "城市名称", example = "上海")
    private String cityName;

    @Schema(description = "最低额度", example = "100000")
    private BigDecimal minAmount;

    @Schema(description = "最高额度", example = "5000000")
    private BigDecimal maxAmount;

    @Schema(description = "最低利率", example = "0.0450")
    private BigDecimal minRate;

    @Schema(description = "最高利率", example = "0.0850")
    private BigDecimal maxRate;

    @Schema(description = "最短期数(月)", example = "3")
    private Integer periodMin;

    @Schema(description = "最长期数(月)", example = "60")
    private Integer periodMax;

    @Schema(description = "还款方式(JSON数组)", example = "[\"等额本息\",\"先息后本\"]")
    private String repaymentMethods;

    @Schema(description = "客户资质要求(JSON格式)")
    private String requirements;

    @Schema(description = "放款银行", example = "工商银行")
    private String bankName;

    @Schema(description = "产品特点")
    private String productFeatures;

    @Schema(description = "所需材料")
    private String requiredDocuments;

    @Schema(description = "审批时效(小时)", example = "24")
    private Integer processingTime;

    @Schema(description = "状态", example = "1")
    private Integer status;

}