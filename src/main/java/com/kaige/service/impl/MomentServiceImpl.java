package com.kaige.service.impl;

import com.kaige.entity.Immutables;
import com.kaige.entity.Moment;
import com.kaige.entity.MomentTable;
import com.kaige.entity.dto.MomentView;
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

    private static final Integer totalPages = 5;
    @Override
    public Page<MomentView> getMomentList(Integer pageNum, boolean adminIdentity) {

        Page<MomentView> momentViewPage = momentRepository.getMomentList(pageNum,totalPages);
        List<MomentView> momentList = momentViewPage.getRows();

        for (MomentView moment : momentList) {
            if(adminIdentity || moment.isPublished()){
                moment.setContent(MarkdownUtils.markdownToHtmlExtensions(moment.getContent()));
            }else {
                moment.setContent(PRIVATE_MOMENT_CONTENT);
            }
        }
        return momentViewPage;
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
