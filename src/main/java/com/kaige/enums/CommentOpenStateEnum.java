package com.kaige.enums;

/**
 * 评论状态枚举
 */
public enum CommentOpenStateEnum {
    /**
     * 博客不存在 or 博客未公开
     */
    NOT_FOUND,
    /**
     * 评论开放
     */
    OPEN,
    /**
     * 评论关闭
     */
    CLOSE,
    /**
     * 评论所在的页面需要面膜
     */
    PASSWORD,
}
