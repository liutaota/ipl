package com.gdztyy.inca.mapper;

import com.gdztyy.api.vo.CustomManageScopeVo;
import com.gdztyy.inca.entity.GspCompanyManagerage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author peiqy
 * @since 2020-08-19
 */
public interface GspCompanyManagerageMapper extends BaseMapper<GspCompanyManagerage> {
    List<CustomManageScopeVo> selectBy(@Param("customId") Long customId, @Param("entryId")Integer entryId, @Param("bussinessScope") Long bussinessScope, @Param("useStatus") Integer useStatus);
}
