package com.kaige.repository;

import com.kaige.entity.*;
import com.kaige.entity.dto.CommentView;
import com.kaige.entity.dto.MomentView;
import org.babyfish.jimmer.Page;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.babyfish.jimmer.sql.ast.Predicate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MomentRepository extends JRepository<Moment,Long>, Tables {

    MomentTable momentTable = MomentTable.$;
    default Page<MomentView> getMomentList(Integer pageNum,Integer pageSize){
        return sql().createQuery(momentTable)
                .orderBy(Predicate.sql("%v", it -> it.value("create_time desc")))
                .select(momentTable.fetch(
                        MomentView.class
                ))
                .fetchPage(pageNum - 1, pageSize);
    }
}
