package com.gdztyy.inca.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gdztyy.api.vo.GoodsVo;
import com.gdztyy.api.vo.ResultVo;
import com.gdztyy.inca.entity.BmsStQtyLst;
import com.gdztyy.inca.entity.PubGoods;
import com.gdztyy.inca.mapper.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author peiqy
 * @since 2020-08-18
 */
public interface IPubGoodsService extends IService<PubGoods> {




}
