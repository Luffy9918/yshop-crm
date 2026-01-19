package co.yixiang.yshop.module.crm.service.crmloanproduct;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.module.crm.controller.admin.crmloanproduct.vo.LoanProductPageReqVO;
import co.yixiang.yshop.module.crm.controller.admin.crmloanproduct.vo.LoanProductSaveReqVO;
import co.yixiang.yshop.module.crm.dal.dataobject.crmloanproduct.LoanProductDO;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 贷款产品 Service 接口
 *
 * @author yshop
 */
public interface LoanProductService {

    /**
     * 创建贷款产品
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createLoanProduct(@Valid LoanProductSaveReqVO createReqVO);

    /**
     * 更新贷款产品
     *
     * @param updateReqVO 更新信息
     */
    void updateLoanProduct(@Valid LoanProductSaveReqVO updateReqVO);

    /**
     * 删除贷款产品
     *
     * @param id 编号
     */
    void deleteLoanProduct(Long id);

    /**
     * 获得贷款产品
     *
     * @param id 编号
     * @return 贷款产品
     */
    LoanProductDO getLoanProduct(Long id);

    /**
     * 获得贷款产品分页
     *
     * @param pageReqVO 分页查询
     * @return 贷款产品分页
     */
    PageResult<LoanProductDO> getLoanProductPage(LoanProductPageReqVO pageReqVO);

    /**
     * 获取所有上架的贷款产品
     *
     * @return 贷款产品列表
     */
    List<LoanProductDO> getAvailableLoanProducts();

    /**
     * 按城市获取可用的贷款产品
     *
     * @param cityCode 城市编码
     * @return 贷款产品列表
     */
    List<LoanProductDO> getLoanProductsByCity(String cityCode);

    /**
     * 校验贷款产品是否存在
     *
     * @param id 产品ID
     */
    void validateLoanProductExists(Long id);
}