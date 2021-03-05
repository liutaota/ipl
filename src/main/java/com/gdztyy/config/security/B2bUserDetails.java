package com.gdztyy.config.security;


import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;

/**
 * B2b 用户信息
 *
 * @author
 */
@Setter
@Getter
public class B2bUserDetails extends User {
    public Long customId;


    public B2bUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Long customId) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked,new ArrayList<>());
        this.customId = customId;
    }
}
