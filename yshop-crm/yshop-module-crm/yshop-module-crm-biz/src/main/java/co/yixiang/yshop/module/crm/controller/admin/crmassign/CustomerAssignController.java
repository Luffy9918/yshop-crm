package co.yixiang.yshop.module.crm.controller.admin.crmassign;

import co.yixiang.yshop.framework.apilog.core.annotation.ApiAccessLog;
import co.yixiang.yshop.framework.apilog.core.enums.OperateTypeEnum;
import co.yixiang.yshop.framework.common.pojo.CommonResult;
import co.yixiang.yshop.module.crm.controller.admin.crmassign.vo.CustomerAssignReqVO;
import co.yixiang.yshop.module.crm.controller.admin.crmassign.vo.CustomerAutoAssignReqVO;
import co.yixiang.yshop.module.crm.controller.admin.crmassign.vo.CustomerBatchAssignReqVO;
import co.yixiang.yshop.module.crm.dal.dataobject.crmcustomer.CrmCustomerDO;
import co.yixiang.yshop.module.crm.dal.mysql.crmcustomer.CrmCustomerMapper;
import co.yixiang.yshop.module.crm.service.assign.AssignEngine;
import co.yixiang.yshop.module.crm.service.crmoperatelog.CrmOperatelogService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.module.crm.enums.ErrorCodeConstants.CUSTOMER_NOT_EXISTS;

/**
 * 客户分配 Controller
 *
 * @author yshop
 */
@Tag(name = "管理后台 - 客户分配")
@RestController
@RequestMapping("/crm/customer-assign")
@Validated
@Slf4j
public class CustomerAssignController {

    @Resource
    private AssignEngine assignEngine;

    @Resource
    private CrmCustomerMapper customerMapper;

    @Resource
    private CrmOperatelogService crmOperatelogService;

    @PostMapping("/assign")
    @Operation(summary = "手动分配客户")
    @PreAuthorize("@ss.hasPermission('crm:customer:assign')")
    @ApiAccessLog(operateType = OperateTypeEnum.UPDATE)
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<Boolean> assignCustomer(@Valid @RequestBody CustomerAssignReqVO reqVO) {
        // 1. 验证客户是否存在
        CrmCustomerDO customer = customerMapper.selectById(reqVO.getCustomerId());
        if (customer == null) {
            throw exception(CUSTOMER_NOT_EXISTS);
        }

        // 2. 更新客户的 owner_user_id
        Long oldOwnerUserId = customer.getOwnerUserId();
        customerMapper.updateById(CrmCustomerDO.builder()
                .id(reqVO.getCustomerId())
                .ownerUserId(reqVO.getStaffId())
                .followTime(LocalDateTime.now())
                .build());

        // 3. 记录分配日志
        String logTitle = "手动分配客户" + (reqVO.getReason() != null ? " (" + reqVO.getReason() + ")" : "");
        crmOperatelogService.createLog(logTitle, reqVO.getCustomerId(), 0L, 0L);

        log.info("客户 {} 从业务员 {} 分配给业务员 {}", reqVO.getCustomerId(), oldOwnerUserId, reqVO.getStaffId());
        return CommonResult.success(true);
    }

    @PostMapping("/batch-assign")
    @Operation(summary = "批量分配客户")
    @PreAuthorize("@ss.hasPermission('crm:customer:assign')")
    @ApiAccessLog(operateType = OperateTypeEnum.UPDATE)
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<Integer> batchAssignCustomers(@Valid @RequestBody CustomerBatchAssignReqVO reqVO) {
        List<Long> customerIds = reqVO.getCustomerIds();
        if (customerIds == null || customerIds.isEmpty()) {
            return CommonResult.success(0);
        }

        int successCount = 0;
        for (Long customerId : customerIds) {
            try {
                // 验证客户是否存在
                CrmCustomerDO customer = customerMapper.selectById(customerId);
                if (customer == null) {
                    log.warn("客户不存在，跳过: {}", customerId);
                    continue;
                }

                // 更新客户的 owner_user_id
                Long oldOwnerUserId = customer.getOwnerUserId();
                customerMapper.updateById(CrmCustomerDO.builder()
                        .id(customerId)
                        .ownerUserId(reqVO.getStaffId())
                        .followTime(LocalDateTime.now())
                        .build());

                // 记录分配日志
                String logTitle = "批量分配客户" + (reqVO.getReason() != null ? " (" + reqVO.getReason() + ")" : "");
                crmOperatelogService.createLog(logTitle, customerId, 0L, 0L);

                log.info("客户 {} 从业务员 {} 批量分配给业务员 {}", customerId, oldOwnerUserId, reqVO.getStaffId());
                successCount++;
            } catch (Exception e) {
                log.error("批量分配客户失败: {}", customerId, e);
            }
        }

        return CommonResult.success(successCount);
    }

    @PostMapping("/auto-assign")
    @Operation(summary = "自动分配客户")
    @PreAuthorize("@ss.hasPermission('crm:customer:assign')")
    @ApiAccessLog(operateType = OperateTypeEnum.UPDATE)
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<Boolean> autoAssignCustomer(@Valid @RequestBody CustomerAutoAssignReqVO reqVO) {
        // 1. 验证客户是否存在
        CrmCustomerDO customer = customerMapper.selectById(reqVO.getCustomerId());
        if (customer == null) {
            throw exception(CUSTOMER_NOT_EXISTS);
        }

        // 2. 使用分配引擎获取业务员ID
        Long staffId = assignEngine.assignCustomer(
                reqVO.getCustomerId(),
                reqVO.getConditionType(),
                reqVO.getConditionValue()
        );

        if (staffId == null) {
            log.warn("未找到合适的业务员分配给客户: {}", reqVO.getCustomerId());
            return CommonResult.success(false);
        }

        // 3. 更新客户的 owner_user_id
        Long oldOwnerUserId = customer.getOwnerUserId();
        customerMapper.updateById(CrmCustomerDO.builder()
                .id(reqVO.getCustomerId())
                .ownerUserId(staffId)
                .followTime(LocalDateTime.now())
                .build());

        // 4. 记录分配日志
        String logTitle = "自动分配客户 (" + reqVO.getConditionType() + ": " + reqVO.getConditionValue() + ")";
        crmOperatelogService.createLog(logTitle, reqVO.getCustomerId(), 0L, 0L);

        log.info("客户 {} 从业务员 {} 自动分配给业务员 {} (条件: {}={})",
                reqVO.getCustomerId(), oldOwnerUserId, staffId, reqVO.getConditionType(), reqVO.getConditionValue());
        return CommonResult.success(true);
    }

}