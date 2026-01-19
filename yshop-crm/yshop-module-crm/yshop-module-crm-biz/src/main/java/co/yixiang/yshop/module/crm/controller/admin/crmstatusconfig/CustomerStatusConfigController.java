package co.yixiang.yshop.module.crm.controller.admin.crmstatusconfig;

import co.yixiang.yshop.framework.common.pojo.CommonResult;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;
import co.yixiang.yshop.module.crm.controller.admin.crmstatusconfig.vo.StatusConfigSaveReqVO;
import co.yixiang.yshop.module.crm.controller.admin.crmstatusconfig.vo.StatusConfigRespVO;
import co.yixiang.yshop.module.crm.dal.dataobject.crmstatusconfig.CustomerStatusConfigDO;
import co.yixiang.yshop.module.crm.service.crmstatusconfig.CustomerStatusConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static co.yixiang.yshop.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 客户状态配置")
@RestController
@RequestMapping("/crm/customer-status")
@Validated
public class CustomerStatusConfigController {

    @Resource
    private CustomerStatusConfigService customerStatusConfigService;

    @PostMapping("/create")
    @Operation(summary = "创建状态配置")
    @PreAuthorize("@ss.hasPermission('crm:customer-status:create')")
    public CommonResult<Long> createStatusConfig(@Valid @RequestBody StatusConfigSaveReqVO createReqVO) {
        return success(customerStatusConfigService.createStatusConfig(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新状态配置")
    @PreAuthorize("@ss.hasPermission('crm:customer-status:update')")
    public CommonResult<Boolean> updateStatusConfig(@Valid @RequestBody StatusConfigSaveReqVO updateReqVO) {
        customerStatusConfigService.updateStatusConfig(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除状态配置")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:customer-status:delete')")
    public CommonResult<Boolean> deleteStatusConfig(@RequestParam("id") Long id) {
        customerStatusConfigService.deleteStatusConfig(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得状态配置")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('crm:customer-status:query')")
    public CommonResult<StatusConfigRespVO> getStatusConfig(@RequestParam("id") Long id) {
        CustomerStatusConfigDO config = customerStatusConfigService.getStatusConfig(id);
        return success(BeanUtils.toBean(config, StatusConfigRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获取所有状态配置")
    @PreAuthorize("@ss.hasPermission('crm:customer-status:query')")
    public CommonResult<List<StatusConfigRespVO>> getAllStatusConfigs() {
        List<CustomerStatusConfigDO> list = customerStatusConfigService.getAllStatusConfigs();
        return success(BeanUtils.toBean(list, StatusConfigRespVO.class));
    }

    @GetMapping("/type/{statusType}")
    @Operation(summary = "按类型获取状态")
    @Parameter(name = "statusType", description = "状态类型", required = true, example = "normal")
    @PreAuthorize("@ss.hasPermission('crm:customer-status:query')")
    public CommonResult<List<StatusConfigRespVO>> getStatusByType(@PathVariable("statusType") String statusType) {
        List<CustomerStatusConfigDO> list = customerStatusConfigService.getStatusByType(statusType);
        return success(BeanUtils.toBean(list, StatusConfigRespVO.class));
    }

}