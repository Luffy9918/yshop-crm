package co.yixiang.yshop.module.crm.controller.admin.crmstatistics;

import co.yixiang.yshop.framework.common.pojo.CommonResult;
import co.yixiang.yshop.module.crm.controller.admin.crmstatistics.vo.*;
import co.yixiang.yshop.module.crm.service.crmstatistics.CrmStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static co.yixiang.yshop.framework.common.pojo.CommonResult.success;

/**
 * 统计 Controller
 *
 * @author yshop
 */
@Tag(name = "管理后台 - 统计")
@RestController
@RequestMapping("/crm/statistics")
@Validated
public class CrmStatisticsController {

    @Resource
    private CrmStatisticsService crmStatisticsService;

    @GetMapping("/overview")
    @Operation(summary = "获取统计总览")
    @PreAuthorize("@ss.hasPermission('crm:statistics:query')")
    public CommonResult<StatisticsOverviewVO> getOverview() {
        return success(crmStatisticsService.getOverview());
    }

    @GetMapping("/customer-trend")
    @Operation(summary = "获取客户趋势")
    @PreAuthorize("@ss.hasPermission('crm:statistics:query')")
    public CommonResult<List<CustomerTrendVO>> getCustomerTrend(
            @Parameter(description = "开始日期", example = "2024-01-01")
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @Parameter(description = "结束日期", example = "2024-01-31")
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return success(crmStatisticsService.getCustomerTrend(startDate, endDate));
    }

    @GetMapping("/status-distribute")
    @Operation(summary = "获取状态分布")
    @PreAuthorize("@ss.hasPermission('crm:statistics:query')")
    public CommonResult<List<StatusDistributeVO>> getStatusDistribute() {
        return success(crmStatisticsService.getStatusDistribute());
    }

    @GetMapping("/data-source-roi")
    @Operation(summary = "获取数据源ROI")
    @PreAuthorize("@ss.hasPermission('crm:statistics:query')")
    public CommonResult<List<DataSourceRoiVO>> getDataSourceRoi() {
        return success(crmStatisticsService.getDataSourceRoi());
    }

}