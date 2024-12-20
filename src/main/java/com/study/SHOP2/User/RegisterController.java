package com.study.SHOP2.User;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class RegisterController {
    
    //회원가입에서는 @RequiredArgsConstructor을 사용안하고 Autowird로
    @Autowired
    private RegisterService registerService;

    @Autowired
    private MemberRepository memberRepository;


    // 회원가입
    @GetMapping("/register")
    public String registerForm() {

        return "register.html";
    }


    //회원가입 처리
    @PostMapping("/sign")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> register(
            @RequestBody
            @Valid RegisterDto registerDto,
            BindingResult bindingResult
    ) {
        System.out.println("RegisterDto: " + registerDto);
        System.out.println("BindingResult errors: " + bindingResult.getAllErrors());
        System.out.println("아이디" + registerDto.getUsername());
        System.out.println("비밀번호" + registerDto.getPassword());
        System.out.println("닉네임" + registerDto.getDisplayName());

        if (bindingResult.hasErrors()) {
            System.out.println("RegisterDto____: " + registerDto);
            System.out.println("bindingResult2" + bindingResult.getAllErrors());
            Map<String, Object> response = new HashMap<>();

            response.put("success", false);
            response.put("errors", bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .toArray());
            return ResponseEntity.badRequest().body(response);
        }

        try {
            registerService.register(registerDto);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            System.out.println("response!!!!" + response);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("errors", List.of(e.getMessage()));
            return ResponseEntity.badRequest().body(response);
        }
    }


    //아이디중복체크 api
    @PostMapping("/check-username")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkUsername(@RequestBody Map<String, String> request) {
        String username = request.get("username");  // 클라이언트가 보낸 JSON 객체에서 username 가져오기
        Map<String, Object> response = new HashMap<>();

        // 아이디 중복 체크
        if (memberRepository.existsByUsername(username)) {
            response.put("success+check-username", false);
            response.put("errors", new String[]{"이미 존재하는 아이디입니다."});
        } else {
            response.put("success+check-username2", true); // 잘작동
        }

        return ResponseEntity.ok(response);
    }
    
    //닉네임 중복
    @PostMapping("/check-displayName")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkDisplayName(@RequestBody Map<String, String> request) {
        String displayName = request.get("displayName");

        Map<String, Object> response = new HashMap<>();

        //닉네임 중복체크
        if(memberRepository.existsBydisplayName(displayName)) {
            response.put("success",false);
            response.put("errors",new String[]{"이미 존재하는 닉네임입니다."});
        } else {
            response.put("success",true);
        }
        return ResponseEntity.ok(response);
    }








}
