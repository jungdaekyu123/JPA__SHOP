package com.study.SHOP2.Item;

import com.study.SHOP2.User.Member;
import com.study.SHOP2.User.MemberRepository;
import com.study.SHOP2.User.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    // DB입출력하려면 뭐부터 해야함 3스탭
    // 1. repository interface 만들고
    // 2. 등록하고
    // 3. 사용

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;


    //모든 item 목록조회
    public List<Item> getAllItems() {
        return itemRepository.findAll(); // List자료형으로 가져옴 findAll [   ]
    }

    // 아이템 저장 service
    //함수로 뺄땐 안에 있는 변수들도 따로 정의해야함
    public void saveItem(String title, Integer price, Authentication authentication,String imageUrl) {

        // 상세페이지 업로드안하면 기본값 고정
        if(imageUrl == null || imageUrl.trim().isEmpty()) {
            imageUrl = "https://placehold.co/300";
        } 

        Item item = new Item();
        item.setTitle(title);
        item.setPrice(price);
        item.setImageUrl(imageUrl);


        String username = authentication.getName();
     //   System.out.println("username" + username);
        Optional<Member> memberOptional = memberRepository.findByUsername(username);

        if(memberOptional.isPresent()) {
            Member member = memberOptional.get();
            item.setMember(member);
        } else {
            throw new IllegalArgumentException("로그인된 유저를 찾을수 없다");
        }

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

    //페이지나누기(pagination)
    private static final int PAGE_SIZE = 3;
    public Page<Item> getPaging(int page) {
        if (page < 1) {
           page = 1;
        }
        int currentPage = page -1;

        return itemRepository.findAll(PageRequest.of(currentPage,PAGE_SIZE, Sort.by(Sort.Direction.DESC,"id")));
        // id대신 createdAt을 사용 해보기
    }

    //검색기능
    public List<Item> searchItem(String keyword) {
       // System.out.println("서비스 레벨 키워드: " + keyword);
        keyword = keyword.trim();
        return itemRepository.findByTitleContainingIgnoreCase(keyword);
    }







}
