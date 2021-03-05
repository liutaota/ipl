package com.gdztyy.inca.service.impl;

import com.gdztyy.inca.entity.UserInfo;
import com.gdztyy.inca.mapper.UserInfoMapper;
import com.gdztyy.inca.service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author peiqy
 * @since 2020-08-20
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

}
