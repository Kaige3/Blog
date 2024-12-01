package com.kaige.controller.web;

import com.kaige.constant.JwtConstant;
import com.kaige.entity.Blog;
import com.kaige.entity.Immutables;
import com.kaige.entity.Result;
import com.kaige.entity.User;
import com.kaige.entity.dto.BlogPassword;
import com.kaige.handler.exception.NotFoundException;
import com.kaige.service.BlogService;
import com.kaige.service.UserService;
import com.kaige.service.impl.UserServiceImpl;
import com.kaige.utils.JwtUtils;
import org.babyfish.jimmer.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
public class BlogController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private UserServiceImpl userService;

    /**
     * 按照创建时间 分页查询博客简要信息
     */
    @GetMapping("/blogs")
    public Result getBlogListByIsPublished(@RequestParam(defaultValue = "1") Integer pageNum) {
        Page<Blog> blogPage = blogService.getBlogListByIsPublished(pageNum);
        return Result.ok("获取成功", blogPage);
    }

    /**
     * 根据id 获取博客详细信息
     * @param id 文章id
     * @param jwt 密码保护文章的 访问token
     * @return
     */
    @GetMapping("/blog")
    public Result getBlog(@RequestParam Long id,
                          @RequestHeader(value = "Authorization", defaultValue = "") String jwt){
        Blog blogDetail = blogService.getBlogByIdAndIsPublished(id);
//        判断是否有密码
        if(!"".equals(blogDetail.password())){
//            判断token是否存在
            if(JwtUtils.judgeTokenIsExist(jwt)){
//                判断token是否正确
                try {
                    String subject = JwtUtils.getTokenBody(jwt).getSubject();
                    if(jwt.startsWith(JwtConstant.ADMIN_PREFIX)){
    //                  获取用户名 ： 去除前缀  得到用户名
                        String username = subject.replace(JwtConstant.ADMIN_PREFIX, "");
                        User admin = (User) userService.loadUserByUsername(username);
                        if(admin == null){
                            return Result.create(403,"博主身份已过期，请重新登录");
                        }
                    }else{
    //                    经密码验证后的token
                        Long tokenBlogId = Long.parseLong(subject);
    //                    判断token是否匹配 文章id
                        if(!tokenBlogId.equals(id)){
                            return Result.create(403,"token不匹配，请重新验证密码");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.create(403,"Token已失效，请重新验证密码");
                }
            }else {
                return Result.create(403,"此文章受密码保护，请验证密码");
            }
        }
        Blog blog = Immutables.createBlog(blogDetail, it -> {
            it.setPassword("");
        });
        return Result.ok("获取成功",blog);
    }
    @PostMapping("/checkBlogPassword")
    public Result checkBlogPassword(@RequestBody BlogPassword blogPassword){
        String passWord =  blogService.getBlogPassword(blogPassword.getId());
//        System.out.println(passWord);
        if (passWord.equals(blogPassword.getPassword())){
            String token = JwtUtils.generateToken(blogPassword.getId().toString(), 1000 * 3600 * 30L);
            return Result.ok("验证成功",token);
        }else {
            return Result.create(403,"密码错误");
        }
    }

}
