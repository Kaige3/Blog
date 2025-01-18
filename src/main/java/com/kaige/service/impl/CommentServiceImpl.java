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
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public org.springframework.data.domain.Page<Comment> getCommentListOfPage(Integer page, BigInteger pageId, Integer pageNum, Integer pageSize) {
        return commentRepository.getCommentListOfPage(page,pageId,pageNum,pageSize);
    }

    @Override
    public void updateCommentPublished(Integer id, Boolean published) {
        // 如果是隐藏评论 则将其所有子评论也隐藏
        //1.查询改父评论下的所有子评论
        if(!published){
            List<Comment> commentList = commentRepository.getCommentListByParentId(id);
            for(Comment comment:commentList){
        //2.递归修改评论公开状态
                  hideComment(comment);
            }
        }
        commentRepository.updateCommentPublished(id,published);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCommentNotice(Integer id, Boolean notice) {
        try {
            commentRepository.updateCommentNotice(id,notice);
        } catch (Exception e) {
            throw new PersistenceException("更新失败");
        }
    }

    /**
     * 删除id下的所有评论
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCommentById(Integer id) {
        //1.查询改父评论下的所有子评论
        List<Comment> commentList = commentRepository.getCommentListByParentId(id);
        //2.递归删除评论
        for(Comment comment:commentList){
            deleteComment(comment);
        }
        try {
            commentRepository.deleteCommentById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateComment(CommentInput commentInput) {
        try {
            commentRepository.update(commentInput);
        } catch (Exception e) {
            throw new PersistenceException("修改评论失败");
        }
    }

    /**
     * 递归删除评论
     * @param comment
     */
    private void deleteComment(Comment comment) {
        for(Comment c:comment.childComment()){
            if(c.childComment()!= null){
                deleteComment(c);
            }
        }
        try {
            commentRepository.deleteCommentById(comment.id());
        } catch (Exception e) {
            throw new PersistenceException("删除失败");
        }
    }

    /**
     * 递归隐藏子评论
     * @param comment
     */
    private void hideComment(Comment comment) {
        for(Comment c:comment.childComment()){
            if(c.childComment()!= null){
                hideComment(c);
            }
        }
        commentRepository.updateCommentPublished(comment.id(),false);
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
