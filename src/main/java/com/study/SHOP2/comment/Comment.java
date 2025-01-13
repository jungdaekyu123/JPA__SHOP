package com.study.SHOP2.comment;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@ToString
public class Comment {
    //댓글 엔티티

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    // 댓글총글자갯수 1000자정도
    @Column(length = 1000)
    private String content;

    private Integer parentId;


}
