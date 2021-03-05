package com.gdztyy.config.security;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdztyy.inca.entity.UserInfo;
import com.gdztyy.inca.service.IUserInfoService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 报货平台 获取用户信息
 * @author peiqy
 */
@Service
public class B2bUserDetailsServiceImpl implements UserDetailsService {
    @Resource
    IUserInfoService userInfoService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserInfo::getUserName,username);
        UserInfo one = userInfoService.getOne(queryWrapper);

        if(ObjectUtil.isNotNull(one)){
            return new B2bUserDetails(one.getUserName(),one.getPassword(),true,true,true,true,one.getCustomId());
        }

        return null;
    }
}
