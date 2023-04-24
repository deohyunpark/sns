package com.snsapplication.project.sns.controller.response;

import com.snsapplication.project.sns.model.User;
import com.snsapplication.project.sns.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public
class UserResponse {
    private Integer id;
    private UserRole role;
    private String userName;

    public static UserResponse fromUser(User user) {
        return new UserResponse(
                user.getId(),
                user.getUserRole(),
                user.getUsername()
        );
    }

}