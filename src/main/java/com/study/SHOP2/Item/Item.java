package com.study.SHOP2.Item;

import com.study.SHOP2.User.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@Getter
@Setter
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

   // @Column(nullable = false, columnDefinition = "TEXT")
    private String title; //title이라는 컬럼추가한거,추가적으로 검색기능만들때 검색대상임
    private Integer price;
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void setPrice(Integer price) {
        if (price == null ) {
            System.out.println("가격은 0원 이상이여야합니다. 기본값 0을 사용합니다.");
            this.price = 0;
        } else if(price < 0 ){
            System.out.println("가격은 음수일수 없습니다. 기존 가격을 유지합니다.");
        } else {
            this.price = price;
        }
    }

    public void setTitle (String title){
        if (title == null || title.trim().isEmpty()) {
            System.out.println("제목은 비어있을 수 없습니다. 기본값 'Unnamed'를 사용합니다.");
            this.title = "Unnamed"; // 기본값으로 설정
        } else {
            this.title = title;
        }
    }

//     기본 생성자
//    public Item() {
//    }
//
//     생성자
//    public Item(String title, Integer price) {
//        this.title = title;
//        this.price = price;
//    }


//    @Override
//    public String toString() {
//        return "Item{" +
//                "id=" + id +
//                ", title='" + title + '\'' +
//                ", price=" + price +
//                '}';
//    }
    //ALTER TABLE item ADD COLUMN image_url VARCHAR(255); 하자 추가로
}
