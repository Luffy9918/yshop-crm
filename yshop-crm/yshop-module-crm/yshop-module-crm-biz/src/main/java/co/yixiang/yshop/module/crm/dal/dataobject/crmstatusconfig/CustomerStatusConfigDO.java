package co.yixiang.yshop.module.crm.dal.dataobject.crmstatusconfig;

import com.baomidou.mybatisplus.annotation.*;
import co.yixiang.yshop.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

/**
 * 客户状态配置 DO - 12种状态管理
 *
 * @author yshop
 */
@TableName("crm_customer_status_config")
@KeySequence("crm_customer_status_config_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerStatusConfigDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 状态编码
     */
    private String statusCode;

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 状态类型: normal=普通状态 special=特殊状态
     */
    private String statusType;

    /**
     * 颜色类型: primary/success/warning/danger/info
     */
    private String colorType;

    /**
     * CSS样式类
     */
    private String cssClass;

    /**
     * 排序号
     */
    private Integer sortOrder;

    /**
     * 是否启用: 1=启用 0=禁用
     */
    private Integer isEnabled;

}