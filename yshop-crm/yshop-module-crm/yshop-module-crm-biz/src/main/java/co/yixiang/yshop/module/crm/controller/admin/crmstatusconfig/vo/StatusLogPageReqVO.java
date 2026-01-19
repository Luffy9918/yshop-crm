package co.yixiang.yshop.module.crm.controller.admin.crmstatusconfig.vo;

import co.yixiang.yshop.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static co.yixiang.yshop.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 状态变更日志分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StatusLogPageReqVO extends PageParam {

    @Schema(description = "客户ID", example = "1")
    private Long customerId;

    @Schema(description = "原状态", example = "new")
    private String oldStatus;

    @Schema(description = "新状态", example = "contacted")
    private String newStatus;

    @Schema(description = "操作人ID", example = "1")
    private Long operatorId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}