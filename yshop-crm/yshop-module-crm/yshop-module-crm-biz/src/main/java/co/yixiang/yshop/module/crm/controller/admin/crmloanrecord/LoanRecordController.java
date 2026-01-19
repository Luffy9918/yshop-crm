package co.yixiang.yshop.module.crm.controller.admin.crmloanrecord;

import co.yixiang.yshop.framework.common.pojo.CommonResult;
import co.yixiang.yshop.framework.common.pojo.PageParam;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;
import co.yixiang.yshop.framework.excel.core.util.ExcelUtils;
import co.yixiang.yshop.framework.apilog.core.annotation.ApiAccessLog;
import co.yixiang.yshop.module.crm.controller.admin.crmloanrecord.vo.LoanRecordPageReqVO;
import co.yixiang.yshop.module.crm.controller.admin.crmloanrecord.vo.LoanRecordRespVO;
import co.yixiang.yshop.module.crm.controller.admin.crmloanrecord.vo.LoanRecordSaveReqVO;
import co.yixiang.yshop.module.crm.dal.dataobject.crmloanrecord.LoanRecordDO;
import co.yixiang.yshop.module.crm.service.crmloanrecord.LoanRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static co.yixiang.yshop.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static co.yixiang.yshop.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 放款记录")
@RestController
@RequestMapping("/crm/loan-record")
@Validated
public class LoanRecordController {

    @Resource
    private LoanRecordService loanRecordService;

    @PostMapping("/create")
    @Operation(summary = "创建放款记录")
    @PreAuthorize("@ss.hasPermission('crm:loan-record:create')")
    public CommonResult<Long> createLoanRecord(@Valid @RequestBody LoanRecordSaveReqVO createReqVO) {
        return success(loanRecordService.createLoanRecord(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新放款记录")
    @PreAuthorize("@ss.hasPermission('crm:loan-record:update')")
    public CommonResult<Boolean> updateLoanRecord(@Valid @RequestBody LoanRecordSaveReqVO updateReqVO) {
        loanRecordService.updateLoanRecord(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除放款记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:loan-record:delete')")
    public CommonResult<Boolean> deleteLoanRecord(@RequestParam("id") Long id) {
        loanRecordService.deleteLoanRecord(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得放款记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('crm:loan-record:query')")
    public CommonResult<LoanRecordRespVO> getLoanRecord(@RequestParam("id") Long id) {
        LoanRecordDO loanRecord = loanRecordService.getLoanRecord(id);
        return success(BeanUtils.toBean(loanRecord, LoanRecordRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得放款记录分页")
    @PreAuthorize("@ss.hasPermission('crm:loan-record:query')")
    public CommonResult<PageResult<LoanRecordRespVO>> getLoanRecordPage(@Valid LoanRecordPageReqVO pageReqVO) {
        PageResult<LoanRecordDO> pageResult = loanRecordService.getLoanRecordPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, LoanRecordRespVO.class));
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "获取客户的放款记录")
    @Parameter(name = "customerId", description = "客户ID", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('crm:loan-record:query')")
    public CommonResult<List<LoanRecordRespVO>> getLoanRecordsByCustomerId(@PathVariable("customerId") Long customerId) {
        List<LoanRecordDO> list = loanRecordService.getLoanRecordsByCustomerId(customerId);
        return success(BeanUtils.toBean(list, LoanRecordRespVO.class));
    }

    @GetMapping("/staff/{staffId}")
    @Operation(summary = "获取渠道专员的放款记录")
    @Parameter(name = "staffId", description = "专员ID", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('crm:loan-record:query')")
    public CommonResult<List<LoanRecordRespVO>> getLoanRecordsByStaffId(@PathVariable("staffId") Long staffId) {
        List<LoanRecordDO> list = loanRecordService.getLoanRecordsByStaffId(staffId);
        return success(BeanUtils.toBean(list, LoanRecordRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出放款记录 Excel")
    @PreAuthorize("@ss.hasPermission('crm:loan-record:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportLoanRecordExcel(@Valid LoanRecordPageReqVO pageReqVO,
                                       HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<LoanRecordDO> list = loanRecordService.getLoanRecordPage(pageReqVO).getList();
        ExcelUtils.write(response, "放款记录.xls", "数据", LoanRecordRespVO.class,
                BeanUtils.toBean(list, LoanRecordRespVO.class));
    }

}