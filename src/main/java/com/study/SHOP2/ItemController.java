package com.study.SHOP2;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;
    private final NoticeRepository noticeRepository;
    private final ItemService itemService;

//    @Autowired
//    public ItemController(ItemRepository itemRepository) {
//        this.itemRepository = itemRepository;
//    } lombok 쓰기 싫을때 서비스레이어 만들때
    
    //목록
    @GetMapping("/list")
    String list(Model model) {
        List<Item> result = itemService.getAllItems();
        // System.out.println(result);
        model.addAttribute("items", result);
        return "list.html";
    }

    @GetMapping("/notice")
    String notice(Model model) {
        List<Notice> result = noticeRepository.findAll();
        model.addAttribute("notices", result);
        return "notice.html";
    }
    
    //아이템 넣는 api 추후 관리자만 볼수있게 변환
    @GetMapping("/write")
    String write() {
        return "write.html";
    }

    @PostMapping("/add")
    public ModelAndView addPost(@RequestParam(name = "title") String title,
                                @RequestParam(name = "price") Integer price
    ) {

        ModelAndView mav = new ModelAndView();

        if (!itemService.isValidTitle(".*[a-zA-Z가-힣].*")) {

            mav.addObject("errorMessage", "제목에 한글 또는 영문자 포함해야됌");
            mav.setViewName("addForm");
            return mav;
        }

        if (!itemService.isValidPrice(price)) {
            mav.addObject("errorMessage", "가격은 양의 정수로 입력해야 합니다.");
            mav.setViewName("addForm");
            return mav;
        }

        itemService.saveItem(title,price);
        mav.setViewName("redirect:/list");
        return mav;
    }

    //상세페이지 -> url 파라미터
    @GetMapping("/detail/{id}")
    String detail(@PathVariable Integer id, Model model) {

            Optional<Item> result = itemService.getItemById(id);
            if (result.isPresent()) {
                model.addAttribute("items", result.get());
            } else {
                model.addAttribute("errorMessage", "해당 아이템을 찾을 수 없습니다.");
            }
            return "detail.html";

    }


}

//  1. repository 만들기 2. 원하는 클래스에 repository등록 3, repository.입출력문 등록