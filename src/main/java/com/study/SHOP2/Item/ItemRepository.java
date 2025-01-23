package com.study.SHOP2.Item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item,Integer> {

     // JpaRepository<Entity명,id컬럼타입> -> Item 들어가서

    Page<Item> findAll(Pageable page); // 원래 findPageBy라고 하나 메서드를 만들었는데 jpa에서 findAll이라는 기본제공 메소드가 있어서 사용

    List<Item> findByTitleContainingIgnoreCase(String keyword);

    @Query(value = "select * from item where MATCH(title) AGAINST (?1 IN NATURAL LANGUAGE MODE)",
            countQuery = "SELECT COUNT(*) FROM item WHERE MATCH(title) AGAINST (?1 IN NATURAL LANGUAGE MODE)",
            nativeQuery = true) // match -> title컬럼검색 against -> 사용자 입력값 , IN NATURAL LANGUAGE MODE 자연어검색 더 복잡한 검색은 in boolean mode로 한다고하는데 다음에 알아보자
    Page<Item> searchByTitleFullText(String keyword,Pageable pageable);
}
