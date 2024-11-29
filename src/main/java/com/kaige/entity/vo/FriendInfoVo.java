package com.kaige.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Data
//@AllArgsConstructor
//@NoArgsConstructor
public class FriendInfoVo {
    private String content;
    private Boolean commentEnabled;

    public FriendInfoVo() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getCommentEnabled() {
        return commentEnabled;
    }

    public void setCommentEnabled(Boolean commentEnabled) {
        this.commentEnabled = commentEnabled;
    }

    public FriendInfoVo(String content, Boolean commentEnabled) {
        this.content = content;
        this.commentEnabled = commentEnabled;
    }
}
