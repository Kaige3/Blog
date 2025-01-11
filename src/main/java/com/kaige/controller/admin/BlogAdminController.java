package com.kaige.controller.admin;

import cn.hutool.core.date.DateTime;
import com.kaige.entity.*;
import com.kaige.entity.dto.*;
import com.kaige.service.BlogService;
import com.kaige.service.CategoryService;
import com.kaige.service.CommentService;
import com.kaige.service.TagService;
import com.kaige.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static cn.hutool.core.util.RuntimeUtil.getResult;

@RestController
@RequestMapping("/admin")
public class BlogAdminController {

    @Autowired
    BlogService blogService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    CommentService commentService;
    @Autowired
    TagService tagService;

    /**
     * 根据文章标题和分类模糊查询文章列表
     */
    @GetMapping("/blogs")
    public Result getBlogList(@RequestParam(defaultValue ="") String title,
                              @RequestParam(defaultValue = "",required = false) Integer categoryId,
                              @RequestParam(defaultValue = "0") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize){
        Page<BlogDetailView> blogs = blogService.getBlogList(title,categoryId,pageNum,pageSize);
        List<CategoryView> categories = categoryService.getCategoryList();
        HashMap<String, Object> map = new HashMap<>(4);
        map.put("blogs",blogs);
        map.put("categories",categories);
        return Result.ok("获取成功",map);
    }

    /**
     * 删除文章，同时删除文章对应的评论和标签
     */
    @DeleteMapping("/blog")
    public Result deleteBlog(@RequestParam BigInteger id){
        // 查询文章下所属的tagId
       List<Blog> tags = blogService.getTagIdsByBlogId(id);
       // 拿到文章所属的tagId列表,因为jimmer伪外键不能自动删除中间表，
        // 以下代码完全是因为写了伪外键而导致的
        ArrayList<BigInteger> TagIdList = new ArrayList<>();
        for (Blog tagList : tags) {
            for (Tag tagId : tagList.tags()) {
                TagIdList.add(tagId.id());
            }
        }
//         删除文章所属的标签
         blogService.deleteBlogByBlogId(id,TagIdList);
//        删除文章
        blogService.deleteBlog(id);
//        删除文章对应的评论
        commentService.deleteCommentByBlogId(id);
        return Result.ok("删除成功" ,TagIdList);
    }

    //TODO 可能有一个BUG，需要tagID
    /**
     * 获取分类和标签列表
     */
    @GetMapping("categoryAndTag")
    public Result categoryAndTag(){
        List<CategoryView> categories = categoryService.getCategoryList();
        List<TagView> tagList = tagService.getTagList();
        HashMap<String, Object> map = new HashMap<>(4);
        map.put("categories",categories);
        map.put("tags",tagList);
        return Result.ok("获取成功",map);
    }

    /**
     * 更新博客置顶状态
     */
    @PutMapping("/blog/top")
    public Result update(@RequestParam BigInteger id , @RequestParam Boolean Top){
        blogService.updateTop(id,Top);
        return Result.ok("更新成功");
    }

    /**
     * 更新博客推荐状态
     */
    @PutMapping("/blog/recommend")
    public Result updateRecommend(@RequestParam BigInteger id, @RequestParam Boolean Recommend){
        blogService.updateRecommend(id,Recommend);
        return Result.ok("更新成功");
    }

    /**
     * 更新博客可见性
     */
    @PutMapping("/blog/published")
    public Result updatePublished(@RequestParam BigInteger id, @RequestParam Boolean Published){
        blogService.updatePublished(id,Published);
        return Result.ok("更新成功");
    }

    /**
     * 按照id获取博客详情
     */
    @GetMapping("/blog")
    public Result getBlogById(@RequestParam BigInteger id){
        BlogDetailView blogById = blogService.getBlogById(id);
        return Result.ok("获取成功",blogById);
    }

    /**
     * 发布博客
     */
    @PostMapping("/blog")
    public Result saveBlog(@RequestBody BlogInput blogDetailView){
        return getResult(blogDetailView,"save");
    }

    /**
     * 更新博客
     */
    @PutMapping("/blog")
    public Result updateBlog(@RequestBody BlogInput blogDetailView){
        return getResult(blogDetailView,"update");
    }


    /**
     * 根据type，执行添加或者更新博客，
     * 1.检验参数合法
     * 2.添加分类，标签
     * 3.维护博客标签表
     * @param blog
     * @param type
     * @return
     */
    private Result getResult(BlogInput blog, String type) {
        // 检验关键字段
        if (StringUtils.isEmpty(blog.getTitle(),blog.getTitle(),blog.getContent(),blog.getDescription()) ||
        blog.getWords() == null || blog.getWords() < 0){
            return Result.error("参数有误");
        }
        // 处理分类
        Object cate = blog.getCate();
         if (cate == null){
             return Result.error("分类不能为空");
         }
         if (cate instanceof BigInteger){
             Category categoryById = categoryService.getCategoryById((BigInteger) cate);
             blog.setCategory((BlogInput.TargetOf_category) categoryById);
         }else if(cate instanceof String){ // 添加新的分类
             Category c = categoryService.getCategoryByName((String) cate);
             if (c != null){
                 return Result.error("不可添加已存在的分类");
             }
             Category produce = CategoryDraft.$.produce(draft -> draft.setCategoryName((String) cate));
             categoryService.saveCategory(produce);
             blog.setCategory((BlogInput.TargetOf_category) produce);
         }else {
             return Result.error("添加分类出错了");
         }

         // 处理标签
        List<BlogInput.TargetOf_tags> tags = blog.getTags();
        ArrayList<Tag> list = new ArrayList<>();
        for (Object t:tags) {
            if (t instanceof BigInteger){
                Tag tag = tagService.getTagById((BigInteger) t);
                list.add(tag);
            }else if(t instanceof String){
                Tag tag = tagService.getTagByName((String) t);
                Tag produce = TagDraft.$.produce(draft -> draft.setTagName((String) t));
                tagService.saveTag(produce);
                tags.add((BlogInput.TargetOf_tags) produce);
            }else {
                return Result.error("标签有问题");
            }
        }
        DateTime dateTime = new DateTime();
        if (blog.getReadTime()<0){
            // 粗略计算阅读时长
            blog.setReadTime(Math.round(blog.getWords()/200.0f));
        }
        if (blog.getViews() <0){
            blog.setViews(0);
        }
        if ("save".equals(type)){
            blog.setCreateTime(dateTime.toLocalDateTime());
            blog.setUpdateTime(dateTime.toLocalDateTime());
            blog.setUserId(BigInteger.valueOf(1));
            blogService.saveBlog(blog);
            // 维护博客标签关联表
            for(Tag t:list){
                blogService.saveBlogTag(blog.getId(),t.id());
            }
            return Result.ok("添加成功");
        }else {
            blog.setUpdateTime(dateTime.toLocalDateTime());
            blogService.updateBlog(blog);
            // 维护博客标签关联表
            List<Blog> tagList2 = blogService.getTagIdsByBlogId(blog.getId());
            // 拿到文章所属的tagId列表,因为jimmer伪外键不能自动删除中间表，
            // 以下代码完全是因为写了伪外键而导致的
            ArrayList<BigInteger> TagIdList = new ArrayList<>();
            for (Blog tagList : tagList2) {
                for (Tag tagId : tagList.tags()) {
                    TagIdList.add(tagId.id());
                }
            }
            blogService.deleteBlogByBlogId(blog.getId(),TagIdList);
            for(Tag t:list){
                blogService.saveBlogTag(blog.getId(),t.id());
            }
            return Result.ok("更新成功");
        }

    }
}
