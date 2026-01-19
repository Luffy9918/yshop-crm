package co.yixiang.yshop.module.crm.dal.dataobject.crmloanproduct;

import com.baomidou.mybatisplus.annotation.*;
import co.yixiang.yshop.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;

/**
 * 贷款产品 DO - 多城市产品
 *
 * @author yshop
 */
@TableName("crm_loan_product")
@KeySequence("crm_loan_product_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanProductDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品编码
     */
    private String productCode;

    /**
     * 适用城市编码
     */
    private String cityCode;

    /**
     * 适用城市名称
     */
    private String cityName;

    /**
     * 最低额度
     */
    private BigDecimal minAmount;

    /**
     * 最高额度
     */
    private BigDecimal maxAmount;

    /**
     * 最低利率 (如: 0.0450 代表4.5%)
     */
    private BigDecimal minRate;

    /**
     * 最高利率
     */
    private BigDecimal maxRate;

    /**
     * 最短期数 (月)
     */
    private Integer periodMin;

    /**
     * 最长期数 (月)
     */
    private Integer periodMax;

    /**
     * 还款方式 (JSON数组: ["等额本息","先息后本"])
     */
    private String repaymentMethods;

    /**
     * 客户资质要求 (JSON格式)
     */
    private String requirements;

    /**
     * 放款银行
     */
    private String bankName;

    /**
     * 产品特点
     */
    private String productFeatures;

    /**
     * 所需材料
     */
    private String requiredDocuments;

    /**
     * 审批时效 (小时)
     */
    private Integer processingTime;

    /**
     * 状态: 1=上架 0=下架
     */
    private Integer status;

}