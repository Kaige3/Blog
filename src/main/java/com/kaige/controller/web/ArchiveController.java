package com.kaige.controller.web;

import com.kaige.entity.Result;
import com.kaige.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ArchiveController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/archives")
    public Result archives(){
        Map<String,Object> archiveBlogMap = blogService.getArchiveBlogAndCountByIsPublished();
        return Result.ok("获取成功",archiveBlogMap);
    }
}
