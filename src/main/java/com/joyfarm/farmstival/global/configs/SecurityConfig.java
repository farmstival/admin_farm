package com.joyfarm.farmstival.global.configs;


import com.joyfarm.farmstival.member.services.LoginFailureHandler;
import com.joyfarm.farmstival.member.services.LoginSuccessHandler;
import com.joyfarm.farmstival.member.services.MemberAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /* 로그인, 로그아웃 S */
        http.formLogin(f -> {
            f.loginPage("/member/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .successHandler(new LoginSuccessHandler()) //로그인 성공시 유입
                    .failureHandler(new LoginFailureHandler()); //로그인 실패시 유입
        });


        http.logout(f -> {
            f.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                    .logoutSuccessUrl("/member/login"); //로그아웃 성공시 이동할 주소

        });
        /* 로그인, 로그아웃 E */

        /* 인가(접근 통제) 설정 S */
        http.authorizeHttpRequests(c -> {
            c.requestMatchers("/member/**").permitAll()
                    .anyRequest().permitAll();
        });

        //AuthenticationEntryPoint: 인증이 필요한 리소스에 접근할 때, 인증되지 않은 사용자에게 어떻게 응답할지 결정
        http.exceptionHandling(c -> {
            c.authenticationEntryPoint(new MemberAuthenticationEntryPoint()).accessDeniedHandler((req, res, e) -> {
                res.sendError(HttpStatus.UNAUTHORIZED.value()); //인증된 상태에서도 권한이 부족하여 접근이 거부되었을 경우
            });
        });
        /* 인가(접근 통제) 설정 E */


        // iframe 자원 출처를 같은 서버 자원으로 한정
        http.headers(c -> c.frameOptions(f -> f.sameOrigin()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}