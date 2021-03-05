package com.gdztyy.inca.mapper;

import com.gdztyy.inca.entity.BmsTrPosDef;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author peiqy
 * @since 2020-08-18
 */
public interface BmsTrPosDefMapper extends BaseMapper<BmsTrPosDef> {

    BmsTrPosDef selectByStoreIdId(@Param("b2bStoreId")Long b2bStoreId);
}
