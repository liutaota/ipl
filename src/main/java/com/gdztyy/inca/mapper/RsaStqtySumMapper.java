package com.gdztyy.inca.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gdztyy.api.vo.RsaStqtySumVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RsaStqtySumMapper extends BaseMapper<RsaStqtySumVo> {

    List<RsaStqtySumVo> selectList();

    List<RsaStqtySumVo> selectListBy();
}
