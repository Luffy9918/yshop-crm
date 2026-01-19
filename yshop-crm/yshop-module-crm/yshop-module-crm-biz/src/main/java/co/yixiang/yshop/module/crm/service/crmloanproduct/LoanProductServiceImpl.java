package co.yixiang.yshop.module.crm.service.crmloanproduct;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.module.crm.controller.admin.crmloanproduct.vo.LoanProductPageReqVO;
import co.yixiang.yshop.module.crm.controller.admin.crmloanproduct.vo.LoanProductSaveReqVO;
import co.yixiang.yshop.module.crm.dal.dataobject.crmloanproduct.LoanProductDO;
import co.yixiang.yshop.module.crm.dal.mysql.crmloanproduct.LoanProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import java.util.List;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.module.crm.enums.ErrorCodeConstants.LOAN_PRODUCT_NOT_EXISTS;

/**
 * 贷款产品 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
@Slf4j
public class LoanProductServiceImpl implements LoanProductService {

    @Resource
    private LoanProductMapper loanProductMapper;

    @Override
    public Long createLoanProduct(@Valid LoanProductSaveReqVO createReqVO) {
        // 插入
        LoanProductDO loanProduct = LoanProductDO.builder()
                .productName(createReqVO.getProductName())
                .productCode(createReqVO.getProductCode())
                .cityCode(createReqVO.getCityCode())
                .cityName(createReqVO.getCityName())
                .minAmount(createReqVO.getMinAmount())
                .maxAmount(createReqVO.getMaxAmount())
                .minRate(createReqVO.getMinRate())
                .maxRate(createReqVO.getMaxRate())
                .periodMin(createReqVO.getPeriodMin())
                .periodMax(createReqVO.getPeriodMax())
                .repaymentMethods(createReqVO.getRepaymentMethods())
                .requirements(createReqVO.getRequirements())
                .bankName(createReqVO.getBankName())
                .productFeatures(createReqVO.getProductFeatures())
                .requiredDocuments(createReqVO.getRequiredDocuments())
                .processingTime(createReqVO.getProcessingTime())
                .status(createReqVO.getStatus() != null ? createReqVO.getStatus() : 1)
                .build();
        loanProductMapper.insert(loanProduct);
        return loanProduct.getId();
    }

    @Override
    public void updateLoanProduct(@Valid LoanProductSaveReqVO updateReqVO) {
        // 校验存在
        validateLoanProductExists(updateReqVO.getId());
        // 更新
        LoanProductDO updateObj = LoanProductDO.builder()
                .id(updateReqVO.getId())
                .productName(updateReqVO.getProductName())
                .productCode(updateReqVO.getProductCode())
                .cityCode(updateReqVO.getCityCode())
                .cityName(updateReqVO.getCityName())
                .minAmount(updateReqVO.getMinAmount())
                .maxAmount(updateReqVO.getMaxAmount())
                .minRate(updateReqVO.getMinRate())
                .maxRate(updateReqVO.getMaxRate())
                .periodMin(updateReqVO.getPeriodMin())
                .periodMax(updateReqVO.getPeriodMax())
                .repaymentMethods(updateReqVO.getRepaymentMethods())
                .requirements(updateReqVO.getRequirements())
                .bankName(updateReqVO.getBankName())
                .productFeatures(updateReqVO.getProductFeatures())
                .requiredDocuments(updateReqVO.getRequiredDocuments())
                .processingTime(updateReqVO.getProcessingTime())
                .status(updateReqVO.getStatus())
                .build();
        loanProductMapper.updateById(updateObj);
    }

    @Override
    public void deleteLoanProduct(Long id) {
        // 校验存在
        validateLoanProductExists(id);
        // 删除
        loanProductMapper.deleteById(id);
    }

    @Override
    public LoanProductDO getLoanProduct(Long id) {
        return loanProductMapper.selectById(id);
    }

    @Override
    public PageResult<LoanProductDO> getLoanProductPage(LoanProductPageReqVO pageReqVO) {
        return loanProductMapper.selectPage(pageReqVO);
    }

    @Override
    public List<LoanProductDO> getAvailableLoanProducts() {
        return loanProductMapper.selectList("status", 1);
    }

    @Override
    public List<LoanProductDO> getLoanProductsByCity(String cityCode) {
        return loanProductMapper.selectListByCityCode(cityCode);
    }

    @Override
    public void validateLoanProductExists(Long id) {
        if (loanProductMapper.selectById(id) == null) {
            throw exception(LOAN_PRODUCT_NOT_EXISTS);
        }
    }
}