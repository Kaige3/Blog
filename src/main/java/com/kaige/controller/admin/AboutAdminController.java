package com.kaige.controller.admin;

import com.kaige.entity.Result;
import com.kaige.service.AboutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AboutAdminController {

    @Autowired
    AboutService aboutService;

    /**
     * 获取关于我的页面
     */
    @GetMapping("/about")
    public Result about(){
        return Result.ok("请求成功",aboutService.getAboutInfo());
    }

    /**
     * 修改关于我的页面的信息
     */
    @PutMapping("/about")
    public Result updateAbout(@RequestBody Map<String,String> map){
        aboutService.updateAbout(map);
        return Result.ok("修改成功");
    }
}
