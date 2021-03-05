package com.gdztyy.api.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdztyy.api.service.PubMonthlySalesService;
import com.gdztyy.api.vo.PubMonthlySales;
import com.gdztyy.inca.mapper.PubMonthlySalesMapper;
import com.gdztyy.util.AssertExt;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author MP
 * @since 2021-01-08
 */
@Service
public class PubMonthlySalesServiceImpl extends ServiceImpl<PubMonthlySalesMapper, PubMonthlySales> implements PubMonthlySalesService {
    @Resource
    PubMonthlySalesMapper pubMonthlySalesMapper;



    @Override
    public IPage<PubMonthlySales> selectPageList(Page page, List<Long> salesIdList, String customids, String tomonthss, String startTime, String endTime) {
        System.out.print("severice 层");
        AssertExt.notNull(page, "页码不为空");
        return pubMonthlySalesMapper.selectPageList(page, salesIdList,customids, tomonthss,startTime,endTime);
    }

    @Override
    public List<PubMonthlySales> selectListByTime(String startTime, String endTime) {
        return pubMonthlySalesMapper.selectListByTime(startTime,endTime);
    }
}
