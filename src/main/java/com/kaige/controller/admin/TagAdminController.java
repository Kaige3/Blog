package com.kaige.controller.admin;

import com.kaige.entity.*;
import com.kaige.entity.dto.TagInput;
import com.kaige.entity.dto.TagView;
import com.kaige.service.BlogService;
import com.kaige.service.TagService;
import com.kaige.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class TagAdminController {


    @Autowired
    TagService tagService;
    @Autowired
    BlogService blogService;
    /**
     * 分页查询标签列表
     */
    @GetMapping("/tags")
    public Result tags(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<TagView> tagViews = tagService.pageOfTags(pageNum, pageSize);
        return Result.ok("请求成功",tagViews);
    }

    /**
     * 保存标签
     */
    @PostMapping("/tag")
    public Result saveTag(@RequestBody TagInput tagInput) {
       return getResult(tagInput,"save");
    }

    /**
     * 更新标签
     */
    @PutMapping("/tag")
    public Result updateTag(@RequestBody TagInput tagInput) {
        return getResult(tagInput,"update");
    }

    /**
     * 保存或者更新的操作
     * 1.校验参数是否合法
     * 2.检查标签是否已经存在
     * @param tagInput
     * @param save
     * @return
     */
    private Result getResult(TagInput tagInput, String save) {
        if (StringUtils.isEmpty(tagInput.getName())){
            return Result.error("标签名不能为空");
        }
        //检查标签是否已经存在
        Tag tagByName = tagService.getTagByName(tagInput.getName());
        if (tagByName !=null && !tagByName.id().equals(tagInput.getId())){
            return Result.error("标签名已经存在");
        }
        // 保存或者更新
        if ("save".equals(save)){
            Tag produce = TagDraft.$.produce(draft -> {
                draft.setTagName(tagInput.getName());
                draft.setColor(tagInput.getColor());
            });
             tagService.saveTag(produce);
            return Result.ok("保存成功");
        }else {
            tagService.updateTag(tagInput);
            return Result.ok("更新成功");
        }
    }

    /**
     * 根据id删除标签
     */
    @DeleteMapping("/tag")
    public Result deleteTag(@RequestParam BigInteger id) {
        // 判断当前便签是否关联文章
        List<Blog> blogsByTagId = blogService.getBlogsByTagId(id);
        if (!blogsByTagId.isEmpty()){
            return Result.error("该标签已经关联文章，无法删除");
        }
        tagService.deleteTagById(id);
        return Result.ok("删除成功");
    }

}
