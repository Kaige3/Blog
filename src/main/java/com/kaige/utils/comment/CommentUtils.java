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
import com.kaige.utils.HashUtils;
import com.kaige.utils.IpAddressUtils;
import com.kaige.utils.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.babyfish.jimmer.Immutable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;

@Component
//指定当前Bean 的初始化 依赖于springContextUtils，确保他们的加载顺序
@DependsOn("springContextUtils")
public class CommentUtils {

    @Autowired
    private AboutService aboutService;
    @Autowired
    private FriendService friendService;
    private static BlogService blogService;
//    private CommentNotifyChannel
    private Boolean commentDefaultOpen;

    @Value("true")
    public void setCommentDefaultOpen(Boolean commentDefaultOpen) {
        this.commentDefaultOpen = commentDefaultOpen;
    }

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
                 FriendInfoVo siteFriendInfo = friendService.getSiteFriendInfo();
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

    public void setAdminComment(CommentInput comment, HttpServletRequest request, User userDetails) {
//        第一种
//        Comment comment2 = Immutables.createComment(comment1, it -> {
//            it.setIp(IpAddressUtils.getIpAddress(request));
//        });

//        设置 博主评论的公共属性
        setGeneralAdminComment(comment, userDetails);
        comment.setIp(IpAddressUtils.getIpAddress(request));
//        return comment2;

//        第二种写法，略
//        CommetDraft.$.produce(draft -> draft.set);
    }

    private void setGeneralAdminComment(CommentInput comment, User userDetails) {

//        Comment comment1 = Immutables.createComment((Comment) comment, it -> {
//            it.setIsAdminComment(true);
//            it.setCreateTime(new DateTime().toLocalDateTime());
//            it.setAvatar(userDetails.avatar());
//            it.setWebsite("/");
//            it.setNickname(userDetails.nickname());
//            it.setEmail(userDetails.email());
//            it.setIsNotice(false);
//        });
        comment.setAdminComment(true);
        comment.setCreateTime(new DateTime().toLocalDateTime());
        comment.setAvatar(userDetails.avatar());
        comment.setWebsite("/");
        comment.setNickname(userDetails.nickname());
        comment.setEmail(userDetails.email());
        comment.setNotice(false);

//        return comment1;
    }

    public void setVisitorComment(CommentInput comment, HttpServletRequest request) {
        comment.setNickname(comment.getNickname().trim());
//        根据昵称Hash 设置头像
        setCommentRandomAvatar(comment);
        if(!isValidUrl(comment.getWebsite())){
            comment.setWebsite("");
        }
        comment.setAdminComment(false);
        comment.setCreateTime(new DateTime().toLocalDateTime());
        comment.setPublished(commentDefaultOpen);
        comment.setEmail(comment.getEmail().trim());
        comment.setIp(IpAddressUtils.getIpAddress(request));
//                comment.setIsAdminComment(false);
//                .setPublished(commentDefaultOpen)
//                .setEmail(comment.getEmail().trim())
//                .setIp(IpAddressUtils.getIpAddress(request))
//                .setCreateTime(new DateTime().toLocalDateTime()));
    }

    //设置 随机头像 对于昵称不是QQ号的评论，根据昵称Hash设置 头像
    private void setCommentRandomAvatar(CommentInput comment) {
        //设置随机头像
        //根据评论昵称 取hash 确保每一个昵称 对应一个头像
        long murmurHash32 = HashUtils.getMurmurHash32(comment.getNickname());
        long num = murmurHash32 % 6 + 1;
        String avatar = "/img/comment-avatar/" + num + ".jpg";
        comment.setAvatar(avatar);
    }

    /**
     * URL合法性校验
     *
     * @param url url
     * @return 是否合法
     */
    private static boolean isValidUrl(String url) {
        return url.matches("^https?://([^!@#$%^&*?.\\s-]([^!@#$%^&*?.\\s]{0,63}[^!@#$%^&*?.\\s])?\\.)+[a-z]{2,6}/?");
    }
}
