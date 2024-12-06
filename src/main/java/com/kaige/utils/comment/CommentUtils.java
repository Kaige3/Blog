package com.kaige.utils.comment;

import cn.hutool.core.date.DateTime;
import com.kaige.config.LocalDateTimeConvert;
import com.kaige.constant.PageConstants;
import com.kaige.entity.Comment;
import com.kaige.entity.CommentDraft;
import com.kaige.entity.Immutables;
import com.kaige.entity.User;
import com.kaige.entity.dto.CommentInput;
import com.kaige.entity.vo.FriendInfoVo;
import com.kaige.enums.CommentOpenStateEnum;
import com.kaige.service.AboutService;
import com.kaige.service.BlogService;
import com.kaige.service.FriendService;
import com.kaige.utils.IpAddressUtils;
import com.kaige.utils.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.babyfish.jimmer.Immutable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class CommentUtils {

    @Autowired
    private AboutService aboutService;
    @Autowired
    private FriendService friendService;
    private static BlogService blogService;
    public  void setBlogService(BlogService blogService) {
        CommentUtils.blogService = blogService;
    }


    public CommentOpenStateEnum judgeCommentState(Integer page,BigInteger blogId){
         switch (page){
             case PageConstants.BLOG:
//                 博客页面
                 Boolean commentEnabled = blogService.getCommentEnabledByBlogId(blogId);
                 Boolean published = blogService.getPublishedByBlogId(blogId);
                 if(commentEnabled == null || published == null){
//                     未找到此博客
                     return CommentOpenStateEnum.NOT_FOUND;
                 }
                 else if(!published) {
//                     博客为公开
                     return CommentOpenStateEnum.NOT_FOUND;
                 } else if (!commentEnabled) {
//                     评论已关闭
                     return CommentOpenStateEnum.CLOSE;
                 }
                 String blogPassword = blogService.getBlogPassword(blogId);
                 if (!StringUtils.isEmpty(blogPassword)) {
//                  密码不正确
                     return CommentOpenStateEnum.PASSWORD;
                 }
                 break;
             case PageConstants.ABOUT:
                 if(!aboutService.getAboutCommentEnabled()) {
//                     页面已关闭评论
                     return CommentOpenStateEnum.CLOSE;
                 }
                 break;
             case PageConstants.FRIEND:
                 FriendInfoVo siteFriendInfo = friendService.getSiteFriendinfo();
                 if (!siteFriendInfo.getCommentEnabled()){
//                     评论页面已关闭
                     return CommentOpenStateEnum.CLOSE;
                 }
                 break;
             default:
                 break;
            }
            return CommentOpenStateEnum.OPEN;
         }

    public Comment setAdminComment(CommentInput comment, HttpServletRequest request, User userDetails) {
        Comment comment1 = setGeneralAdminComment(comment, userDetails);
        Comment comment2 = Immutables.createComment(comment1, it -> {
            it.setIp(IpAddressUtils.getIpAddress(request));
        });
        return comment2;
    }

    private Comment setGeneralAdminComment(CommentInput comment, User userDetails) {

        Comment comment1 = Immutables.createComment((Comment) comment, it -> {
            it.setIsAdminComment(true);
            it.setCreateTime(new DateTime().toLocalDateTime());
            it.setAvatar(userDetails.avatar());
            it.setWebsite("/");
            it.setNickname(userDetails.nickname());
            it.setEmail(userDetails.email());
            it.setIsNotice(false);
        });
        return comment1;
    }

}
