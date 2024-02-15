package com.example.blog.web.config;

import com.example.blog.web.common.security.CustomUserDetailsService;
import com.example.blog.web.common.security.handler.CustomLoginFailureHandler;
import com.example.blog.web.common.security.handler.CustomLoginSuccessHandler;
import com.example.blog.web.common.security.jwt.filter.JwtAuthenticationFilter;
import com.example.blog.web.common.security.jwt.filter.JwtRequestFilter;
import com.example.blog.web.common.security.jwt.provider.JwtTokenProvider;
import com.example.blog.web.common.security.service.CustomOAuth2UserService;
import com.example.blog.web.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //spring security 설정 클래스 정의

    //jwt
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOAuth2UserService customOAuth2UserService;

    //비밀번호 암호처리기 생성
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //쿠키 기반 인가 Repository
    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("security config...");

        //폼 로그인 기능과 베이직 인증 비활성화
        http.formLogin().disable()
                .httpBasic().disable();

        http.cors();

        //CSRF 방지 지원기능 비활성화
        http.csrf().disable();

        //jwt 관련 필터 보안 컨텍스트에 추가
        http.addFilterAt(new JwtAuthenticationFilter(authenticationManager(), jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtRequestFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //oauth login 활성화
        http.oauth2Login()
                    .authorizationEndpoint()
                    .baseUri("/oauth2/authorization/")
                    .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
                .and()
                    .redirectionEndpoint()
                    .baseUri("/login/oauth2/code/**")
                .and()
                    .userInfoEndpoint()//oauth2 로그인 성공 후 사용자 정보 가져옴
                    .userService(customOAuth2UserService) // Userservice 구현체
                .and()
                    .successHandler(authenticationSuccessHandler())
                    .failureHandler(authenticationFailureHandler());

                http.addFilterBefore(new JwtAuthenticationFilter(authenticationManager(), jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);


        //요청에 대한 인증 요구
        http.authorizeRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/api/users/**").access("permitAll")
                .antMatchers("/api/categories/**").access("permitAll")
                .antMatchers("/api/guests/**").permitAll()
                .antMatchers("/api/boards/image/upload").permitAll()
                .antMatchers("/api/sendmail").permitAll()
                .antMatchers("/api/boards/**").access("request.method == 'GET' ? permitAll : hasAnyRole('ROLE_USER','ROLE_ADMIN')")
                .anyRequest().authenticated();

        http.logout()
                .logoutUrl("/api/googleout")
                .logoutSuccessUrl("/");
    }

    //spring security의 UserDetailsService를 구현한 클래스를 빈으로 등록
    @Bean
    public UserDetailsService customUserDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // oauth 인증 성공 핸들러
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomLoginSuccessHandler(oAuth2AuthorizationRequestBasedOnCookieRepository(), jwtTokenProvider);
    }

    // oauth 인증 실패 핸들러
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomLoginFailureHandler();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //CustomUserDetailsService 빈을 인증 제공자에 지정하고 비밀번호 암호처리기들 등록
        auth.userDetailsService(customUserDetailsService())
                .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.httpFirewall(defaultHttpFireWall());
    }

    @Bean
    public HttpFirewall defaultHttpFireWall() {
        return new DefaultHttpFirewall();
    }


    //CORS 사용자 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Origin", "X-Requested-With", "Content-Type", "Accept", "Accept", "Key", "Authorization"));
        config.setExposedHeaders(Arrays.asList("AUthorization", "Content-Disposition", "x-auth-token"));

        // 허용된 도메인 리스트
        List<String> allowedOrigins = Arrays.asList("https://blog.shin-eunji.com", "https://api.shin-eunji.com");

        // 요청의 Origin 헤더를 확인하고, 허용된 도메인이면 'Access-Control-Allow-Origin'에 설정
        config.setAllowedOriginPatterns(allowedOrigins);

        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
