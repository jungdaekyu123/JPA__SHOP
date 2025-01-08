package com.study.SHOP2.Item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item,Integer> {

     // JpaRepository<Entity명,id컬럼타입> -> Item 들어가서

    Page<Item> findAll(Pageable page); // 원래 findPageBy라고 하나 메서드를 만들었는데 jpa에서 findAll이라는 기본제공 메소드가 있어서 사용
}
