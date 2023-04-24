package com.snsapplication.project.sns.controller;

import com.snsapplication.project.sns.controller.request.UserJoinRequest;
import com.snsapplication.project.sns.controller.request.UserLoginRequest;
import com.snsapplication.project.sns.controller.response.Response;
import com.snsapplication.project.sns.controller.response.UserJoinRespone;
import com.snsapplication.project.sns.model.User;
import com.snsapplication.project.sns.controller.response.UserLoginResponse;
import com.snsapplication.project.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinRespone> join(@RequestBody UserJoinRequest request) {
        User user = userService.join(request.getUserName(), request.getPassword());
        return Response.success(UserJoinRespone.fromUser(user));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request.getUserName(),request.getPassword());
        return Response.success(new UserLoginResponse(token));
    }
}
