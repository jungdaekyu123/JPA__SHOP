package com.study.SHOP2.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(RegisterDto registerDto) {

        validateRegisterDto(registerDto);
        checkDuplicateUsername(registerDto.getUsername());

        // 새로운 회원 정보 저장
        Member member = new Member();
        member.setUsername(registerDto.getUsername());
        member.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        member.setDisplayName(registerDto.getDisplayName());
        memberRepository.save(member);
    }



        private void validateRegisterDto(RegisterDto registerDto) {
            if (registerDto.getUsername() == null || registerDto.getUsername().isBlank()) {
                throw new IllegalArgumentException("아이디 입력부탁");
            }
            if (registerDto.getPassword() == null || registerDto.getPassword().isBlank()) {
                throw new IllegalArgumentException("비밀번호 입력부탁");
            }
            if (registerDto.getDisplayName() == null || registerDto.getDisplayName().isBlank()) {
                throw new IllegalArgumentException("닉네임 입력부탁");
            }
        }

        // 아이디 중복 체크
        private void checkDuplicateUsername(String username) {
            if (memberRepository.findByUsername(username) != null) {
                throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
            }
        }

        //닉네임 중복체크
        private void checkDuplicatedisplayName(String displayName) {
         if (memberRepository.findBydisplayName(displayName) != null) {
             throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
           }
        }

    }
// 흐름 회원가입 요청 register() 메서드 호출 -> RegisterDto객체(사용자가입력한거) 받아옴
// 입력값 검증 validateRegisterDto(registerDto);