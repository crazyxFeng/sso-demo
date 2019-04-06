package com.demo.xf.provider;

import com.demo.xf.domain.User;
import com.demo.xf.service.UserService;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: xiongfeng
 * Date: 2019/4/5 12:02
 * Description:
 */
@Component
public class CustomProvider implements AuthenticationProvider {

    @Resource
    UserService userService;

    @Resource
    PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        // 可以自定义service完成验证
        User userInfo = userService.get(username);
        if(userInfo == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        boolean pwdBoolean = passwordEncoder.matches(password, userInfo.getPassword());
        if (!pwdBoolean){
            throw new BadCredentialsException("用户密码错误!");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
