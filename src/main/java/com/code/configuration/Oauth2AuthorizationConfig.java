package com.code.configuration;

import com.code.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
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
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

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
        security.tokenKeyAccess("permitAll()") // 모두 허용하지 않으면 '/oauth/token_key' URL은 404 에러와 토큰을 DB에 저장 할 수 없어 필수 (isAuthenticated()로 설정시 'token_key' 호출시 404 에러 발생 > 모두 허용 또는 허용 안함중에 하나 인것으로 보임)
                //.checkTokenAccess("isAuthenticated()") // 인증된 사용자만 토큰 유효성 체크 '/oauth/check_token' 가능 (JWT 일때는 필요 없음)
                .allowFormAuthenticationForClients();
    }

    // client 설정
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource);
    }

    // 토큰 DB 저장
    /*@Bean
    public TokenStore tokenStore(){
        return new JdbcTokenStore(dataSource);
    }*/

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
                //.tokenStore(tokenStore()) // jwt 로 변경시 토큰 저장하지 않아도 리소스 서버에서 차제적으로 체크 가능
                .accessTokenConverter(jwtAccessTokenConverter())
                .approvalStore(approvalStore());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        // RSA 암호화 : 비 대칭키 암호화 : 공개키로 암호화 하면 개인키로 복호화
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwtkey.jks"), "corin1234".toCharArray());
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("jwtkey"));

        // 대칭키 암호화 : key 값은 리소스 서버에도 넣고 하면 됨.
         /*JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("key");*/

        return converter;
    }

}
