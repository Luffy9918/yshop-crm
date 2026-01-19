package co.yixiang.yshop.module.crm.controller.admin.crmdatasource;

import co.yixiang.yshop.framework.common.pojo.CommonResult;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.pojo.PageParam;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;
import co.yixiang.yshop.framework.excel.core.util.ExcelUtils;
import co.yixiang.yshop.framework.apilog.core.annotation.ApiAccessLog;
import co.yixiang.yshop.framework.apilog.core.enums.OperateTypeEnum;
import co.yixiang.yshop.module.crm.controller.admin.crmdatasource.vo.*;
import co.yixiang.yshop.module.crm.dal.dataobject.crmdatasource.DataSourceDO;
import co.yixiang.yshop.module.crm.service.crmdatasource.DataSourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static co.yixiang.yshop.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 数据源")
@RestController
@RequestMapping("/crm/data-source")
@Validated
public class DataSourceController {

    @Resource
    private DataSourceService dataSourceService;

    @PostMapping("/create")
    @Operation(summary = "创建数据源")
    @PreAuthorize("@ss.hasPermission('crm:data-source:create')")
    public CommonResult<Long> createDataSource(@Valid @RequestBody DataSourceSaveReqVO createReqVO) {
        return success(dataSourceService.createDataSource(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新数据源")
    @PreAuthorize("@ss.hasPermission('crm:data-source:update')")
    public CommonResult<Boolean> updateDataSource(@Valid @RequestBody DataSourceSaveReqVO updateReqVO) {
        dataSourceService.updateDataSource(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除数据源")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:data-source:delete')")
    public CommonResult<Boolean> deleteDataSource(@RequestParam("id") Long id) {
        dataSourceService.deleteDataSource(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得数据源")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('crm:data-source:query')")
    public CommonResult<DataSourceRespVO> getDataSource(@RequestParam("id") Long id) {
        DataSourceDO dataSource = dataSourceService.getDataSource(id);
        return success(BeanUtils.toBean(dataSource, DataSourceRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得数据源分页")
    @PreAuthorize("@ss.hasPermission('crm:data-source:query')")
    public CommonResult<PageResult<DataSourceRespVO>> getDataSourcePage(@Valid DataSourcePageReqVO pageReqVO) {
        PageResult<DataSourceDO> pageResult = dataSourceService.getDataSourcePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, DataSourceRespVO.class));
    }

    @GetMapping("/list-all")
    @Operation(summary = "获取所有启用的数据源")
    @PreAuthorize("@ss.hasPermission('crm:data-source:query')")
    public CommonResult<List<DataSourceRespVO>> getEnabledDataSources() {
        List<DataSourceDO> list = dataSourceService.getEnabledDataSources();
        return success(BeanUtils.toBean(list, DataSourceRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出数据源 Excel")
    @PreAuthorize("@ss.hasPermission('crm:data-source:export')")
    @ApiAccessLog(operateType = OperateTypeEnum.EXPORT)
    public void exportDataSourceExcel(@Valid DataSourcePageReqVO pageReqVO,
                                      HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DataSourceDO> list = dataSourceService.getDataSourcePage(pageReqVO).getList();
        ExcelUtils.write(response, "数据源.xls", "数据", DataSourceRespVO.class,
                BeanUtils.toBean(list, DataSourceRespVO.class));
    }

    @PostMapping("/import-excel")
    @Operation(summary = "导入数据源 Excel")
    @PreAuthorize("@ss.hasPermission('crm:data-source:import')")
    @ApiAccessLog(operateType = OperateTypeEnum.CREATE)
    public CommonResult<Integer> importDataSourceExcel(@RequestParam("file") MultipartFile file) throws IOException {
        List<DataSourceImportVO> importList = ExcelUtils.read(file, DataSourceImportVO.class);
        Integer successCount = dataSourceService.batchImportDataSources(importList);
        return success(successCount);
    }

}