package co.yixiang.yshop.module.crm.dal.dataobject.crmstatusconfig;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 客户状态变更日志 DO - 状态流转记录
 *
 * @author yshop
 */
@TableName("crm_customer_status_log")
@KeySequence("crm_customer_status_log_seq")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerStatusLogDO {

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
     * 原状态
     */
    private String oldStatus;

    /**
     * 新状态
     */
    private String newStatus;

    /**
     * 操作人ID
     */
    private Long operatorId;

    /**
     * 操作人姓名
     */
    private String operatorName;

    /**
     * 变更原因
     */
    private String changeReason;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}