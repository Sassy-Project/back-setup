package com.projectsassy.sassy.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable() // ui에서 들어오는 것. auth 기반의 로그인창이 안뜨도록 설정.(security 사용하면 기본 로그인 창이있음)
                    .csrf().disable() // crosssite 기능. csrf 보안 기능이 rest api 에서 안쓰이므로 disable.
                    .cors()
                .and()
                    .headers().frameOptions().sameOrigin()
                .and()// crosssite 다른 domain 허용
                    .exceptionHandling()
                .and()
                    .authorizeRequests()
                    .antMatchers("/swagger-resources/**").permitAll()
                    .antMatchers("/users/**").permitAll() // user 권한 허용 // 이거 다시
                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt 사용할 경우 세션을 사용하지 않는다.
//                .and()
//                .addFilterBefore(new JwtFilter(userService), UsernamePasswordAuthenticationFilter.class) // 토큰인가 전 로그인 검증
                .build();
    }
}
