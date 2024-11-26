package com.kaige.controller.web;

import com.kaige.entity.About;
import com.kaige.entity.AboutFetcher;
import com.kaige.entity.AboutTable;
import com.kaige.entity.Result;
import org.babyfish.jimmer.sql.JSqlClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
public class AboutController {

     JSqlClient sqlClient;

    public AboutController(JSqlClient sqlClient) {
        this.sqlClient = sqlClient;
    }
    @GetMapping("/about")
    public Result about() {
        AboutTable about = AboutTable.$;
        List<About> abouts = sqlClient.createQuery(about)
                .select(about.fetch(
                        AboutFetcher.$.allScalarFields()
                )).execute();
        HashMap<String, String> map = new HashMap<>();
        for (About about1 : abouts) {
//            map.put(about1., about1.getValue().toString());
            map.put(about1.nameEn(),about1.value());
        }
        return Result.ok("获取成功",map);
    }

}
