package com.study.SHOP2;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;
    private final NoticeRepository noticeRepository;

//    @Autowired
//    public ItemController(ItemRepository itemRepository) {
//        this.itemRepository = itemRepository;
//    } lombok 쓰기 싫을때 서비스레이어 만들때

    @GetMapping("/list")
    String list(Model model){
      List<Item> result =  itemRepository.findAll(); // List자료형으로 가져옴 findAll
       // System.out.println(result);

        model.addAttribute("items", result);
        return "list.html";
    }

    @GetMapping("/notice")
    String notice(Model model) {
        List<Notice> result = noticeRepository.findAll();

        model.addAttribute("notices",result);
        return "notice.html";
    }

    @GetMapping("/write")
    String write() {
        return "write.html";
    }

    @PostMapping("/add")
    String addPost( @RequestParam(name="title") String title,
                   @RequestParam(name="price") Integer price) {
        Item item = new Item();
        item.setTitle(title);
        item.setPrice(price);
        itemRepository.save(item);
        
        /*
        * item.java에서 기본생성자를 만들면 item.setTitle(title); 이렇게 말고
        * Item item = new Item(title,price); 이렇게 한다 따로 불리시키지않음
        * 
        * */

        return "redirect:/list";
    }
}

//  1. repository 만들기 2. 원하는 클래스에 repository등록 3, repository.입출력문 등록