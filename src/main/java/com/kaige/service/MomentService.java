package com.kaige.service;

import com.kaige.entity.Moment;
import org.babyfish.jimmer.Page;

public interface MomentService {
    Page<Moment> getMomentList(Integer pageNum, boolean adminIdentity);
}
