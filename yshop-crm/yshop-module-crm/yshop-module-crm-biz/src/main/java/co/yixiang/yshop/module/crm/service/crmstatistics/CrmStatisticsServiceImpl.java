package co.yixiang.yshop.module.crm.service.crmstatistics;

import cn.hutool.core.date.DateUtil;
import co.yixiang.yshop.module.crm.controller.admin.crmstatistics.vo.*;
import co.yixiang.yshop.module.crm.dal.dataobject.crmcustomer.CrmCustomerDO;
import co.yixiang.yshop.module.crm.dal.dataobject.crmdatasource.DataSourceDO;
import co.yixiang.yshop.module.crm.dal.mysql.crmcustomer.CrmCustomerMapper;
import co.yixiang.yshop.module.crm.dal.mysql.crmdatasource.DataSourceMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 统计 Service 实现类
 *
 * @author yshop
 */
@Service
@Slf4j
public class CrmStatisticsServiceImpl implements CrmStatisticsService {

    @Resource
    private CrmCustomerMapper customerMapper;

    @Resource
    private DataSourceMapper dataSourceMapper;

    @Override
    public StatisticsOverviewVO getOverview() {
        // 1. 总客户数
        Long totalCustomers = customerMapper.selectCount(null);

        // 2. 今日新增客户
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        Long newCustomersToday = customerMapper.selectCount(new LambdaQueryWrapper<CrmCustomerDO>()
                .ge(CrmCustomerDO::getCreateTime, todayStart)
                .le(CrmCustomerDO::getCreateTime, todayEnd));

        // 3. 跟进中客户数 (假设 dealStatus != 1 都是跟进中)
        Long followingCustomers = customerMapper.selectCount(new LambdaQueryWrapper<CrmCustomerDO>()
                .ne(CrmCustomerDO::getDealStatus, 1));

        // 4. 本月成交数
        LocalDateTime monthStart = LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIN);
        Long dealsThisMonth = customerMapper.selectCount(new LambdaQueryWrapper<CrmCustomerDO>()
                .eq(CrmCustomerDO::getDealStatus, 1)
                .ge(CrmCustomerDO::getDealTime, monthStart));

        // 5. 转化率 = 成交数 / 总客户数
        Long totalDeals = customerMapper.selectCount(new LambdaQueryWrapper<CrmCustomerDO>()
                .eq(CrmCustomerDO::getDealStatus, 1));
        BigDecimal conversionRate = totalCustomers > 0
                ? BigDecimal.valueOf(totalDeals).multiply(BigDecimal.valueOf(100))
                        .divide(BigDecimal.valueOf(totalCustomers), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        return StatisticsOverviewVO.builder()
                .totalCustomers(totalCustomers)
                .newCustomersToday(newCustomersToday)
                .followingCustomers(followingCustomers)
                .dealsThisMonth(dealsThisMonth)
                .conversionRate(conversionRate)
                .build();
    }

    @Override
    public List<CustomerTrendVO> getCustomerTrend(LocalDate startDate, LocalDate endDate) {
        List<CustomerTrendVO> trendList = new ArrayList<>();

        // 如果没有指定日期范围，默认查询最近30天
        if (startDate == null) {
            startDate = LocalDate.now().minusDays(29);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        // 遍历日期范围，统计每天的新增和成交
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            LocalDateTime dayStart = LocalDateTime.of(currentDate, LocalTime.MIN);
            LocalDateTime dayEnd = LocalDateTime.of(currentDate, LocalTime.MAX);

            // 当天新增客户数
            Long newCount = customerMapper.selectCount(new LambdaQueryWrapper<CrmCustomerDO>()
                    .ge(CrmCustomerDO::getCreateTime, dayStart)
                    .le(CrmCustomerDO::getCreateTime, dayEnd));

            // 当天成交客户数
            Long dealCount = customerMapper.selectCount(new LambdaQueryWrapper<CrmCustomerDO>()
                    .eq(CrmCustomerDO::getDealStatus, 1)
                    .ge(CrmCustomerDO::getDealTime, dayStart)
                    .le(CrmCustomerDO::getDealTime, dayEnd));

            trendList.add(CustomerTrendVO.builder()
                    .date(currentDate)
                    .newCustomerCount(newCount)
                    .dealCount(dealCount)
                    .build());

            currentDate = currentDate.plusDays(1);
        }

        return trendList;
    }

