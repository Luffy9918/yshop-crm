package co.yixiang.yshop.module.crm.service.crmstatusconfig;

import co.yixiang.yshop.module.crm.controller.admin.crmstatusconfig.vo.StatusConfigSaveReqVO;
import co.yixiang.yshop.module.crm.dal.dataobject.crmstatusconfig.CustomerStatusConfigDO;
import co.yixiang.yshop.module.crm.dal.mysql.crmstatusconfig.CustomerStatusConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import java.util.List;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.module.crm.enums.ErrorCodeConstants.*;

/**
 * 客户状态配置 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
@Slf4j
public class CustomerStatusConfigServiceImpl implements CustomerStatusConfigService {

    @Resource
    private CustomerStatusConfigMapper customerStatusConfigMapper;

    @Override
    public List<CustomerStatusConfigDO> getAllStatusConfigs() {
        return customerStatusConfigMapper.selectList();
    }

    @Override
    public List<CustomerStatusConfigDO> getStatusByType(String statusType) {
        return customerStatusConfigMapper.selectList("status_type", statusType);
    }

    @Override
    public Long createStatusConfig(@Valid StatusConfigSaveReqVO createReqVO) {
        // 校验编码唯一性
        validateStatusCodeUnique(null, createReqVO.getStatusCode());

        // 插入
        CustomerStatusConfigDO config = CustomerStatusConfigDO.builder()
                .statusCode(createReqVO.getStatusCode())
                .statusName(createReqVO.getStatusName())
                .statusType(createReqVO.getStatusType())
                .colorType(createReqVO.getColorType())
                .cssClass(createReqVO.getCssClass())
                .sortOrder(createReqVO.getSortOrder())
                .isEnabled(createReqVO.getIsEnabled() != null ? createReqVO.getIsEnabled() : 1)
                .build();
        customerStatusConfigMapper.insert(config);
        return config.getId();
    }

    @Override
    public void updateStatusConfig(@Valid StatusConfigSaveReqVO updateReqVO) {
        // 校验存在
        validateStatusConfigExists(updateReqVO.getId());
        // 校验编码唯一性
        validateStatusCodeUnique(updateReqVO.getId(), updateReqVO.getStatusCode());

        // 更新
        CustomerStatusConfigDO updateObj = CustomerStatusConfigDO.builder()
                .id(updateReqVO.getId())
                .statusCode(updateReqVO.getStatusCode())
                .statusName(updateReqVO.getStatusName())
                .statusType(updateReqVO.getStatusType())
                .colorType(updateReqVO.getColorType())
                .cssClass(updateReqVO.getCssClass())
                .sortOrder(updateReqVO.getSortOrder())
                .isEnabled(updateReqVO.getIsEnabled())
                .build();
        customerStatusConfigMapper.updateById(updateObj);
    }

    @Override
    public void deleteStatusConfig(Long id) {
        // 校验存在
        validateStatusConfigExists(id);
        // 删除
        customerStatusConfigMapper.deleteById(id);
    }

    @Override
    public CustomerStatusConfigDO getStatusConfig(Long id) {
        return customerStatusConfigMapper.selectById(id);
    }

    @Override
    public void validateStatusConfigExists(Long id) {
        if (customerStatusConfigMapper.selectById(id) == null) {
            throw exception(CUSTOMER_STATUS_CONFIG_NOT_EXISTS);
        }
    }

    /**
     * 校验状态编码唯一性
     */
    private void validateStatusCodeUnique(Long id, String statusCode) {
        CustomerStatusConfigDO existing = customerStatusConfigMapper.selectOne("status_code", statusCode);
        if (existing == null) {
            return;
        }
        // 如果是更新，检查是否是当前记录
        if (id != null && existing.getId().equals(id)) {
            return;
        }
        throw exception(CUSTOMER_STATUS_CODE_EXISTS);
    }
}