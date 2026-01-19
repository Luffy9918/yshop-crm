package co.yixiang.yshop.module.crm.service.crmstatusconfig;

import co.yixiang.yshop.module.crm.controller.admin.crmstatusconfig.vo.StatusConfigSaveReqVO;
import co.yixiang.yshop.module.crm.dal.dataobject.crmstatusconfig.CustomerStatusConfigDO;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 客户状态配置 Service 接口
 *
 * @author yshop
 */
public interface CustomerStatusConfigService {

    /**
     * 获取所有状态配置
     */
    List<CustomerStatusConfigDO> getAllStatusConfigs();

    /**
     * 根据类型获取状态
     */
    List<CustomerStatusConfigDO> getStatusByType(String statusType);

    /**
     * 创建状态配置
     */
    Long createStatusConfig(@Valid StatusConfigSaveReqVO createReqVO);

    /**
     * 更新状态配置
     */
    void updateStatusConfig(@Valid StatusConfigSaveReqVO updateReqVO);

    /**
     * 删除状态配置
     */
    void deleteStatusConfig(Long id);

    /**
     * 获取状态配置
     */
    CustomerStatusConfigDO getStatusConfig(Long id);

    /**
     * 校验状态配置是否存在
     */
    void validateStatusConfigExists(Long id);
}