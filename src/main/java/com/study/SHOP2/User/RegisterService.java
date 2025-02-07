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
        checkDuplicatedisplayName(registerDto.getDisplayName());

        // 새로운 회원 정보 저장
        Member member = new Member();
        member.setUsername(registerDto.getUsername());
        member.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        member.setDisplayName(registerDto.getDisplayName());
        member.setRole(Role.ROLE_USER);

        Member savedMember = memberRepository.save(member);
        System.out.println("유저저장 " + savedMember);

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

        //아이디 중복확인
        private void checkDuplicateUsername(String username) {
            boolean exists = memberRepository.existsByUsername(username);
            //System.out.println("Check Duplicate Username: " + username + ", Exists: " + exists);
            if (exists) {
                throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
            }
        }


        private void checkDuplicatedisplayName(String displayName) {
            boolean exists = memberRepository.existsByDisplayName(displayName);
            //System.out.println("Check Duplicate DisplayName: " + displayName + ", Exists: " + exists);
            if (exists) {
                throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
            }
    }
    
    // 관리자를 분리해보자
    public void registerAdmin(RegisterDto registerDto) {

        validateRegisterDto(registerDto);
        checkDuplicateUsername(registerDto.getUsername());
        checkDuplicatedisplayName(registerDto.getDisplayName());

        Member admin = new Member();
        admin.setUsername(registerDto.getUsername());
        admin.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        admin.setDisplayName(registerDto.getDisplayName());
        admin.setRole(Role.ROLE_ADMIN); // 관리자 권한 설정

        Member saveAdmin = memberRepository.save(admin);
        System.out.println("관리자저장 " + saveAdmin);
    }







    }
// 흐름 회원가입 요청 register() 메서드 호출 -> RegisterDto객체(사용자가입력한거) 받아옴
// 입력값 검증 validateRegisterDto(registerDto);