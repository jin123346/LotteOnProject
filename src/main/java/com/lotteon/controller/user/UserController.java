package com.lotteon.controller.user;

import com.lotteon.entity.User.Member;
import com.lotteon.entity.User.Seller;
import com.lotteon.entity.User.User;
import com.lotteon.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager; // AuthenticationManager로 수정
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/join")
    public String userJoin(Model model) {
        model.addAttribute("content", "join");
        return "content/user/join"; // Points to "content/user/join"
    }

    @GetMapping("/login")
    public String userLogin(Model model) {
        model.addAttribute("content", "login");
        return "content/user/login"; // Points to "content/user/login"
    }

    @GetMapping("/term")
    public String terms(Model model) {
        model.addAttribute("content", "term");
        return "content/user/term"; // Points to "content/user/term"
    }

    @PostMapping("/login")
    public String login(@RequestParam("inId") String username,
                        @RequestParam("Password") String password,
                        Model model) {

       //현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUsername = authentication.getName();  // 인증된 사용자 이름 가져오기

        // 로그인 성공 시 Member의 name 값을 가져와 모델에 추가
        String memberName = userService.getMemberNameByUsername(authenticatedUsername);
        log.info("memberName:" + memberName);
        model.addAttribute("memberName", memberName);


        return "redirect:/"; // 로그인 성공 후 이동할 페이지
    }


}

