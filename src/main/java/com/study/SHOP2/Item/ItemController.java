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
    private final S3Service s3Service;


//    @Autowired
//    public ItemController(ItemRepository itemRepository) {
//        this.itemRepository = itemRepository;
//    } lombok 쓰기 싫을때 서비스레이어 만들때
    
    //목록
    @GetMapping("/list")
    String redirectToFirstPage() {
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
                                Authentication authentication,
                                @RequestParam String imageUrl
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

        itemService.saveItem(title,price,authentication,imageUrl);
        mav.setViewName("redirect:/list");
        return mav;
    }

    //상세페이지 -> url 파라미터
    @GetMapping("/detail/{id}")
    String detail(@PathVariable Integer id, Model model) {

            Optional<Item> result = itemService.getItemById(id);
            if (result.isPresent()) {
                Item item = result.get();
              //  System.out.println("조회된 아이템: " + item);  // 디버깅용 출력
                model.addAttribute("items", result.get());
            } else {
                model.addAttribute("errorMessage", "해당 아이템을 찾을 수 없습니다.");
                return "redirect:/list/page/1";
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
        if (page == null || page < 1) {
            page = 1;
        }
        // 한페이지네이션에서 보여줄 페이지수
        int pageGroupSize = 10;

        //페이지네이션 그룹계산

        
        // 페이징 데이터 서비스부분에서 받아오기
        Page<Item> pageN = itemService.getPaging(page);
        int currentGroup = (page -1) / pageGroupSize;
        int startPage = currentGroup * pageGroupSize + 1;
        int endPage = Math.min(startPage + pageGroupSize -1, itemService.getPaging(page).getTotalPages());
        
        model.addAttribute("items", pageN);// 현재 페이지의 아이템 목록
        model.addAttribute("currentPage", page); // 현재 페이지 번호
        model.addAttribute("totalPages", pageN.getTotalPages()); // 총 페이지 수
        model.addAttribute("startPage", startPage); // 현재 그룹의 시작 페이지 번호
        model.addAttribute("endPage", endPage); // 현재 그룹의 끝 페이지 번호

        return "list.html";
    }


    // S3이미지
    @GetMapping("/presigned-url")
    @ResponseBody // 유저에게 보낼때 씀
    String getURL(@RequestParam String filename) {
        //System.out.println(filename);
         var result =  s3Service.createPresignedUrl("test/" + filename);
         //System.out.println("s3테스트 :" + result);
        return result;
    }


    //검색기능
    @GetMapping( "/search")
    public String search(@RequestParam(required = false) String keyword,
                         @RequestParam(defaultValue = "1") int page,
                         Model model) {
       // System.out.printf("검색 키워드 : " + keyword);

        if (page < 1) {
            page = 1;
        }
        if (keyword == null || keyword.isEmpty()) {
            keyword = ""; // 기본값 설정
        }

        int pageSize = 3;
        int pageGroupSize = 10;


        Page<Item> result = itemService.searchItemFullText(keyword, page, pageSize);
        int currentGroup = (page - 1) / pageGroupSize;
        int startPage = currentGroup * pageGroupSize + 1;
        int endPage = Math.min(startPage + pageGroupSize - 1, result.getTotalPages());


        model.addAttribute("items", result); // key, value
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "list.html";
    }


   }



//  1. repository 만들기 2. 원하는 클래스에 repository등록 3, repository.입출력문 등록