package com.kaige.controller.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaige.controller.web.CommentController;
import com.kaige.entity.dto.CommentInput;
import com.kaige.service.CommentService;
import com.kaige.service.UserService;
import com.kaige.service.impl.UserServiceImpl;
import com.kaige.utils.JwtUtils;
import com.kaige.controller.web.CommentController;
import com.kaige.utils.comment.CommentUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private JwtUtils jwtUtils;
    @MockBean
    private CommentUtils commentUtils;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testPostComment_Success() throws Exception {
        // Create a test comment
        CommentInput commentInput = new CommentInput();
        commentInput.setNickname("kaige");
        commentInput.setAdminComment(false);
        commentInput.setContent("这是一个评论");
        commentInput.setEmail("EMAIL");
        commentInput.setAvatar("/");
        commentInput.setPublished(true);
        commentInput.setPage(1);
        commentInput.setNotice(false);

        // Mock services as needed
//        when(commentService.saveComment(any(CommentInput.class))).thenReturn(true);

        // Perform the request
        mockMvc.perform(post("/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentInput)))
                .andExpect(status().isOk())  // 期待状态是 200 OK
                .andExpect(jsonPath("$.code").value("200")) // 期待返回的 JSON 包含 code=200
                .andExpect(jsonPath("$.message").value("评论成功")); // 期待返回的消息
    }

    @Test
    void testPostComment_InvalidContent() throws Exception {
        // Create an invalid comment (e.g. empty content)
        CommentInput commentInput = new CommentInput();
        commentInput.setNickname("kaige");
        commentInput.setAdminComment(false);
        commentInput.setContent("这是一个评论");
        commentInput.setEmail("EMAIL");
        commentInput.setAvatar("/");
        commentInput.setPublished(true);
        commentInput.setPage(1);
        commentInput.setNotice(false);

        // Perform the request
        mockMvc.perform(post("/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentInput)))
                .andExpect(status().isBadRequest())  // Expect 400 Bad Request
                .andExpect(jsonPath("$.message").value("参数有误")); // Expect error message "参数有误"
    }

    @Test
    void testPostComment_TokenRequired() throws Exception {
        // Create a valid comment without a token
        CommentInput commentInput = new CommentInput();
        commentInput.setNickname("kaige");
        commentInput.setAdminComment(false);
        commentInput.setContent("这是一个评论");
        commentInput.setEmail("EMAIL");
        commentInput.setAvatar("/");
        commentInput.setPublished(true);
        commentInput.setPage(1);
        commentInput.setNotice(false);

        // Mock that no token is found
        when(jwtUtils.judgeTokenIsExist(anyString())).thenReturn(false);

        // Perform the request
        mockMvc.perform(post("/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentInput)))
                .andExpect(status().isForbidden())  // Expect 403 Forbidden
                .andExpect(jsonPath("$.message").value("此文章受密码保护,请验证密码!")); // Expect error message for missing token
    }

    // More test cases can be added for other scenarios like token expiration, invalid token, etc.
}
