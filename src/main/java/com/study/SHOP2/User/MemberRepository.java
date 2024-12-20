package com.study.SHOP2.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {

    // JpaRepository<Entity명,id컬럼타입> -> Member

    // 멤버 찾는 메서드
    Member findByUsername(String username);

    // 아이디 중복 체크 메서드
    boolean existsByUsername(String username);

    //닉네임 찾는 메서드
    Member findBydisplayName(String displayName);

    //닉네임중복
    boolean existsBydisplayName(String displayName);



}
