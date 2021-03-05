package com.gdztyy.inca.mapper;

import com.gdztyy.inca.entity.BmsStDef;
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
public interface BmsStDefMapper extends BaseMapper<BmsStDef> {


    Integer getUseWms(@Param("storageId") Long storageId, @Param("entryId")Integer entryId);
}
