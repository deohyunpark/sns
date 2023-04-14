package com.snsapplication.project.sns.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snsapplication.project.sns.controller.request.UserJoinRequest;
import com.snsapplication.project.sns.exception.SnsApplicationException;
import com.snsapplication.project.sns.model.User;
import com.snsapplication.project.sns.controller.request.UserLoginRequest;
import com.snsapplication.project.sns.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    @Test
    public void 회원가입() throws Exception {
        String userName = "username";
        String password = "password";

        when(userService.join(userName,password)).thenReturn(mock(User.class));

        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName, password)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 중복된_아이디로_회원가입을_하는_경우_에러반환() throws Exception {
        String userName = "username";
        String password = "password";

        when(userService.join(userName,password)).thenThrow(new SnsApplicationException());
        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName, password)))
                ).andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void 로그인() throws Exception {
        String userName = "username";
        String password = "password";

        when(userService.login(userName,password)).thenReturn("test_token");

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 회원가입시_가입안된_아이디로_로그인시_에러반환() throws Exception {
        String userName = "username";
        String password = "password";

        when(userService.login(userName,password)).thenThrow(new SnsApplicationException());

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password)))
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void 로그인시_틀린_비밀번호로_로그인할경우_에러반환() throws Exception {
        String userName = "username";
        String password = "password";

        when(userService.login(userName,password)).thenThrow(new SnsApplicationException());

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
