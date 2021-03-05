package com.gdztyy.api.controller;


import com.gdztyy.api.service.RsaStqtySumService;
import com.gdztyy.api.vo.RsaStqtySumVo;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author
 */
@RestController
@RequestMapping("/api/detail")
public class RsaStqtySumController {
    @Resource
    RsaStqtySumService rsaStqtySumService;


    @RequestMapping("/selectListBy")
    @PreAuthorize("isAuthenticated()")
    public List<RsaStqtySumVo> selectListBy() {
        List<RsaStqtySumVo> rsaStqtySumVo=rsaStqtySumService.selectListBy();
        return rsaStqtySumVo;
    }


}
