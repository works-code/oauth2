package com.code.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

@SessionAttributes("authorizationRequest") // authorizationRequest(로그인 인증 관련 정보)에 대한 값을 세션에 자동으로 저장 것으로 해당 클래스에서 변경 하지 않는다면 선언 하지 않아도 된다.
@Slf4j
@Controller
public class LoginController {

    @Autowired
    private TokenEndpoint tokenEndpoint;

    @RequestMapping(value = "/oauth/login", method = RequestMethod.GET)
    public String login(@RequestParam(value = "error", required = false) String error, Model model){
        model.addAttribute("error", error);
        return "login";
    }

    // 권한 동의 커스텀
    @RequestMapping("/oauth/confirm_access")
    public String confirm(HttpServletRequest request, Map<String, Object> model){
        AuthorizationRequest authorizationRequest = (AuthorizationRequest) request.getSession().getAttribute("authorizationRequest");

        // spring security에서 전체적인 로그인 플로우에서 크로스 도메인 관련 처리 하고 있어 그대로 가져옴 (사용 안하면 해당 코드는 없어도 됨.)
        if (request.getAttribute("_csrf") != null) {
            model.put("_csrf", request.getAttribute("_csrf"));
        }
        log.error("## => {}", authorizationRequest.getClientId()); // 세션에 로그인에 필요한 정보가 담겨 있다.
        return "confirm";
    }

    // 코드 발행 커스텀
    @RequestMapping("/oauth/token")
    public ResponseEntity<OAuth2AccessToken> oauthToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        ResponseEntity<OAuth2AccessToken> response = tokenEndpoint.postAccessToken(principal, parameters);
        String grantType = StringUtils.defaultString(parameters.get("grant_type"), "");

        // 토큰 발행 방식에 따른 비즈니스 코드 작성
        if(StringUtils.equals("authorization_code", grantType)){

        }else if(StringUtils.equals("refresh_token", grantType)){

        }else if(StringUtils.equals("password", grantType)){

        }else if(StringUtils.equals("client_credentials", grantType)){

        }
        log.info("## get token => {} | {}", response.getBody().getAdditionalInformation(), grantType);
        return response;
    }
}
