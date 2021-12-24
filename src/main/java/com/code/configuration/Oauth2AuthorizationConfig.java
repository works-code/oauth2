package com.code.configuration;

import com.code.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;

@EnableAuthorizationServer
@Configuration
public class Oauth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter{

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()") // 모두 허용하지 않으면 해당 서버에서 토큰 접근이 불가능 하여 토큰을 DB에서 찾을 수 없다.
                .checkTokenAccess("isAuthenticated()") // 인증된 사용자만 토큰 체크 가능
                .allowFormAuthenticationForClients();
    }

    // client 설정
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource);
    }

    // 토큰 DB 저장
    @Bean
    public TokenStore tokenStore(){
        return new JdbcTokenStore(dataSource);
    }

    // 권한 동의 DB 저장
    @Bean
    public ApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
    }

    // 인증, 토큰 설정
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager) // grant_type password를 사용하기 위함 (manager 지정 안할시 password type 으로 토큰 발행시 Unsupported grant type: password 오류 발생)
                .userDetailsService(userDetailService) // refrash token 발행시 유저 정보 검사 하는데 사용하는 서비스 설정
                .tokenStore(tokenStore())
                .approvalStore(approvalStore());
    }
}
