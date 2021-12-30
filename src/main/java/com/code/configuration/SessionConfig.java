package com.code.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 10) // 지정시 10초뒤 세션 만료 (해당 어노테이션 사용시 timeout application.yml 로 설정 불가능)
@Configuration
public class SessionConfig {

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("CORINSESSIONID");// JSESSIONID 가 기본
        serializer.setCookiePath("/");
        serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$"); // 호출한 도메인으로 설정됨
        serializer.setCookieMaxAge(10); // -1 이 브라우저 닫힐때까지 이며, 현재 세션이 10초이고, 10초뒤에는 쿠키도 무의미 하니 10초로 설정
        return serializer;
    }

}
