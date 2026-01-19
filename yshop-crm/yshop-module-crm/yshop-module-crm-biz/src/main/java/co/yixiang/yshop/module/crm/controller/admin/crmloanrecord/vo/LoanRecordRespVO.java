package co.yixiang.yshop.module.crm.controller.admin.crmloanrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 放款记录 Response VO")
@Data
public class LoanRecordRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "客户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long customerId;

    @Schema(description = "客户姓名", example = "张三")
    private String customerName;

    @Schema(description = "客户手机号", example = "13800138000")
    private String customerPhone;

    @Schema(description = "产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long productId;

    @Schema(description = "产品名称", example = "上海经营贷")
    private String productName;

    @Schema(description = "放款额度", requiredMode = Schema.RequiredMode.REQUIRED, example = "500000")
    private BigDecimal loanAmount;

    @Schema(description = "利率", requiredMode = Schema.RequiredMode.REQUIRED, example = "0.0550")
    private BigDecimal loanRate;

    @Schema(description = "还款期数(月)", requiredMode = Schema.RequiredMode.REQUIRED, example = "36")
    private Integer loanPeriod;

    @Schema(description = "还款方式", example = "等额本息")
    private String repaymentMethod;

    @Schema(description = "放款银行", example = "工商银行")
    private String bankName;

    @Schema(description = "放款日期", example = "2024-01-15")
    private LocalDate disbursementDate;

    @Schema(description = "中介服务费", example = "5000")
    private BigDecimal commissionAmount;

    @Schema(description = "服务费是否已支付", example = "0")
    private Integer commissionPaid;

    @Schema(description = "渠道专员ID", example = "1")
    private Long channelStaffId;

    @Schema(description = "渠道专员姓名", example = "李四")
    private String channelStaffName;

    @Schema(description = "合同编号", example = "HT202401001")
    private String contractNo;

    @Schema(description = "备注", example = "客户资质良好")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime updateTime;

}