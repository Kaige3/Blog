package com.kaige.controller.admin;

import com.kaige.entity.Category;
import com.kaige.entity.CategoryDraft;
import com.kaige.entity.Result;
import com.kaige.entity.dto.CategoryInput;
import com.kaige.entity.dto.CategoryView;
import com.kaige.service.BlogService;
import com.kaige.service.CategoryService;
import com.kaige.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/admin")
public class  CategoryAdminController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    BlogService blogService;

    @GetMapping("/categories")
    public Result categories(@RequestParam(defaultValue = "1") Integer pageNum,@RequestParam(defaultValue = "10") Integer pageSize) {
        Page<CategoryView> categoryListOfPage = categoryService.getCategoryListOfPage(pageNum, pageSize);
        return Result.ok("请求成功",categoryListOfPage);
    }


    /**
     * 保存分类
     * @param categoryInput
     * @return
     */
    @PostMapping("/category")
    public Result category(@RequestBody CategoryInput categoryInput){
        return getResult(categoryInput,"save");
    }

    /**
     * 更新分类
     * @param categoryInput
     * @return
     */
    @PutMapping("/category")
    public Result saveCategory(@RequestBody CategoryInput categoryInput){
        return getResult(categoryInput,"update");
    }
    /**
     * 新增分类或者更新操作
     * 1. 校验参数是否合法，
     * 2. 检验分类是否已经存在
     */
    private Result getResult(CategoryInput categoryInput,String type){
        if (StringUtils.isEmpty(categoryInput.getCategoryName())){
            return Result.error("分类名称不能为空");
        }
        // 分类是否已经存在
        Category categoryByName = categoryService.getCategoryByName(categoryInput.getCategoryName());
        if (categoryByName !=null && !categoryByName.id().equals(categoryInput.getId())){
            return Result.error("分类名称已经存在");
        }
        if ("save".equals(type)){
            Category produce = CategoryDraft.$.produce(draft -> draft.setCategoryName(categoryInput.getCategoryName()));
            categoryService.saveCategory(produce);
            return Result.ok("添加成功");
        }else {
            Category produce = CategoryDraft.$.produce(draft -> draft.setCategoryName(categoryInput.getCategoryName()).setId(categoryInput.getId()));
            categoryService.updateCategory(produce);
            return Result.ok("更新成功");
        }
    }

    /**
     * 删除分类
     */
    @DeleteMapping("/category")
    public Result deleteCategory(@RequestParam BigInteger id){
        long l = blogService.countBlogByCategoryId(id);
        if (l != 0){
            return Result.error("该分类下有博客，无法删除");
        }
        categoryService.deleteCategory(id);
        return Result.ok("删除成功");
    }
}
