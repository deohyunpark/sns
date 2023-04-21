package com.snsapplication.project.sns.controller.response;

import com.snsapplication.project.sns.model.User;
import com.snsapplication.project.sns.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserJoinRespone {
    private Integer id;
    private String userName;
    private UserRole role;

    public static UserJoinRespone fromUser(User user) {
        return new UserJoinRespone(
                user.getId(),
                user.getUsername(),
                user.getUserRole()
        );
    }

}
