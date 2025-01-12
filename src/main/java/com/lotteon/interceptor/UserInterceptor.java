package com.lotteon.interceptor;

import com.lotteon.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@Log4j2
@RequiredArgsConstructor
public class UserInterceptor implements HandlerInterceptor {

    private final UserService userService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            String authenticatedUsername = authentication.getName();  // 인증된 사용자 이름 가져오기
            log.info("@@@@@@@@@@@"+authenticatedUsername);

            // 로그인 성공 시 Member의 name 값을 가져와 모델에 추가
            String memberName = userService.getMemberNameByUsername(authenticatedUsername);
            log.info("memberName:" + memberName);

            if (modelAndView != null) {
                modelAndView.addObject("memberName", memberName);  // 모델에 추가
            }
        } else {
            log.warn("사용자가 인증되지 않았습니다. 익명 사용자입니다.");
        }
    }

}
