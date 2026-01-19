package co.yixiang.yshop.module.crm.controller.admin.crmloanproduct;

import co.yixiang.yshop.framework.common.pojo.CommonResult;
import co.yixiang.yshop.framework.common.pojo.PageParam;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;
import co.yixiang.yshop.framework.excel.core.util.ExcelUtils;
import co.yixiang.yshop.framework.apilog.core.annotation.ApiAccessLog;
import co.yixiang.yshop.module.crm.controller.admin.crmloanproduct.vo.LoanProductPageReqVO;
import co.yixiang.yshop.module.crm.controller.admin.crmloanproduct.vo.LoanProductRespVO;
import co.yixiang.yshop.module.crm.controller.admin.crmloanproduct.vo.LoanProductSaveReqVO;
import co.yixiang.yshop.module.crm.dal.dataobject.crmloanproduct.LoanProductDO;
import co.yixiang.yshop.module.crm.service.crmloanproduct.LoanProductService;
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

@Tag(name = "管理后台 - 贷款产品")
@RestController
@RequestMapping("/crm/loan-product")
@Validated
public class LoanProductController {

    @Resource
    private LoanProductService loanProductService;

    @PostMapping("/create")
    @Operation(summary = "创建贷款产品")
    @PreAuthorize("@ss.hasPermission('crm:loan-product:create')")
    public CommonResult<Long> createLoanProduct(@Valid @RequestBody LoanProductSaveReqVO createReqVO) {
        return success(loanProductService.createLoanProduct(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新贷款产品")
    @PreAuthorize("@ss.hasPermission('crm:loan-product:update')")
    public CommonResult<Boolean> updateLoanProduct(@Valid @RequestBody LoanProductSaveReqVO updateReqVO) {
        loanProductService.updateLoanProduct(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除贷款产品")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:loan-product:delete')")
    public CommonResult<Boolean> deleteLoanProduct(@RequestParam("id") Long id) {
        loanProductService.deleteLoanProduct(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得贷款产品")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('crm:loan-product:query')")
    public CommonResult<LoanProductRespVO> getLoanProduct(@RequestParam("id") Long id) {
        LoanProductDO loanProduct = loanProductService.getLoanProduct(id);
        return success(BeanUtils.toBean(loanProduct, LoanProductRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得贷款产品分页")
    @PreAuthorize("@ss.hasPermission('crm:loan-product:query')")
    public CommonResult<PageResult<LoanProductRespVO>> getLoanProductPage(@Valid LoanProductPageReqVO pageReqVO) {
        PageResult<LoanProductDO> pageResult = loanProductService.getLoanProductPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, LoanProductRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获取所有上架的贷款产品")
    @PreAuthorize("@ss.hasPermission('crm:loan-product:query')")
    public CommonResult<List<LoanProductRespVO>> getAvailableLoanProducts() {
        List<LoanProductDO> list = loanProductService.getAvailableLoanProducts();
        return success(BeanUtils.toBean(list, LoanProductRespVO.class));
    }

    @GetMapping("/city/{cityCode}")
    @Operation(summary = "按城市获取可用的贷款产品")
    @Parameter(name = "cityCode", description = "城市编码", required = true, example = "SH001")
    @PreAuthorize("@ss.hasPermission('crm:loan-product:query')")
    public CommonResult<List<LoanProductRespVO>> getLoanProductsByCity(@PathVariable("cityCode") String cityCode) {
        List<LoanProductDO> list = loanProductService.getLoanProductsByCity(cityCode);
        return success(BeanUtils.toBean(list, LoanProductRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出贷款产品 Excel")
    @PreAuthorize("@ss.hasPermission('crm:loan-product:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportLoanProductExcel(@Valid LoanProductPageReqVO pageReqVO,
                                        HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<LoanProductDO> list = loanProductService.getLoanProductPage(pageReqVO).getList();
        ExcelUtils.write(response, "贷款产品.xls", "数据", LoanProductRespVO.class,
                BeanUtils.toBean(list, LoanProductRespVO.class));
    }

}