package com.kaige.controller.admin;

import com.kaige.entity.Result;
import com.kaige.entity.SiteSetting;
import com.kaige.service.SiteSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class SiteSettingAdminController {

    @Autowired
    SiteSettingService siteSettingService;

    /**
     * 获取所有站点信息
     */
    @GetMapping("/siteSettings")
    public Result siteSettings() {
        Map<String, List<SiteSetting>> typeMap = siteSettingService.getList();
        return Result.ok("请求成功",typeMap);
    }

    /**
     * 包含删除（部分可以为空，但是不能删除），添加（只能添加部分）和修改操作，
     */
    //TODO 注意一下前后端联调
    @PostMapping("/siteSettings")
    public Result updateAll(@RequestBody Map<String,Object> map){

        List<LinkedHashMap> siteSettings = (List<LinkedHashMap>) map.get("sittings");

        List<BigInteger> deleteIds = (List<BigInteger>) map.get("deleteIds");
        siteSettingService.updateSiteSetting(siteSettings,deleteIds);
        return Result.ok("更新成功");
    }

    /**
     * 查询网页标题后缀
     */
    @GetMapping("/webTitleSuffix")
    public Result webTitleSuffix() {
        String webTitleSuffix = siteSettingService.getWebTitleSuffix();
        return Result.ok("请求成功",webTitleSuffix);
    }
}
