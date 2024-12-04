package com.kaige.service.impl;

import com.kaige.entity.Immutables;
import com.kaige.entity.Moment;
import com.kaige.entity.MomentTable;
import com.kaige.repository.MomentRepository;
import com.kaige.service.MomentService;
import com.kaige.utils.markdown.MarkdownUtils;
import org.babyfish.jimmer.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class MomentServiceImpl implements MomentService {

    @Autowired
    private MomentRepository momentRepository;

    MomentTable momentTable = MomentTable.$;

    private static final String PRIVATE_MOMENT_CONTENT = "<p>此条为私密动态，仅发布者可见！</p>";
    @Override
    public Page<Moment> getMomentList(Integer pageNum, boolean adminIdentity) {
        List<Moment> momentList = momentRepository.getMomentList(pageNum);
        int pageSize = 5;
        int totalItems = momentList.size(); // 总数据条数
        int totalPages = totalItems / pageSize; // 初步计算总页数

// 如果有剩余的数据（总数据不能被 pageSize 整除），则加一页
        if (totalItems % pageSize != 0) {
            totalPages ++;
        }
// 这样 totalPages 就是总页数
        for (Moment moment : momentList) {
            if(adminIdentity || moment.Published()){
                Moment moment1 = Immutables.createMoment(moment,it -> {
                    it.setContent(MarkdownUtils.markdownToHtmlExtensions(moment.content()));
                });
                momentList.set(momentList.indexOf(moment),moment1);
            }else {
                Moment moment2 = Immutables.createMoment(moment,it -> {
                    it.setContent(PRIVATE_MOMENT_CONTENT);
                });
                momentList.set(momentList.indexOf(moment),moment2);
            }
        }
        Page<Moment> momentPage = new Page<>(momentList,momentList.size(),totalPages);
        return momentPage;
    }

    @Override
    public Integer addLikeByMomentId(Long id) {
//        对应id 的likes +1
        return momentRepository.sql().createUpdate(momentTable)
                .where(momentTable.id().eq(BigInteger.valueOf(id)))
                .set(momentTable.likes(),momentTable.likes().plus(1))
                .execute();

    }
}
