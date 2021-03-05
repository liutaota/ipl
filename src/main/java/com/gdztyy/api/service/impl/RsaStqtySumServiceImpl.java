package com.gdztyy.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.gdztyy.api.service.RsaStqtySumService;
import com.gdztyy.api.vo.RsaStqtySumVo;
import com.gdztyy.inca.mapper.RsaStqtySumMapper;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RsaStqtySumServiceImpl extends ServiceImpl<RsaStqtySumMapper, RsaStqtySumVo> implements RsaStqtySumService {
    @Autowired
    private  RsaStqtySumMapper rsaStqtySumMapper;

    @Override
    public List<RsaStqtySumVo> selectList() {
        System.out.print("rsaStqtySumMapper"+"进来了");
            return this.rsaStqtySumMapper.selectList();
    }

    @Override
    public List<RsaStqtySumVo> selectListBy() {
        return rsaStqtySumMapper.selectListBy();
    }
}
