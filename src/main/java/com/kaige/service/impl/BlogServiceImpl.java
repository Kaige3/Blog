package com.kaige.service.impl;

import com.kaige.entity.Blog;
import com.kaige.entity.BlogDraft;
import com.kaige.entity.BlogFetcher;
import com.kaige.entity.BlogTable;
import com.kaige.service.BlogService;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.Predicate;
import org.babyfish.jimmer.sql.ast.query.MutableRootQuery;
import org.babyfish.jimmer.sql.ast.tuple.Tuple6;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.lang.String.format;

@Service
public class BlogServiceImpl implements BlogService {

    private JSqlClient jSqlClient;
    public BlogServiceImpl(JSqlClient jSqlClient) {
        this.jSqlClient = jSqlClient;
    }
    @Override
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
//            for (Blog b : execute1){
//                if(!"".equals(b.password())){
////                    BlogDraft blogDraft= BlogDraft.
////                    blogDraft.setPassword("");
////                    blogDraft.setPublished(false);
////                    b=blogDraft;
//                    b.createTime().format(DateTimeFormatter.ofPattern("dd日"));
//                    Blog newBlog = BlogDraft.$.produce(b, draft -> {
//                        draft.setPassword("");
//                        draft.setPublished(false);
//                        draft.setCreateTime(b.createTime().format(DateTimeFormatter.ofPattern("dd日")));
//                    });
//                    BeanUtils.copyProperties(b,newBlog);
//                }else {
//                    b.createTime().format(outputFormatter);
//                }
//            }
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
