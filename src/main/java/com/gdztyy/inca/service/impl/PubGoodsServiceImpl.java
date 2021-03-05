package com.gdztyy.inca.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdztyy.api.vo.GoodsVo;
import com.gdztyy.inca.entity.PubGoods;
import com.gdztyy.inca.mapper.PubGoodsMapper;
import com.gdztyy.inca.service.IPubGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author peiqy
 * @since 2020-08-18
 */
@Service
@Slf4j
public class PubGoodsServiceImpl extends ServiceImpl<PubGoodsMapper, PubGoods> implements IPubGoodsService {

    @Resource
    PubGoodsMapper pubGoodsMapper;


}
