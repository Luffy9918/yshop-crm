package co.yixiang.yshop.module.crm.controller.admin.crmdatasource.vo;

import co.yixiang.yshop.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static co.yixiang.yshop.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 数据源分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DataSourcePageReqVO extends PageParam {

    @Schema(description = "数据源编码", example = "A+G11.9")
    private String sourceCode;

    @Schema(description = "数据源名称", example = "A+渠道-11月数据")
    private String sourceName;

    @Schema(description = "所属渠道", example = "A+")
    private String channel;

    @Schema(description = "地区", example = "上海")
    private String region;

    @Schema(description = "状态", example = "1")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDate[] createTime;

}