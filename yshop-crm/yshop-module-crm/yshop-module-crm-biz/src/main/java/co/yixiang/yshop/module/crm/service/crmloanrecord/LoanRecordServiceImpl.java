package co.yixiang.yshop.module.crm.service.crmloanrecord;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.module.crm.controller.admin.crmloanrecord.vo.LoanRecordPageReqVO;
import co.yixiang.yshop.module.crm.controller.admin.crmloanrecord.vo.LoanRecordSaveReqVO;
import co.yixiang.yshop.module.crm.dal.dataobject.crmloanrecord.LoanRecordDO;
import co.yixiang.yshop.module.crm.dal.mysql.crmloanrecord.LoanRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.module.crm.enums.ErrorCodeConstants.LOAN_RECORD_NOT_EXISTS;

/**
 * 放款记录 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
@Slf4j
public class LoanRecordServiceImpl implements LoanRecordService {

    @Resource
    private LoanRecordMapper loanRecordMapper;

    @Override
    public Long createLoanRecord(@Valid LoanRecordSaveReqVO createReqVO) {
        // 插入
        LoanRecordDO loanRecord = LoanRecordDO.builder()
                .customerId(createReqVO.getCustomerId())
                .customerName(createReqVO.getCustomerName())
                .customerPhone(createReqVO.getCustomerPhone())
                .productId(createReqVO.getProductId())
                .productName(createReqVO.getProductName())
                .loanAmount(createReqVO.getLoanAmount())
                .loanRate(createReqVO.getLoanRate())
                .loanPeriod(createReqVO.getLoanPeriod())
                .repaymentMethod(createReqVO.getRepaymentMethod())
                .bankName(createReqVO.getBankName())
                .disbursementDate(createReqVO.getDisbursementDate())
                .commissionAmount(createReqVO.getCommissionAmount())
                .commissionPaid(createReqVO.getCommissionPaid() != null ? createReqVO.getCommissionPaid() : 0)
                .channelStaffId(createReqVO.getChannelStaffId())
                .channelStaffName(createReqVO.getChannelStaffName())
                .contractNo(createReqVO.getContractNo())
                .remark(createReqVO.getRemark())
                .build();
        loanRecordMapper.insert(loanRecord);
        return loanRecord.getId();
    }

    @Override
    public void updateLoanRecord(@Valid LoanRecordSaveReqVO updateReqVO) {
        // 校验存在
        validateLoanRecordExists(updateReqVO.getId());
        // 更新
        LoanRecordDO updateObj = LoanRecordDO.builder()
                .id(updateReqVO.getId())
                .customerId(updateReqVO.getCustomerId())
                .customerName(updateReqVO.getCustomerName())
                .customerPhone(updateReqVO.getCustomerPhone())
                .productId(updateReqVO.getProductId())
                .productName(updateReqVO.getProductName())
                .loanAmount(updateReqVO.getLoanAmount())
                .loanRate(updateReqVO.getLoanRate())
                .loanPeriod(updateReqVO.getLoanPeriod())
                .repaymentMethod(updateReqVO.getRepaymentMethod())
                .bankName(updateReqVO.getBankName())
                .disbursementDate(updateReqVO.getDisbursementDate())
                .commissionAmount(updateReqVO.getCommissionAmount())
                .commissionPaid(updateReqVO.getCommissionPaid())
                .channelStaffId(updateReqVO.getChannelStaffId())
                .channelStaffName(updateReqVO.getChannelStaffName())
                .contractNo(updateReqVO.getContractNo())
                .remark(updateReqVO.getRemark())
                .build();
        loanRecordMapper.updateById(updateObj);
    }

    @Override
    public void deleteLoanRecord(Long id) {
        // 校验存在
        validateLoanRecordExists(id);
        // 删除
        loanRecordMapper.deleteById(id);
    }

    @Override
    public LoanRecordDO getLoanRecord(Long id) {
        return loanRecordMapper.selectById(id);
    }

    @Override
    public PageResult<LoanRecordDO> getLoanRecordPage(LoanRecordPageReqVO pageReqVO) {
        return loanRecordMapper.selectPage(pageReqVO);
    }

    @Override
    public List<LoanRecordDO> getLoanRecordsByCustomerId(Long customerId) {
        return loanRecordMapper.selectListByCustomerId(customerId);
    }

    @Override
    public List<LoanRecordDO> getLoanRecordsByStaffId(Long staffId) {
        return loanRecordMapper.selectListByStaffId(staffId);
    }

    @Override
    public BigDecimal calculateTotalLoanAmountByStaff(Long staffId) {
        return loanRecordMapper.selectListByStaffId(staffId).stream()
                .map(LoanRecordDO::getLoanAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal calculateTotalCommissionByStaff(Long staffId) {
        return loanRecordMapper.selectListByStaffId(staffId).stream()
                .map(r -> r.getCommissionAmount() != null ? r.getCommissionAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public void validateLoanRecordExists(Long id) {
        if (loanRecordMapper.selectById(id) == null) {
            throw exception(LOAN_RECORD_NOT_EXISTS);
        }
    }
}