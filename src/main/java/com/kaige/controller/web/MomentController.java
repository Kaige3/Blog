package com.kaige.controller.web;

import com.kaige.constant.JwtConstant;
import com.kaige.entity.Moment;
import com.kaige.entity.Result;
import com.kaige.entity.User;
import com.kaige.entity.UserDraft;
import com.kaige.entity.dto.MomentView;
import com.kaige.service.MomentService;
import com.kaige.service.UserService;
import com.kaige.service.impl.UserServiceImpl;
import com.kaige.utils.JwtUtils;
import org.babyfish.jimmer.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apis")
public class MomentController {

    @Autowired
    private MomentService momentService;
    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/moments")
    public Result moments(@RequestParam(defaultValue = "1")Integer pageNum,
                          @RequestHeader(value = "Authorization",defaultValue = "")String jwt){
//        博主身份
        boolean adminIdentity = false;
        if(JwtUtils.judgeTokenIsExist(jwt)){
//              解析token
            try {
                String subject = JwtUtils.getTokenBody(jwt).getSubject();
                if(subject.startsWith(JwtConstant.ADMIN_PREFIX)){
    //                用户名
                    String username = subject.replace(JwtConstant.ADMIN_PREFIX, "");
                    User AdminUser = userService.findByUsernameAndpassword(username);
                    System.out.println("登录成功了");
                    if(AdminUser!= null){
                        adminIdentity = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Page<MomentView> momentPage = momentService.getMomentList(pageNum,adminIdentity);
        return Result.ok("获取成功",momentPage);
    }

    @PostMapping("/moment/like/{id}")
    public Result like(@PathVariable Long id){
        Integer i = momentService.addLikeByMomentId(id);
        if (i==1){
            return Result.ok("点赞成功");
        }else {
            return Result.error("点赞失败");
        }
    }
}
