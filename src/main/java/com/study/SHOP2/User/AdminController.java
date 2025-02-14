package com.study.SHOP2.User;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.stream.Collectors;

@Controller
public class AdminController {

    @GetMapping("/admin/dashboard")

    public String adminDashboard(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            System.out.println("현재 로그인한 사용자: " + authentication.getName());
            System.out.println("권한 목록: " + authentication.getAuthorities());
        } else {
            System.out.println("인증 정보가 없습니다.");
        }
        return "/admin/dashboard";
    }

    //http://localhost:8080/admin/access-denied
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "/admin/access-denied"; // /admin/access-denied
    }

}
