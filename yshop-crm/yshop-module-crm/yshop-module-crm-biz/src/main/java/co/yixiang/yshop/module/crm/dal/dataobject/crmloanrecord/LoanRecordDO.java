package co.yixiang.yshop.module.crm.dal.dataobject.crmloanrecord;

import com.baomidou.mybatisplus.annotation.*;
import co.yixiang.yshop.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 放款记录 DO - 渠道专员使用
 *
 * @author yshop
 */
@TableName("crm_loan_record")
@KeySequence("crm_loan_record_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanRecordDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 客户姓名
     */
    private String customerName;

    /**
     * 客户手机号
     */
    private String customerPhone;

    /**
     * 产品ID
     */
    private Long productId;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 放款额度
     */
    private BigDecimal loanAmount;

    /**
     * 利率
     */
    private BigDecimal loanRate;

    /**
     * 还款期数 (月)
     */
    private Integer loanPeriod;

    /**
     * 还款方式
     */
    private String repaymentMethod;

    /**
     * 放款银行
     */
    private String bankName;

    /**
     * 放款日期
     */
    private LocalDate disbursementDate;

    /**
     * 中介服务费
     */
    private BigDecimal commissionAmount;

    /**
     * 服务费是否已支付: 1=是 0=否
     */
    private Integer commissionPaid;

    /**
     * 渠道专员ID
     */
    private Long channelStaffId;

    /**
     * 渠道专员姓名
     */
    private String channelStaffName;

    /**
     * 合同编号
     */
    private String contractNo;

    /**
     * 备注
     */
    private String remark;

}