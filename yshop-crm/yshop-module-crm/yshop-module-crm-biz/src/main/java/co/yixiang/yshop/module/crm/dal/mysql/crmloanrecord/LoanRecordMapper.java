package co.yixiang.yshop.module.crm.dal.mysql.crmloanrecord;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.mybatis.core.mapper.BaseMapperX;
import co.yixiang.yshop.framework.mybatis.core.query.LambdaQueryWrapperX;
import co.yixiang.yshop.module.crm.controller.admin.crmloanrecord.vo.LoanRecordPageReqVO;
import co.yixiang.yshop.module.crm.dal.dataobject.crmloanrecord.LoanRecordDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 放款记录 Mapper
 *
 * @author yshop
 */
@Mapper
public interface LoanRecordMapper extends BaseMapperX<LoanRecordDO> {

    default PageResult<LoanRecordDO> selectPage(LoanRecordPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<LoanRecordDO>()
                .eqIfPresent(LoanRecordDO::getCustomerId, reqVO.getCustomerId())
                .likeIfPresent(LoanRecordDO::getCustomerName, reqVO.getCustomerName())
                .likeIfPresent(LoanRecordDO::getCustomerPhone, reqVO.getCustomerPhone())
                .eqIfPresent(LoanRecordDO::getProductId, reqVO.getProductId())
                .eqIfPresent(LoanRecordDO::getChannelStaffId, reqVO.getChannelStaffId())
                .likeIfPresent(LoanRecordDO::getContractNo, reqVO.getContractNo())
                .eqIfPresent(LoanRecordDO::getCommissionPaid, reqVO.getCommissionPaid())
                .betweenIfPresent(LoanRecordDO::getDisbursementDate, reqVO.getDisbursementDate())
                .betweenIfPresent(LoanRecordDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(LoanRecordDO::getId));
    }

    default List<LoanRecordDO> selectListByCustomerId(Long customerId) {
        return selectList(new LambdaQueryWrapperX<LoanRecordDO>()
                .eq(LoanRecordDO::getCustomerId, customerId)
                .orderByDesc(LoanRecordDO::getId));
    }

    default List<LoanRecordDO> selectListByStaffId(Long staffId) {
        return selectList(new LambdaQueryWrapperX<LoanRecordDO>()
                .eq(LoanRecordDO::getChannelStaffId, staffId)
                .orderByDesc(LoanRecordDO::getId));
    }

}