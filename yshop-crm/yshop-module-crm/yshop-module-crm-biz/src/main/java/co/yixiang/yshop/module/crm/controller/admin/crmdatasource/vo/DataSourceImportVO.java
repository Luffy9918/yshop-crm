package co.yixiang.yshop.module.crm.controller.admin.crmdatasource.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 数据源导入 VO
 *
 * @author yshop
 */
@Data
public class DataSourceImportVO {

    @ExcelProperty("数据源编码")
    private String sourceCode;

    @ExcelProperty("数据源名称")
    private String sourceName;

    @ExcelProperty("所属渠道")
    private String channel;

    @ExcelProperty("获客成本")
    private BigDecimal acquireCost;

    @ExcelProperty("获客时间")
    private LocalDate acquireTime;

    @ExcelProperty("地区")
    private String region;

    @ExcelProperty("备注")
    private String remark;

}