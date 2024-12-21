package com.kaige.service;

import com.kaige.entity.Moment;
import com.kaige.entity.dto.MomentView;
import org.babyfish.jimmer.Page;

public interface MomentService {
    Page<MomentView> getMomentList(Integer pageNum, boolean adminIdentity);

    Integer addLikeByMomentId(Long id);
}
