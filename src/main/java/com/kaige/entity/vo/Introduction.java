package com.kaige.entity.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Introduction {
    private String avatar;
    private String name;
    private String github;
    private String qq;
    private String telegram;
    private String bilibili;
    private String netease;
    private String email;

    private List<String> rollText = new ArrayList<>();
    private List<Favorite> favorites = new ArrayList<>();
}
