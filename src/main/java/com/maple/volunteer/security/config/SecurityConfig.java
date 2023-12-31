package com.maple.volunteer.security.config;

import com.maple.volunteer.security.jwt.config.JwtAuthFilter;
import com.maple.volunteer.security.jwt.config.JwtExceptionFilter;
import com.maple.volunteer.security.oauth2.CustomOAuth2UserService;
import com.maple.volunteer.security.oauth2.MyAuthenticationFailureHandler;
import com.maple.volunteer.security.oauth2.MyAuthenticationSuccessHandler;
import com.maple.volunteer.type.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.http.HttpMethod;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    private final MyAuthenticationFailureHandler myAuthenticationFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtAuthFilter jwtAuthFilter;
    private final JwtExceptionFilter jwtExceptionFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .headers().frameOptions().disable()
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .cors().configurationSource(configurationSource())
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
//                .authorizeRequests()
//                .antMatchers("/maple/user/login",
//                        "/maple/user/signup",
//                        "/maple/community/category",
//                        "/maple/community/search/{type}",
//                        "maple/community/{communityId}").permitAll()
//                .antMatchers(HttpMethod.GET, "/maple/category").permitAll()
//                .antMatchers(HttpMethod.GET, "/maple/community").permitAll()
//                .antMatchers("/maple/admin/**").hasAuthority(Role.ADMIN.getKey())
//                .antMatchers(HttpMethod.POST, "/maple/community").hasAuthority(Role.HOST.getKey())
//                .antMatchers(HttpMethod.PUT, "/maple/community/{communityId}").hasAuthority(Role.HOST.getKey())
//                .antMatchers(HttpMethod.DELETE, "/maple/community/{communityId}").hasAuthority(Role.HOST.getKey())
//                .anyRequest().hasAuthority(Role.USER.getKey())
//                .and()
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


    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "https://volunteer-frontend.vercel.app/", "https://ecof.site"));
        configuration.setAllowCredentials(true);  // 토큰 주고 받을 때
        configuration.addAllowedHeader("*");
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PATCH", "PUT", "DELETE", "OPTIONS"));
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
