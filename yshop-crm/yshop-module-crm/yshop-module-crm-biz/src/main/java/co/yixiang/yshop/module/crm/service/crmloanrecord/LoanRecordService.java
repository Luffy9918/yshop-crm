package co.yixiang.yshop.module.crm.service.crmloanrecord;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.module.crm.controller.admin.crmloanrecord.vo.LoanRecordPageReqVO;
import co.yixiang.yshop.module.crm.controller.admin.crmloanrecord.vo.LoanRecordSaveReqVO;
import co.yixiang.yshop.module.crm.dal.dataobject.crmloanrecord.LoanRecordDO;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * 放款记录 Service 接口
 *
 * @author yshop
 */
public interface LoanRecordService {

    /**
     * 创建放款记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createLoanRecord(@Valid LoanRecordSaveReqVO createReqVO);

    /**
     * 更新放款记录
     *
     * @param updateReqVO 更新信息
     */
    void updateLoanRecord(@Valid LoanRecordSaveReqVO updateReqVO);

    /**
     * 删除放款记录
     *
     * @param id 编号
     */
    void deleteLoanRecord(Long id);

    /**
     * 获得放款记录
     *
     * @param id 编号
     * @return 放款记录
     */
    LoanRecordDO getLoanRecord(Long id);

    /**
     * 获得放款记录分页
     *
     * @param pageReqVO 分页查询
     * @return 放款记录分页
     */
    PageResult<LoanRecordDO> getLoanRecordPage(LoanRecordPageReqVO pageReqVO);

    /**
     * 获取客户的放款记录
     *
     * @param customerId 客户ID
     * @return 放款记录列表
     */
    List<LoanRecordDO> getLoanRecordsByCustomerId(Long customerId);

    /**
     * 获取渠道专员的放款记录
     *
     * @param staffId 专员ID
     * @return 放款记录列表
     */
    List<LoanRecordDO> getLoanRecordsByStaffId(Long staffId);

    /**
     * 计算渠道专员的放款总额
     *
     * @param staffId 专员ID
     * @return 放款总额
     */
    BigDecimal calculateTotalLoanAmountByStaff(Long staffId);

    /**
     * 计算渠道专员的服务费总额
     *
     * @param staffId 专员ID
     * @return 服务费总额
     */
    BigDecimal calculateTotalCommissionByStaff(Long staffId);

    /**
     * 校验放款记录是否存在
     *
     * @param id 记录ID
     */
    void validateLoanRecordExists(Long id);
}