package com.kaige.utils;

import com.kaige.constant.JwtConstant;
import com.kaige.entity.Result;
import com.kaige.entity.User;
import com.kaige.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class TokenAndPasswordVerify {

    private static UserServiceImpl userService;

    @Autowired
    public TokenAndPasswordVerify(UserServiceImpl userService) {
        this.userService = userService;
    }

    public static Result judgeTokenAndPasswordIsOK(String jwt, BigInteger id) {
        if (JwtUtils.judgeTokenIsExist(jwt)) {
            try {
                String subject = JwtUtils.getTokenBody(jwt).getSubject();
                if (subject.startsWith(JwtConstant.ADMIN_PREFIX)) {
                    //博主身份Token
                    String username = subject.replace(JwtConstant.ADMIN_PREFIX, "");
                    User admin = (User) userService.loadUserByUsername(username);
                    if (admin == null) {
                        return Result.create(403, "博主身份Token已失效，请重新登录！");
                    }
                } else {
                    //经密码验证后的Token
                    Long tokenBlogId = Long.parseLong(subject);
                    //博客id不匹配，验证不通过，可能博客id改变或客户端传递了其它密码保护文章的Token
                    if (!tokenBlogId.equals(id)) {
                        return Result.create(403, "Token不匹配，请重新验证密码！");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return Result.create(403, "Token已失效，请重新验证密码！");
            }
        } else {
            return Result.create(403, "此文章受密码保护，请验证密码！");
        }
        return Result.create(200, "Token验证成功，用户已通过验证！");
    }
}
