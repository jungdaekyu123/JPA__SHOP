package com.study.SHOP2.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment")
    @ResponseBody
    public Comment saveComment(String content, Integer parentId, Authentication authentication) {

        String username = authentication.getName();
        //System.out.println("뭐가 들어왔을까? : " + username + "  " + content + " " + parentId);
        Comment savedComment = commentService.saveComment(username,content,parentId);
        return savedComment;

    }


    // 댓글목록 받는api
    @GetMapping("/comments/{parentId}")
    @ResponseBody
    public List<Comment> getComments(@PathVariable Integer parentId) {
        return commentService.getCommentsByParentId(parentId);
    }








    /*
     * 댓글정리
     * 브라우저 -> controller -> service -> repository -> DB저장
     * 페이지로드 -> ajax요청(get) -> controller -> service -> repo -> DB에서 댓글가져오기
     * */
}
