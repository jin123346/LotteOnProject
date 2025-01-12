package com.lotteon.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 로그인 설정
        // 로그인 설정
        http.formLogin(login -> login
                .loginPage("/user/login")
                .loginProcessingUrl("/user/login")  // 로그인 처리 경로
                .successHandler(customAuthSuccessHandler()) // 로그인 성공 핸들러 설정
                .failureUrl("/user/login?error=true")  // 로그인 실패 시 URL 수정
                .usernameParameter("inId")  // 로그인 시 사용할 파라미터 이름
                .passwordParameter("Password")  // 로그인 시 사용할 비밀번호 파라미터 이름
        );

        // /seller/login에 대한 설정
        http.formLogin(login -> login
                .loginPage("/seller/login")
                .loginProcessingUrl("/seller/login")
                .successHandler(customAuthSuccessHandler()) // 로그인 성공 핸들러 설정
                .usernameParameter("inId")
                .passwordParameter("Password")
                .failureUrl("/seller/login?error=true")  // 로그인 실패 시 리디렉션 경로
        );


        // 로그아웃 설정
        http.logout(logout -> logout
                .invalidateHttpSession(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                .logoutSuccessUrl("/?success=101")
        );

        http.authorizeHttpRequests(authorize -> authorize
                // 더 구체적인 관리자 권한부터 상단에 배치
                .requestMatchers("/user/login").permitAll()  // 로그인 페이지는 인증 없이 접근 허용
                .requestMatchers("/seller/login").permitAll()  // 로그인 페이지는 인증 없이 접근 허용
                .requestMatchers("/admin/faq/**").hasRole("ADMIN")
                .requestMatchers("/admin/qna/**").hasRole("ADMIN")
                .requestMatchers("/admin/notice/**").hasRole("ADMIN")
                .requestMatchers("/admin/store/**").hasRole("ADMIN")
                .requestMatchers("/admin/user/**").hasRole("ADMIN")

                // ADMIN 또는 SELLER 권한으로 접근 가능한 /admin 경로
                .requestMatchers("/admin/**").hasAnyRole("ADMIN", "SELLER")

                // SELLER와 ADMIN만 접근할 수 있는 /seller 경로
                .requestMatchers("/seller/**").hasAnyRole("ADMIN", "SELLER")

                // 인증이 필요한 경로들
                .requestMatchers("/cart").authenticated()
                .requestMatchers("/article/delete/**").authenticated()

                // 공개 접근이 가능한 경로들
                .requestMatchers("/article/**").permitAll()
                .requestMatchers("/company/**").permitAll()
                .requestMatchers("/policy/**").permitAll()
                .requestMatchers("/cs/**").permitAll()

                // 기본 페이지는 모두 접근 가능
                .requestMatchers("/").permitAll()

                // 그 외의 요청들은 모두 허용
                .anyRequest().permitAll()
        );

        http.sessionManagement(session -> session
                .sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::migrateSession)
                .maximumSessions(1)
                .expiredUrl("/user/login?session=expired")  // 세션이 만료되었을 때의 리디렉트 경로
        );


        http.sessionManagement(session -> session
                .sessionFixation().migrateSession()  // 새로운 세션으로 교체
                .invalidSessionUrl("/user/login?error=invalidSession")  // 세션이 유효하지 않을 때 리디렉션
                .maximumSessions(1)  // 동시에 하나의 세션만 허용
                .expiredUrl("/user/login?error=sessionExpired"));  // 세션 만료 시 리디렉션


        // CSRF 비활성화 (필요에 따라 활성화 가능)
        http.csrf().disable();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http, MyUserDetailsService myUserDetailsService) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthSuccessHandler() {
        return new CustomAuthSuccessHandler();
    }



}
