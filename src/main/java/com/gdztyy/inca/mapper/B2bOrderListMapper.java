package com.gdztyy.inca.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.gdztyy.inca.entity.B2bOrderList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author peiqy
 * @since 2020-08-19
 */
public interface B2bOrderListMapper extends BaseMapper<B2bOrderList> {

    void add1HintCount(@Param(Constants.WRAPPER)QueryWrapper<B2bOrderList> queryWrapper);
}
