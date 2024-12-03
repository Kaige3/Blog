package com.kaige.repository;

import com.kaige.entity.*;
import org.babyfish.jimmer.Page;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.babyfish.jimmer.sql.ast.Predicate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MomentRepository extends JRepository<Moment,Long>, Tables {

    MomentTable momentTable = MomentTable.$;
    default List<Moment> getMomentList(Integer pageNum){
        return sql().createQuery(momentTable)
                .orderBy(Predicate.sql("%v", it -> it.value("create_time desc")))
//                .where(momentTable.Published().eq(true))
                .select(momentTable.fetch(
                        MomentFetcher.$
                                .content()
                                .createTime()
                                .likes()
                                .Published()
                ))
                .execute();

    }
}
