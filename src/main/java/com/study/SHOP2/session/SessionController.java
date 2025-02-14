package com.study.SHOP2.session;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SessionController {

    @GetMapping("/session-info")
    public Map<String, Object> getSessionInfo(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        response.put("loginTime", session.getAttribute("loginTime"));
        return response;
    }

    @PostMapping("/extend-session")
    public Map<String, Object> extendSession(HttpSession session) {
        session.setMaxInactiveInterval(10 * 60);  // 세션 10분 연장
        session.setAttribute("loginTime", Instant.now());  // 새로운 로그인 시간으로 갱신
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return response;
    }
}
