package com.study.SHOP2.User;

import com.study.SHOP2.sales.Sales;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Getter
@Setter
@ToString(exclude = {"password","salesList"})
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="username",nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "displayName")
    private String displayName;

    //양방향 매핑 : Member -> sales
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Sales> salesList = new ArrayList<>();

    /*양방향매핑 정리
    * mappedBy = "member" 는 sales 엔티티의 member 필드가 이 관계를 관리한다는 뜻
    * 즉 외래키는 sales 테이블에 저장되고 Member는 읽기 전용으로 관리
    *
    * cascade = CascadeType.ALL 부모(member)가 삭제되면 자식(Sales)도 함께 삭제되는옵션
    *
    * orphanRemoval = true -> salesList에서 주문을 제거하면 DB에서도 해당 주문이 삭제
    *
    *
    * */

    @Enumerated(EnumType.STRING)
    private Role role;

}
