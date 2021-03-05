package com.gdztyy.api.service;

import com.gdztyy.api.vo.RsaStqtySumVo;


import java.util.List;

public interface RsaStqtySumService {
    List<RsaStqtySumVo> selectList();

    List<RsaStqtySumVo> selectListBy();
}
