package com.study.SHOP2.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    // JpaRepository<Entity명,id컬럼타입> -> Member

    // Derived query methods
    // and,or조건 줄수있음
    // 특정문자 포함되었는지 검색
    // 특정 숫자이상/이하 검색
    //정렬 가능


    // 아이디 중복 체크 메서드
    boolean existsByUsername(String username);

    //닉네임 찾는 메서드
    Optional<Member> findByDisplayName(String displayName);

    //닉네임중복
    boolean existsByDisplayName(String displayName);

    // 멤머찾기 및 로그인
    Optional<Member> findByUsername(String username);







}
