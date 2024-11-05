package com.study.SHOP2;

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
    private String title; //title이라는 컬럼추가한거
    private Integer price;

    public void setPrice(Integer price) {
        if (price == null || price < 0) {
            System.out.println("가격은 0원 이상이여야합니다. 기본값 0을 사용합니다.");
            this.price = 0;
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
}