    @Override
    public List<StatusDistributeVO> getStatusDistribute() {
        // 获取总客户数
        Long totalCustomers = customerMapper.selectCount(null);

        // 按成交状态分组统计
        List<StatusDistributeVO> distributeList = new ArrayList<>();

        // 未成交
        Long notDealCount = customerMapper.selectCount(new LambdaQueryWrapper<CrmCustomerDO>()
                .eq(CrmCustomerDO::getDealStatus, 0));
        BigDecimal notDealPercent = totalCustomers > 0
                ? BigDecimal.valueOf(notDealCount).multiply(BigDecimal.valueOf(100))
                        .divide(BigDecimal.valueOf(totalCustomers), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        distributeList.add(StatusDistributeVO.builder()
                .statusCode("not_deal")
                .status("未成交")
                .count(notDealCount)
                .percent(notDealPercent)
                .build());

        // 已成交
        Long dealCount = customerMapper.selectCount(new LambdaQueryWrapper<CrmCustomerDO>()
                .eq(CrmCustomerDO::getDealStatus, 1));
        BigDecimal dealPercent = totalCustomers > 0
                ? BigDecimal.valueOf(dealCount).multiply(BigDecimal.valueOf(100))
                        .divide(BigDecimal.valueOf(totalCustomers), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        distributeList.add(StatusDistributeVO.builder()
                .statusCode("deal")
                .status("已成交")
                .count(dealCount)
                .percent(dealPercent)
                .build());

        // 按跟进状态细分 (如果需要更详细的分类)
        // 这里可以根据实际需求扩展更多状态分类

        return distributeList.stream()
                .sorted(Comparator.comparing(StatusDistributeVO::getCount).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<DataSourceRoiVO> getDataSourceRoi() {
        // 获取所有数据源
        List<DataSourceDO> dataSources = dataSourceMapper.selectList(null);

        if (dataSources == null || dataSources.isEmpty()) {
            return new ArrayList<>();
        }

        Long totalCustomers = customerMapper.selectCount(null);

        return dataSources.stream()
                .map(dataSource -> {
                    // 统计该数据源的客户数
                    Long customerCount = customerMapper.selectCount(new LambdaQueryWrapper<CrmCustomerDO>()
                            .eq(CrmCustomerDO::getSource, dataSource.getId().intValue()));

                    // 统计该数据源的成交数
                    Long dealCount = customerMapper.selectCount(new LambdaQueryWrapper<CrmCustomerDO>()
                            .eq(CrmCustomerDO::getSource, dataSource.getId().intValue())
                            .eq(CrmCustomerDO::getDealStatus, 1));

                    // 成交金额 (这里简化处理，实际应该从合同表汇总)
                    BigDecimal dealAmount = BigDecimal.ZERO; // TODO: 从合同表汇总

                    // 获客成本
                    BigDecimal acquireCost = dataSource.getAcquireCost() != null
                            ? dataSource.getAcquireCost()
                            : BigDecimal.ZERO;

                    // ROI = (成交金额 - 获客成本) / 获客成本
                    BigDecimal roi = BigDecimal.ZERO;
                    if (acquireCost.compareTo(BigDecimal.ZERO) > 0) {
                        roi = dealAmount.subtract(acquireCost)
                                .divide(acquireCost, 2, RoundingMode.HALF_UP);
                    }

                    return DataSourceRoiVO.builder()
                            .dataSourceId(dataSource.getId())
                            .dataSourceName(dataSource.getSourceName())
                            .customerCount(customerCount)
                            .dealCount(dealCount)
                            .dealAmount(dealAmount)
                            .acquireCost(acquireCost)
                            .roi(roi)
                            .build();
                })
                .filter(vo -> vo.getCustomerCount() > 0) // 只返回有客户的数据源
                .sorted(Comparator.comparing(DataSourceRoiVO::getRoi).reversed())
                .collect(Collectors.toList());
    }

}