package com.study.SHOP2.Item;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item,Integer> {

     // JpaRepository<Entity명,id컬럼타입> -> Item 들어가서
}
