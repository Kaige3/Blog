package com.kaige.service;

import com.kaige.entity.Moment;
import com.kaige.entity.dto.MomentInput;
import com.kaige.entity.dto.MomentView;
import org.babyfish.jimmer.Page;

import java.math.BigInteger;

public interface MomentService {
    Page<MomentView> getMomentList(Integer pageNum, boolean adminIdentity);

    Integer addLikeByMomentId(Long id);

    org.springframework.data.domain.Page<MomentView> getMomentListOfPage(Integer pageNum, Integer pageSize);

    void updatePublished(Long id, Boolean published);

    Moment getMomentById(Long id);

    void deleteMomentById(BigInteger id);

    void saveMoment(MomentInput momentInput);

    void updateById(MomentInput momentInput);
}
