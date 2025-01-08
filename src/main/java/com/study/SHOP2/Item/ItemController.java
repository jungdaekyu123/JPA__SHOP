package com.study.SHOP2.Item;

import com.study.SHOP2.Notice;
import com.study.SHOP2.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
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
        //return "list.html";
        return "redirect:/list/page/1";
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
                                @RequestParam(name = "price") Integer price,
                                Authentication authentication
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

        itemService.saveItem(title,price,authentication);
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



    //수정페이지
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id ,Model model) {

        Optional<Item> result =  itemService.findItemById(id);
//        System.out.println("Requested ID: " + id);
//        System.out.println("Result: " + result);
        if (result.isPresent()) {
            model.addAttribute("data",result.get());
            return "edit.html";
        } else  {
            model.addAttribute("errorMessage","해당 ID의 항목을 찾을수 없습니다.");
            return "redirect:/list";
        }
    }
    
    //수정페이지 post요청
    @PostMapping("/edit/{id}")
    public String update(@PathVariable Integer id, @ModelAttribute Item item,Model model) {

        if(item.getPrice() == null || item.getPrice() < 0 ) {
            model.addAttribute("errorMessage","가격은 필수 입력, 마이너스값 안됌");
            model.addAttribute("data", item); // 입력된 데이터를 다시 반환해야 유효성검사 및 예외발생했을 경우 입력했던값을 다시 전달가능
            return "edit.html";
        }

        try {
            Optional<Item> updateItem = itemService.updateItem(id, item);
            if (updateItem.isPresent()) {
                return "redirect:/list";

            } else {
                model.addAttribute("errorMessage", "해당 ID의 항목 찾을수 없음");
                model.addAttribute("data", item);
                return "edit.html";
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("data", item);
            return "edit.html";
            }
        }


        @PostMapping("/delete")
        @ResponseBody
        public ResponseEntity<String> deleteItem(@RequestParam Integer id) {
            boolean isDeleted = itemService.deleteItemById(id);
            if (isDeleted) {
                return ResponseEntity.ok("삭제 성공");
            } else {
                return ResponseEntity.status(404).body("삭제 실패: 아이템을 찾을 수 없습니다.");
            }
        }

    //페이지나누기(pagination)
    @GetMapping("/list/page/{page}")
    public String pagination(Model model, @PathVariable(value = "page",required = false) Integer page) {

//       int currentPage = 0; // 기본값 설정
//        if(page !=null && page > 0 ) {
//            currentPage = page -1;
//         }

//        int currentPage;
//        if(page == null) {
//            currentPage = 0; // 페이지가 null이면 0으로 설정
//        } else if (page > 0) {
//            currentPage = page - 1; // 올바른 페이지 번호면 클라이언트가보낸 파라미터 -1
//        } else {
//            currentPage = 0; // 잘못된 페이지 번호도 0으로
//        }

        // 개선점 pathVariable이 null일경우 대비
        if(page == null || page < 1 ) {
            page = 1;
        }

        Page<Item> pageN = itemService.getPaging(page);
        model.addAttribute("items", pageN);// 현재 페이지의 아이템 목록
        model.addAttribute("currentPage", page); // 현재 페이지 번호
        model.addAttribute("totalPages", pageN.getTotalPages()); // 총 페이지 수

        return "list.html";
    }












//
//        @PostMapping ("/test1")
//        String test(@RequestBody Map<String, Object> body) {
//            System.out.println(body);
//         return "redirect:/list";
//        }


    }



//  1. repository 만들기 2. 원하는 클래스에 repository등록 3, repository.입출력문 등록