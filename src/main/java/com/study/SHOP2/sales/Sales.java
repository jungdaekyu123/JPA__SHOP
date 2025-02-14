package com.study.SHOP2.sales;


import com.study.SHOP2.User.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    private String itemName;
    private Integer price;
    private Integer count;
    private String username;
    private String displayName;
    @CreationTimestamp
    private LocalDateTime orderDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // FK
    private Member member;

    @PrePersist
    protected void onCreate() {
        this.orderDate = LocalDateTime.now();
    }

}
