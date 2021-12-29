package com.code.configuration;

import com.code.service.UserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailService userDetailService;

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    // 로그인 버튼 클릭시 사용자 체크 메소드
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        UserDetails info = userDetailService.loadUserByUsername(username);
        log.error("## => {} | {} | {}", username, password, info.getAuthorities());
        if(ObjectUtils.isEmpty(info)){
            throw new UsernameNotFoundException("user not found");
        }
        if(!StringUtils.equals(password, StringUtils.replace(info.getPassword(), "{noop}",""))){
            throw new UsernameNotFoundException("please password check");
        }
        // principal 정보는 jwt 토큰 생성시 추가정보 넣는데 사용하오니 필요한 정보는 여기서 넣어주세요.
        return new UsernamePasswordAuthenticationToken(info,password,info.getAuthorities());
    }
}
