package com.study.SHOP2.sales;

import com.study.SHOP2.User.Member;
import com.study.SHOP2.User.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
public class SalesController {

    private final SalesRepository salesRepository;
    private final MemberRepository memberRepository;

    @PostMapping("/order")
    public Map<String, Object> placeOrder(
            @RequestParam String itemName,
            @RequestParam Integer price,
            @RequestParam Integer count,
            Authentication authentication) {

        Map<String, Object> response = new HashMap<>();

        if (authentication == null || !authentication.isAuthenticated()) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return response;
        }

        String username = authentication.getName();
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        Sales order = new Sales();
        order.setItemName(itemName);
        order.setPrice(price);
        order.setCount(count);
        order.setMember(member);
        order.setUsername(member.getUsername());
        order.setDisplayName(member.getDisplayName());

        salesRepository.save(order);

        response.put("success", true);
        response.put("message", "주문이 완료되었습니다.");
        return response;
    }

}
