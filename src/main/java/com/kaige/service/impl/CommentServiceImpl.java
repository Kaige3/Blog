package com.kaige.service.impl;

import com.kaige.entity.Comment;
import com.kaige.entity.Immutables;
import com.kaige.entity.dto.CommentInput;
import com.kaige.handler.exception.PersistenceException;
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
import java.util.Map;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Override
    public Page<Comment> getPageCommentList(Integer pageNum,Integer page, BigInteger blogId,Integer pageSize) {
        // 优化版 Optimized version
        Page<Comment> commentPage = commentRepository.getPageCommentListOV(pageNum,page, blogId,pageSize);
        List<Comment> commentList = commentPage.getRows();

        for (Comment comment : commentList) {
            // 获取所有的子评论
            List<Comment> list = new ArrayList<>();
            getReplyComments(list, comment.childComment());
            // 排序子评论列表
            list.sort(Comparator.comparing(Comment::createTime));
            System.out.println(list);
            // 创建新的 Comment 对象并更新其子评论列表
            Comment comment1 = Immutables.createComment(comment, newComment -> {
                newComment.setChildComment(list);  // 更新排序后的子评论列表
            });
            // 替换原评论列表中的评论
            commentList.set(commentList.indexOf(comment), comment1);
        }

        return commentPage;
    }

    @Override
    public Integer getcountByPageAndIsPublished(Integer page, BigInteger blogId, Boolean isPublished) {
        return commentRepository.getcountByPageAndIsPublished(page, blogId, isPublished);
    }

    @Override
    public Comment getCommentById(Integer parentCommentId) {
        return commentRepository.getCommentById(parentCommentId);
    }

    @Override
    public void saveComment(CommentInput adminComment) {
         if(!commentRepository.saveComment(adminComment)){
             throw new PersistenceException("评论失败");
         }
    }

    @Override
    public void deleteCommentByBlogId(BigInteger id) {
        Integer count = commentRepository.deleteCommentByBlogId(id);
        if(count == 0){
            throw new PersistenceException("删除失败");
        }
    }

    //    获取子评论列表 放在 list中
    private void getReplyComments(List<Comment> list, List<Comment> comments) {
        for(Comment c:comments){
            list.add(c);
            if(c.childComment() != null){
                getReplyComments(list,c.childComment());
            }
        }
    }
}
