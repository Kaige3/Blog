package com.kaige.service.impl;

import com.kaige.entity.Comment;
import com.kaige.entity.Immutables;
import com.kaige.repository.CommentRepository;
import com.kaige.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.babyfish.jimmer.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Override
    public List<Comment> getPageCommentList(Integer page, Long blogId) {
        List<Comment> commentList = commentRepository.getPageCommentList(page, blogId);

        for (Comment comment : commentList) {
            // 获取所有的子评论
            List<Comment> list = new ArrayList<>();
            getReplyComments(list, comment.childComment());

            // 排序子评论列表
            list.sort(Comparator.comparing(Comment::createTime).reversed());
            System.out.println(list);
            // 创建新的 Comment 对象并更新其子评论列表
            Comment comment1 = Immutables.createComment(comment, newComment -> {
                newComment.setChildComment(list);  // 更新排序后的子评论列表
            });


            // 替换原评论列表中的评论
            commentList.set(commentList.indexOf(comment), comment1);
        }

        // 如果你需要排序父评论的 createTime
//        commentList.sort(Comparator.comparing(Comment::createTime));

        return commentList;
    }

//    public List<Comment> getPageCommentList(Integer page, Long blogId) {
//            List<Comment> commentList = commentRepository.getPageCommentList(page,blogId);
//            for (Comment comment : commentList){
//                List<Comment> list = new ArrayList<>();
//                getReplyComments(list,comment.childComment());
////                排序子评论列表
//                Comparator<Comment> comparing = Comparator.comparing(Comment::id);
//                list.sort(comparing);
//
//                Comment comment1 = Immutables.createComment(comment, newComment -> {
//                    newComment.setChildComment(list);
//                });
//                commentList.set(commentList.indexOf(comment),comment1);
//            }
//            return commentList;
//    }

    private void getReplyComments(List<Comment> list, List<Comment> comments) {
        for(Comment c:comments){
            list.add(c);
            if(c.childComment() != null){
                getReplyComments(list,c.childComment());
            }
        }
    }
}
