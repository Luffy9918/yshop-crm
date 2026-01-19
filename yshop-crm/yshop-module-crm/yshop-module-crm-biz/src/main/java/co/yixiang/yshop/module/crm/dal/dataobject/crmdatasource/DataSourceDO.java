package co.yixiang.yshop.module.crm.dal.dataobject.crmdatasource;

import com.baomidou.mybatisplus.annotation.*;
import co.yixiang.yshop.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 数据源 DO - 用于ROI分析
 *
 * @author yshop
 */
@TableName("crm_data_source")
@KeySequence("crm_data_source_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataSourceDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 数据源编码 (如: A+G11.9, ssfq12.3)
     */
    private String sourceCode;

    /**
     * 数据源名称
     */
    private String sourceName;

    /**
     * 所属渠道
     */
    private String channel;

    /**
     * 获客成本
     */
    private BigDecimal acquireCost;

    /**
     * 获客时间
     */
    private LocalDate acquireTime;

    /**
     * 地区
     */
    private String region;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态: 1=启用 0=禁用
     */
    private Integer status;

}