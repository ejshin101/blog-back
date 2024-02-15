package com.example.blog.web.common.security.handler;

import com.example.blog.web.common.security.domain.AuthUser;
import com.example.blog.web.common.security.jwt.provider.JwtTokenProvider;
import com.example.blog.web.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.example.blog.web.utils.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.blog.web.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository;
    private final JwtTokenProvider jwtTokenProvider;

    //인증 전에 접근을 시도한 URL로 리다이렉트
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        log.info("onAuthenticationSuccess");
        log.info("auth.getPrincipal() = " + authentication.getPrincipal());

        String targetUrl = determineTargetUrl(request, response, authentication);

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
//        super.onAuthenticationSuccess(request, response, authentication);
//        String targetUrl = "";
//        try {
//            response.setStatus(HttpServletResponse.SC_OK);
//            PrintWriter printWriter = response.getWriter();
//            printWriter.write(targetUrl);
//            printWriter.flush();
//            printWriter.close();
//        }catch (IOException e) {
//            log.error(e.getMessage(), e);
//        }
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUrl = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME).map(Cookie::getValue);
        String targetUrl = redirectUrl.orElse(getDefaultTargetUrl());
        AuthUser user = (AuthUser) authentication.getPrincipal();
        List<String> roles = user.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        System.out.println(user.getUserNo() + " + " +user.getEmail() + " + " + roles );
        String token = jwtTokenProvider.createToken(user.getUserNo(), user.getEmail(), roles);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", token)
                .build().toString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        oAuth2AuthorizationRequestBasedOnCookieRepository.removeAuthorizationRequestCookies(request, response);
    }
}
