package com.code.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
public class LoginController {

    @RequestMapping(value = "/oauth/login", method = RequestMethod.GET)
    public String login(){
        return "login";
    }

    // 권한 동의 커스텀
    @RequestMapping("/oauth/confirm_access")
    public String confirm(HttpServletRequest request){
        AuthorizationRequest authorizationRequest = (AuthorizationRequest) request.getSession().getAttribute("authorizationRequest");
        log.error("## => {}", authorizationRequest.getClientId()); // 세션에 로그인에 필요한 정보가 담겨 있다.
        return "confirm";
    }
}
