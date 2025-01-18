package com.kaige.controller.admin;

import com.kaige.entity.Moment;
import com.kaige.entity.Result;
import com.kaige.entity.dto.MomentInput;
import com.kaige.entity.dto.MomentView;
import com.kaige.service.MomentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/admin")
public class MomentAdminController {

    @Autowired
    MomentService momentService;

    /**
     * 分页查询动态
     */
    @GetMapping("/moments")
    public Result moments(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<MomentView> momentListOfPage = momentService.getMomentListOfPage(pageNum, pageSize);
        return Result.ok("请求成功",momentListOfPage);
    }

    /**
     * 更新动态公开状态
     */
    @PutMapping("/moments")
    public Result updatePublished(@RequestParam Long id, @RequestParam Boolean published) {
        momentService.updatePublished(id,published);
        return Result.ok("更新成功");
    }

    /**
     * 根据id查询动态详情
     */
    @GetMapping("/moment")
    public Result getMomentById(@RequestParam Long id) {
        Moment momentById = momentService.getMomentById(id);
        return Result.ok("请求成功",momentById);
    }

    /**
     * 根据id删除动态
     */
    @DeleteMapping("/moment")
    public Result deleteMomentById(@RequestParam BigInteger id) {
        momentService.deleteMomentById(id);
        return Result.ok("删除成功");
    }

    /**
     * 发布动态
     */
    @PostMapping("/moment")
    public Result saveMoment(@RequestBody MomentInput momentInput) {
        momentService.saveMoment(momentInput);
        return Result.ok("发布成功");
    }

    /**
     * 更新动态
     */
    @PutMapping("/moment")
    public Result updateMomentById(@RequestBody MomentInput momentInput) {
        momentService.updateById(momentInput);
        return Result.ok("更新成功");
    }

}
