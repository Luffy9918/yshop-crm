package co.yixiang.yshop.module.crm.service.crmstatistics;

import co.yixiang.yshop.module.crm.controller.admin.crmstatistics.vo.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 统计 Service 接口
 *
 * @author yshop
 */
public interface CrmStatisticsService {

    /**
     * 获取统计总览
     *
     * @return 统计总览数据
     */
    StatisticsOverviewVO getOverview();

    /**
     * 获取客户趋势数据
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 客户趋势列表
     */
    List<CustomerTrendVO> getCustomerTrend(LocalDate startDate, LocalDate endDate);

    /**
     * 获取状态分布数据
     *
     * @return 状态分布列表
     */
    List<StatusDistributeVO> getStatusDistribute();

    /**
     * 获取数据源ROI数据
     *
     * @return 数据源ROI列表
     */
    List<DataSourceRoiVO> getDataSourceRoi();

}