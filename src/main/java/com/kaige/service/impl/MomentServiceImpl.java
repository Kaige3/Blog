package com.kaige.service.impl;

import com.kaige.entity.Immutables;
import com.kaige.entity.Moment;
import com.kaige.entity.MomentFetcher;
import com.kaige.entity.MomentTable;
import com.kaige.entity.dto.MomentInput;
import com.kaige.entity.dto.MomentView;
import com.kaige.handler.exception.NotFoundException;
import com.kaige.handler.exception.PersistenceException;
import com.kaige.repository.MomentRepository;
import com.kaige.service.MomentService;
import com.kaige.utils.markdown.MarkdownUtils;
import org.babyfish.jimmer.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
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

    @Override
    public org.springframework.data.domain.Page<MomentView> getMomentListOfPage(Integer pageNum, Integer pageSize) {
        return momentRepository.getMomentPage(pageNum,pageSize);
    }

    @Override
    public void updatePublished(Long id, Boolean published) {
        try {
            momentRepository.sql().createUpdate(momentTable)
                    .where(momentTable.id().eq(BigInteger.valueOf(id)))
                    .set(momentTable.Published(),published)
                    .execute();
        } catch (Exception e) {
            throw new RuntimeException("更新失败");
        }
    }

    @Override
    public Moment getMomentById(Long id) {
        Moment moment = momentRepository.sql().createQuery(momentTable)
                .where(momentTable.id().eq(BigInteger.valueOf(id)))
                .select(momentTable.fetch(
                        MomentFetcher.$
                                .content()
                                .createTime()
                                .likes()
                                .Published()
                ))
                .fetchOneOrNull();
        if(moment == null){
            throw new NotFoundException("动态不存在");
        }
        return moment;
    }

    @Override
    public void deleteMomentById(BigInteger id) {
        try {
            momentRepository.sql().deleteById(momentTable.getClass(),id);
        } catch (Exception e) {
            throw new PersistenceException("删除失败");
        }
    }

    @Override
    public void saveMoment(MomentInput momentInput) {
        momentInput.setCreateTime(LocalDateTime.now());
        momentInput.setLikes(0);
        try {
            momentRepository.save(momentInput);
        } catch (Exception e) {
            throw new PersistenceException("发布失败");
        }
    }

    @Override
    public void updateById(MomentInput momentInput) {
        momentRepository.update(momentInput);
    }
}
