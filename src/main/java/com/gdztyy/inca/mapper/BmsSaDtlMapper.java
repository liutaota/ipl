package com.gdztyy.inca.mapper;

import com.gdztyy.inca.entity.BmsSaDtl;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author peiqy
 * @since 2020-08-18
 */
public interface BmsSaDtlMapper extends BaseMapper<BmsSaDtl> {

    List<BmsSaDtl> selectListBy(Long salesId);
}
