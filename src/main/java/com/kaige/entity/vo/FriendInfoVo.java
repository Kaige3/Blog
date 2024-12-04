package com.kaige.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//TODO 负面教学案例，没必要这样写，直接在site里面获取就可以了
public class FriendInfoVo {
    private String content;
    private Boolean commentEnabled;

}
