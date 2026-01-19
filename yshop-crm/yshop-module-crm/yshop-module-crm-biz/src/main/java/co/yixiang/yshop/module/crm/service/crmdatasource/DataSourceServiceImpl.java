package co.yixiang.yshop.module.crm.service.crmdatasource;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.module.crm.controller.admin.crmdatasource.vo.DataSourceImportVO;
import co.yixiang.yshop.module.crm.controller.admin.crmdatasource.vo.DataSourcePageReqVO;
import co.yixiang.yshop.module.crm.controller.admin.crmdatasource.vo.DataSourceSaveReqVO;
import co.yixiang.yshop.module.crm.dal.dataobject.crmdatasource.DataSourceDO;
import co.yixiang.yshop.module.crm.dal.mysql.crmdatasource.DataSourceMapper;
import co.yixiang.yshop.module.crm.service.crmdatasource.DataSourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.BeanUtils;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.module.crm.enums.ErrorCodeConstants.*;

/**
 * 数据源 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
@Slf4j
public class DataSourceServiceImpl implements DataSourceService {

    @Resource
    private DataSourceMapper dataSourceMapper;

    @Override
    public Long createDataSource(@Valid DataSourceSaveReqVO createReqVO) {
        // 校验编码唯一性
        validateSourceCodeUnique(null, createReqVO.getSourceCode());

        // 插入
        DataSourceDO dataSource = DataSourceDO.builder()
                .sourceCode(createReqVO.getSourceCode())
                .sourceName(createReqVO.getSourceName())
                .channel(createReqVO.getChannel())
                .acquireCost(createReqVO.getAcquireCost())
                .acquireTime(createReqVO.getAcquireTime())
                .region(createReqVO.getRegion())
                .remark(createReqVO.getRemark())
                .status(createReqVO.getStatus() != null ? createReqVO.getStatus() : 1)
                .build();
        dataSourceMapper.insert(dataSource);
        return dataSource.getId();
    }

    @Override
    public void updateDataSource(@Valid DataSourceSaveReqVO updateReqVO) {
        // 校验存在
        validateDataSourceExists(updateReqVO.getId());
        // 校验编码唯一性
        validateSourceCodeUnique(updateReqVO.getId(), updateReqVO.getSourceCode());

        // 更新
        DataSourceDO updateObj = DataSourceDO.builder()
                .id(updateReqVO.getId())
                .sourceCode(updateReqVO.getSourceCode())
                .sourceName(updateReqVO.getSourceName())
                .channel(updateReqVO.getChannel())
                .acquireCost(updateReqVO.getAcquireCost())
                .acquireTime(updateReqVO.getAcquireTime())
                .region(updateReqVO.getRegion())
                .remark(updateReqVO.getRemark())
                .status(updateReqVO.getStatus())
                .build();
        dataSourceMapper.updateById(updateObj);
    }

    @Override
    public void deleteDataSource(Long id) {
        // 校验存在
        validateDataSourceExists(id);
        // 删除
        dataSourceMapper.deleteById(id);
    }

    @Override
    public DataSourceDO getDataSource(Long id) {
        return dataSourceMapper.selectById(id);
    }

    @Override
    public PageResult<DataSourceDO> getDataSourcePage(DataSourcePageReqVO pageReqVO) {
        return dataSourceMapper.selectPage(pageReqVO);
    }

    @Override
    public java.util.List<DataSourceDO> getEnabledDataSources() {
        return dataSourceMapper.selectList("status", 1);
    }

    @Override
    public void validateDataSourceExists(Long id) {
        if (dataSourceMapper.selectById(id) == null) {
            throw exception(DATA_SOURCE_NOT_EXISTS);
        }
    }

    /**
     * 校验数据源编码唯一性
     */
    private void validateSourceCodeUnique(Long id, String sourceCode) {
        DataSourceDO existing = dataSourceMapper.selectOne("source_code", sourceCode);
        if (existing == null) {
            return;
        }
        // 如果是更新，检查是否是当前记录
        if (id != null && existing.getId().equals(id)) {
            return;
        }
        throw exception(DATA_SOURCE_CODE_EXISTS);
    }

    @Override
    public Integer batchImportDataSources(java.util.List<DataSourceImportVO> importList) {
        int successCount = 0;
        for (DataSourceImportVO importVO : importList) {
            try {
                // 跳过空行
                if (importVO.getSourceCode() == null || importVO.getSourceCode().trim().isEmpty()) {
                    continue;
                }
                // 检查编码是否已存在，存在则跳过
                DataSourceDO existing = dataSourceMapper.selectOne("source_code", importVO.getSourceCode());
                if (existing != null) {
                    log.warn("数据源编码已存在，跳过: {}", importVO.getSourceCode());
                    continue;
                }
                // 插入新数据
                DataSourceDO dataSource = DataSourceDO.builder()
                        .sourceCode(importVO.getSourceCode())
                        .sourceName(importVO.getSourceName())
                        .channel(importVO.getChannel())
                        .acquireCost(importVO.getAcquireCost())
                        .acquireTime(importVO.getAcquireTime())
                        .region(importVO.getRegion())
                        .remark(importVO.getRemark())
                        .status(1)
                        .build();
                dataSourceMapper.insert(dataSource);
                successCount++;
            } catch (Exception e) {
                log.error("导入数据源失败: {}", importVO.getSourceCode(), e);
            }
        }
        return successCount;
    }
}