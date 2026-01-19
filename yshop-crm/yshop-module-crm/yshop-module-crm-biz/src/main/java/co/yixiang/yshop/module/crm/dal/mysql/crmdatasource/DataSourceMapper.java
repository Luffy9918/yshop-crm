package co.yixiang.yshop.module.crm.dal.mysql.crmdatasource;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.mybatis.core.mapper.BaseMapperX;
import co.yixiang.yshop.framework.mybatis.core.query.LambdaQueryWrapperX;
import co.yixiang.yshop.module.crm.controller.admin.crmdatasource.vo.DataSourcePageReqVO;
import co.yixiang.yshop.module.crm.dal.dataobject.crmdatasource.DataSourceDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据源 Mapper
 *
 * @author yshop
 */
@Mapper
public interface DataSourceMapper extends BaseMapperX<DataSourceDO> {

    default PageResult<DataSourceDO> selectPage(DataSourcePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DataSourceDO>()
                .likeIfPresent(DataSourceDO::getSourceCode, reqVO.getSourceCode())
                .likeIfPresent(DataSourceDO::getSourceName, reqVO.getSourceName())
                .eqIfPresent(DataSourceDO::getChannel, reqVO.getChannel())
                .eqIfPresent(DataSourceDO::getRegion, reqVO.getRegion())
                .eqIfPresent(DataSourceDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(DataSourceDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DataSourceDO::getId));
    }

}