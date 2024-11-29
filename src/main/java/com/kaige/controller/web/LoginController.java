package com.kaige.controller.web;

import com.kaige.entity.Result;
import com.kaige.entity.User;
import com.kaige.entity.dto.UserInput;
import com.kaige.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 登录后 签发token
     * @param userInput
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody UserInput userInput){
        User user = userService.findByUsernameAndPassword(userInput.getUsername(), userInput.getPassword());
        return Result.ok("登录成功",user);
    }
}
