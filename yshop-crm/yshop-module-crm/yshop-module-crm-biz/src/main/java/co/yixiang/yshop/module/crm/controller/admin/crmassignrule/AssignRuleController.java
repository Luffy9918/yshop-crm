package co.yixiang.yshop.module.crm.controller.admin.crmassignrule;

import co.yixiang.yshop.framework.common.pojo.CommonResult;
import co.yixiang.yshop.framework.common.pojo.PageParam;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;
import co.yixiang.yshop.framework.excel.core.util.ExcelUtils;
import co.yixiang.yshop.framework.apilog.core.annotation.ApiAccessLog;
import co.yixiang.yshop.module.crm.controller.admin.crmassignrule.vo.AssignRulePageReqVO;
import co.yixiang.yshop.module.crm.controller.admin.crmassignrule.vo.AssignRuleRespVO;
import co.yixiang.yshop.module.crm.controller.admin.crmassignrule.vo.AssignRuleSaveReqVO;
import co.yixiang.yshop.module.crm.dal.dataobject.crmassignrule.AssignRuleDO;
import co.yixiang.yshop.module.crm.service.crmassignrule.AssignRuleService;
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

@Tag(name = "管理后台 - 分配规则")
@RestController
@RequestMapping("/crm/assign-rule")
@Validated
public class AssignRuleController {

    @Resource
    private AssignRuleService assignRuleService;

    @PostMapping("/create")
    @Operation(summary = "创建分配规则")
    @PreAuthorize("@ss.hasPermission('crm:assign-rule:create')")
    public CommonResult<Long> createAssignRule(@Valid @RequestBody AssignRuleSaveReqVO createReqVO) {
        return success(assignRuleService.createAssignRule(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新分配规则")
    @PreAuthorize("@ss.hasPermission('crm:assign-rule:update')")
    public CommonResult<Boolean> updateAssignRule(@Valid @RequestBody AssignRuleSaveReqVO updateReqVO) {
        assignRuleService.updateAssignRule(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除分配规则")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:assign-rule:delete')")
    public CommonResult<Boolean> deleteAssignRule(@RequestParam("id") Long id) {
        assignRuleService.deleteAssignRule(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得分配规则")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('crm:assign-rule:query')")
    public CommonResult<AssignRuleRespVO> getAssignRule(@RequestParam("id") Long id) {
        AssignRuleDO assignRule = assignRuleService.getAssignRule(id);
        return success(BeanUtils.toBean(assignRule, AssignRuleRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得分配规则分页")
    @PreAuthorize("@ss.hasPermission('crm:assign-rule:query')")
    public CommonResult<PageResult<AssignRuleRespVO>> getAssignRulePage(@Valid AssignRulePageReqVO pageReqVO) {
        PageResult<AssignRuleDO> pageResult = assignRuleService.getAssignRulePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AssignRuleRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获取所有启用的分配规则")
    @PreAuthorize("@ss.hasPermission('crm:assign-rule:query')")
    public CommonResult<List<AssignRuleRespVO>> getEnabledAssignRules() {
        List<AssignRuleDO> list = assignRuleService.getEnabledAssignRules();
        return success(BeanUtils.toBean(list, AssignRuleRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出分配规则 Excel")
    @PreAuthorize("@ss.hasPermission('crm:assign-rule:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportAssignRuleExcel(@Valid AssignRulePageReqVO pageReqVO,
                                       HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AssignRuleDO> list = assignRuleService.getAssignRulePage(pageReqVO).getList();
        ExcelUtils.write(response, "分配规则.xls", "数据", AssignRuleRespVO.class,
                BeanUtils.toBean(list, AssignRuleRespVO.class));
    }

}