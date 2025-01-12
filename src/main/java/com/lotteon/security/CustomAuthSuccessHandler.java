package com.lotteon.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;


@Log4j2
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String redirectUrl = "/";  // 기본 리디렉션 경로는 메인 페이지

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();
            if (role.equals("ROLE_ADMIN")) {
                redirectUrl = "/admin/main";  // 관리자는 관리자 페이지로 리디렉션
                break;
            } else if (role.equals("ROLE_SELLER")) {
                redirectUrl = "/admin/main";  // 판매자는 판매자 페이지로 리디렉션
                break;
            } else if (role.equals("ROLE_MEMBER")) {
                redirectUrl = "/";  // 일반 회원은 메인 페이지로 리디렉션
                break;
            }
        }

        response.sendRedirect(redirectUrl);
    }

    private boolean hasRole(Collection<? extends GrantedAuthority> authorities, String role) {
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(role)) {
                return true;
            }
        }
        return false;
    }
}
