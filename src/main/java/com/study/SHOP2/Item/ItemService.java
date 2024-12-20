package com.study.SHOP2.Item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;


    //모든 item 목록조회
    public List<Item> getAllItems() {
        return itemRepository.findAll(); // List자료형으로 가져옴 findAll [   ]
    }

    // 아이템 저장 service
    //함수로 뺄땐 안에 있는 변수들도 따로 정의해야함
    public void saveItem(String title, Integer price) {
        Item item = new Item();
        item.setTitle(title);
        item.setPrice(price);
        itemRepository.save(item);
        /*
         * item.java에서 기본생성자를 만들면 item.setTitle(title); 이렇게 말고
         * Item item = new Item(title,price); 이렇게 한다 따로 불리시키지않음
         *
         * */
    }

    // 아이템 id 조회
    public Optional<Item> getItemById(Integer id) {
        return itemRepository.findById(id);
    }

    // 제목 유효성 검사
    public boolean isValidTitle(String title) {
        return title.matches(".*[a-zA-Z가-힣].*");  // 한글 또는 영문 포함 여부
    }

    // 가격 유효성 검사
    public boolean isValidPrice(Integer price) {
        return price > 0 && !price.toString().contains(".");  // 양의 정수인지, 소수점 포함 여부
    }

    //수정페이지
    public  Optional<Item> findItemById(Integer id) {
        return itemRepository.findById(id);
    }

    //수정페이지 post요청
    public Optional<Item> updateItem(Integer id, Item updateItem) {
        Optional<Item> existItem = itemRepository.findById(id);
        if (existItem.isPresent()) {

            Item item = existItem.get();

            if(updateItem.getTitle().length() > 100) {
                throw new IllegalArgumentException("타이틀은 100자 이하여야합니다.");
            }

            if(updateItem.getPrice() < 0) {
                throw new IllegalArgumentException("가격은 음수일수 없습니다.");
            }

            item.setTitle(updateItem.getTitle());
            item.setPrice(updateItem.getPrice());
            itemRepository.save(updateItem);

            return Optional.of(item);
        } else {
            return Optional.empty();
        }
    }
    
    //삭제
    public boolean deleteItemById(Integer id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
            return true;
        }
        return false;
    }







}
