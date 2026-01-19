package co.yixiang.yshop.module.crm.service.crmdatasource;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.module.crm.controller.admin.crmdatasource.vo.DataSourceImportVO;
import co.yixiang.yshop.module.crm.controller.admin.crmdatasource.vo.DataSourcePageReqVO;
import co.yixiang.yshop.module.crm.controller.admin.crmdatasource.vo.DataSourceSaveReqVO;
import co.yixiang.yshop.module.crm.dal.dataobject.crmdatasource.DataSourceDO;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 数据源 Service 接口
 *
 * @author yshop
 */
public interface DataSourceService {

    /**
     * 创建数据源
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createDataSource(@Valid DataSourceSaveReqVO createReqVO);

    /**
     * 更新数据源
     *
     * @param updateReqVO 更新信息
     */
    void updateDataSource(@Valid DataSourceSaveReqVO updateReqVO);

    /**
     * 删除数据源
     *
     * @param id 编号
     */
    void deleteDataSource(Long id);

    /**
     * 获得数据源
     *
     * @param id 编号
     * @return 数据源
     */
    DataSourceDO getDataSource(Long id);

    /**
     * 获得数据源分页
     *
     * @param pageReqVO 分页查询
     * @return 数据源分页
     */
    PageResult<DataSourceDO> getDataSourcePage(DataSourcePageReqVO pageReqVO);

    /**
     * 获取所有启用的数据源
     *
     * @return 数据源列表
     */
    List<DataSourceDO> getEnabledDataSources();

    /**
     * 校验数据源是否存在
     *
     * @param id 数据源ID
     */
    void validateDataSourceExists(Long id);

    /**
     * 批量导入数据源
     *
     * @param importList 导入数据列表
     * @return 创建成功的数量
     */
    Integer batchImportDataSources(List<DataSourceImportVO> importList);
}