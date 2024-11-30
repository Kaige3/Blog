package com.kaige.service.impl;

import com.kaige.constant.RedisKeyConstants;
import com.kaige.entity.*;
import com.kaige.service.BlogService;
import com.kaige.service.RedisService;
import org.babyfish.jimmer.Page;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.lang.String.format;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private RedisService redisService;


    private JSqlClient jSqlClient;
    public BlogServiceImpl(JSqlClient jSqlClient) {
        this.jSqlClient = jSqlClient;
    }

    //博客简介列表排序方式
    private static final String orderBy = "is_top desc, create_time desc";

    @Override
    /**
     * 根据分类名称获取 公开 文章列表
     */
    public Page<Blog> getBlogListByCategoryName(String categoryName, Integer pageNum) {
        BlogTable blog = BlogTable.$;
        return jSqlClient.createQuery(blog)
                .where(blog.category().categoryName().eq(categoryName))
                .orderBy(Predicate.sql("%v",it->it.value(orderBy)))
                .select(blog.fetch(
                        BlogFetcher.$
                              .title()
                                .description()
                                .createTime()
                                .views()
                                .words()
                                .readTime()
                                .Top()
                                .password()
                                .Published()
                                .category(CategoryFetcher.$
                                       .categoryName())
                                .tags(TagFetcher.$
                                       .tagName()
                                        .color())
                ))
                .fetchPage(pageNum-1,10);
    }

    @Override
    public Page<Blog> getBlogListByIsPublished(Integer pageNum) {
        String redisKey = RedisKeyConstants.HOME_BLOG_INFO_LIST;
        Page<Blog> pageResultFromRedis = redisService.getBlogInfoPageResultByPublish(redisKey,pageNum);
        if(pageResultFromRedis!=null){
            return pageResultFromRedis;
        }
        BlogTable blog = BlogTable.$;
        Page<Blog> blogPage = jSqlClient.createQuery(blog)
                .where(blog.Published().eq(true))
                .orderBy(Predicate.sql("%v", it -> it.value(orderBy)))
                .select(blog.fetch(
                        BlogFetcher.$
                                .allTableFields()
                                .category(
                                        CategoryFetcher.$
                                                .categoryName()
                                )
                                .tags(TagFetcher.$
                                        .allTableFields()
                                )))
                .fetchPage(pageNum - 1, 10);
        redisService.saveKVToHash(redisKey,pageNum,blogPage);
        return blogPage;
    }

    /**
     文章归档  按照年月  统计文章数量
      */
    @Override
    //TODO 完善字段显示，createTIme格式化
    //TODO 从缓存查询未实现
    public Map<String, Object> getArchiveBlogAndCountByIsPublished() {
//        查缓存
//        按照文章是否公布，对年和月进行统计
        BlogTable blog = BlogTable.$;
        List<LocalDateTime> execute = jSqlClient.createQuery(blog)
                .where(blog.Published().eq(true))
                .select(blog.createTime())
                .execute();

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy年MM月");
        // 存储格式化后的结果
        List<String> formattedDates = new ArrayList<>();
        // 对每个日期进行格式化
        for (LocalDateTime date : execute) {
            // 格式化日期为 "yyyy年MM月"
            String formattedDate = date.format(outputFormatter);
            formattedDates.add(formattedDate);
        }
        Map<String, List<Blog>> archiveMap = new LinkedHashMap<>();
        List<Blog> list = new ArrayList<>();
        // 遍历格式化后的日期列表
        for (String s : formattedDates) {
            List<Blog> execute1 = jSqlClient.createQuery(blog)
                    .where(Predicate.sql("DATE_FORMAT(create_time, '%Y年%m月') = %v", it->it.value(s)))
                    .select(blog.fetch(
                            BlogFetcher.$
                                    .title()
                                    .password()
                                    .createTime()
                                    .password()
                                    .Published()
                    ))
                    .execute();

//            for (Blog b : execute1) {
//                // 判断密码是否为空
//                if (b.password() != null && !"".equals(b.password())) {
//                    // 使用 BlogDraft 的功能，去掉密码，设置未发布
//                    Blog newBlog = BlogDraft.$.produce(b, draft -> {
//                        draft.setPassword("");  // 清空密码
//                        draft.setPublished(false);  // 设置为未发布
//                    });
//                    // 将创建时间格式化
//                    String formattedCreateTime = newBlog.createTime().format(DateTimeFormatter.ofPattern("dd日"));
//                    // 这里假设你希望以 `formattedCreateTime` 更新 newBlog 的属性，如果需要的话
//                    System.out.println("Formatted create time: " + formattedCreateTime);
//
//                    // 添加修改后的 Blog 到 list
//                    blogList.add(newBlog);
//                } else {
//                    // 如果没有密码，则直接使用原 Blog
//                    String formattedCreateTime = b.createTime().format(DateTimeFormatter.ofPattern("dd日"));
//                    System.out.println("Formatted create time: " + formattedCreateTime);
//                    blogList.add(b);
//                }
//            }
            archiveMap.put(s,execute1);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("bolgMap",archiveMap);
        return map;
    }
}
