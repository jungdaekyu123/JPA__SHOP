package com.study.SHOP2.comment;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor // 자동으로 생성자 만들어줌
public class CommentService {

    private final CommentRepository commentRepository;
    
    //댓글목록가져오기
    public List<Comment> getCommentsByParentId(Integer parentId) {
        return commentRepository.findByParentId(parentId);
    }
    
    public Comment saveComment(String username, String content, Integer parentId) {

        Comment comment = new Comment(); // 이게 뭔지 잘모르겠네 왜 new를 쓰는지 엔티티에서 오브젝트를 하나 뽑아서
        comment.setUsername(username);
        comment.setContent(content);
        comment.setParentId(parentId);

        return commentRepository.save(comment);
    }

}
