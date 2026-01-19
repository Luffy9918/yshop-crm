package co.yixiang.yshop.module.crm.controller.admin.crmloanrecord.vo;

import co.yixiang.yshop.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static co.yixiang.yshop.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 放款记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LoanRecordPageReqVO extends PageParam {

    @Schema(description = "客户ID", example = "1")
    private Long customerId;

    @Schema(description = "客户姓名", example = "张三")
    private String customerName;

    @Schema(description = "客户手机号", example = "13800138000")
    private String customerPhone;

    @Schema(description = "产品ID", example = "1")
    private Long productId;

    @Schema(description = "渠道专员ID", example = "1")
    private Long channelStaffId;

    @Schema(description = "合同编号", example = "HT202401001")
    private String contractNo;

    @Schema(description = "服务费是否已支付", example = "1")
    private Integer commissionPaid;

    @Schema(description = "放款日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate[] disbursementDate;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}