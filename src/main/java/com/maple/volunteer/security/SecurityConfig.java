package com.maple.volunteer.security;

import com.maple.volunteer.security.jwt.JwtAuthFilter;
import com.maple.volunteer.security.jwt.JwtExceptionFilter;
import com.maple.volunteer.security.oauth2.CustomOAuth2UserService;
import com.maple.volunteer.security.oauth2.MyAuthenticationFailureHandler;
import com.maple.volunteer.security.oauth2.MyAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Profile("local")
public class SecurityConfig {

    private final MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    private final MyAuthenticationFailureHandler myAuthenticationFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtAuthFilter jwtAuthFilter;
    private final JwtExceptionFilter jwtExceptionFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic().disable()
                .cors().and()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/token/**").permitAll()
                .antMatchers("/").permitAll() // 권한 허용 경로 설정
                .anyRequest().authenticated()
                .and()
                .oauth2Login() // OAuth2 로그인 설정
                .userInfoEndpoint().userService(customOAuth2UserService)
                .and()
                .failureHandler(myAuthenticationFailureHandler)
                .successHandler(myAuthenticationSuccessHandler);

        return httpSecurity
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthFilter.class)
                .build();
    }
}
