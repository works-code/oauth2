package com.code.configuration;

import com.code.handler.CustomFailHandler;
import com.code.handler.CustomSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@EnableWebSecurity // 웹보안 활성화
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() // csrf 공격을 막기 위해 state 값을 전달 받지 않는다
                .formLogin() // 로그인 페이지 설정
                .loginPage("/oauth/login")
                .failureHandler(failureHandler()) // 로그인 실패 custom
                .successHandler(successHandler()) // 로그인 성공 custom
                .and()
                .httpBasic(); // http 통신으로 basic auth를 사용 할 수 있다. (ex: Authorization: Basic bzFbdGfmZrptWY30YQ==)
    }

    /**
     * authenticationManager bean 생성 하여 셋팅 안할시 grant_type : password 지원 안함
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    // 커스텀 인증 : 어떤 사용자인지 확인하는 메소드 커스텀
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Bean
    public AuthenticationFailureHandler failureHandler(){
        return new CustomFailHandler();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler(){
        return new CustomSuccessHandler();
    }

}
