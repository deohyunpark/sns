package com.snsapplication.project.sns.fixture;

import com.snsapplication.project.sns.model.entity.UserEntity;

public class UserEntityFixture {
    //테스트용 UserEntity
    public static UserEntity get(String userName, String password, Integer postId) {
        UserEntity result = new UserEntity();
        result.setId(1);
        result.setUserName(userName);
        result.setPassword(password);

        return result;
    }
}
