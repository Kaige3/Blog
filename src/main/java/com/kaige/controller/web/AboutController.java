package com.kaige.controller.web;

import com.kaige.constant.RedisKeyConstants;
import com.kaige.entity.*;
import com.kaige.service.RedisService;
import com.kaige.utils.markdown.MarkdownUtils;
import org.babyfish.jimmer.sql.JSqlClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AboutController {

     JSqlClient sqlClient;
    public AboutController(JSqlClient sqlClient) {
        this.sqlClient = sqlClient;
    }
    @Autowired
    private RedisService redisService;
    @GetMapping("/about")
    public Result about() {
//        先到缓存中查询信息
        String redisKey = RedisKeyConstants.ABOUT_INFO_MAP;
        Map<String, String> aboutInfoMapFromRedis = redisService.getMapByValue(redisKey);
        if(aboutInfoMapFromRedis!= null){
            return Result.ok("获取成功",aboutInfoMapFromRedis);
        }
//        否则从数据库中查询
        AboutTable about = AboutTable.$;
        List<About> abouts = sqlClient.createQuery(about)
                .select(about.fetch(
                        AboutFetcher.$.allScalarFields()
                )).execute();
        HashMap<String, String> aboutInfoMap = new HashMap<>();
        for (About about1 : abouts) {
            if("content".equals(about1.nameEn())){
//                将markdown语法 转换为 HTML，保存到map中
                String htmlContent = MarkdownUtils.markdownToHtmlExtensions(about1.value());
                aboutInfoMap.put(about1.nameEn(), htmlContent);
            }else
                aboutInfoMap.put(about1.nameEn(),about1.value());
        }
//        将数据保存到缓存中
        redisService.saveMapToValue(redisKey,aboutInfoMap);
        return Result.ok("获取成功",aboutInfoMap);
    }

}
