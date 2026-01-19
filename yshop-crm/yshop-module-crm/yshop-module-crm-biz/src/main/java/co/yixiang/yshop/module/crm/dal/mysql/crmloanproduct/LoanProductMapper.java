package co.yixiang.yshop.module.crm.dal.mysql.crmloanproduct;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.mybatis.core.mapper.BaseMapperX;
import co.yixiang.yshop.framework.mybatis.core.query.LambdaQueryWrapperX;
import co.yixiang.yshop.module.crm.controller.admin.crmloanproduct.vo.LoanProductPageReqVO;
import co.yixiang.yshop.module.crm.dal.dataobject.crmloanproduct.LoanProductDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 贷款产品 Mapper
 *
 * @author yshop
 */
@Mapper
public interface LoanProductMapper extends BaseMapperX<LoanProductDO> {

    default PageResult<LoanProductDO> selectPage(LoanProductPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<LoanProductDO>()
                .likeIfPresent(LoanProductDO::getProductName, reqVO.getProductName())
                .eqIfPresent(LoanProductDO::getProductCode, reqVO.getProductCode())
                .eqIfPresent(LoanProductDO::getCityCode, reqVO.getCityCode())
                .eqIfPresent(LoanProductDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(LoanProductDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(LoanProductDO::getId));
    }

    default List<LoanProductDO> selectListByCityCode(String cityCode) {
        return selectList(new LambdaQueryWrapperX<LoanProductDO>()
                .eq(LoanProductDO::getCityCode, cityCode)
                .eq(LoanProductDO::getStatus, 1)
                .orderByDesc(LoanProductDO::getId));
    }

}