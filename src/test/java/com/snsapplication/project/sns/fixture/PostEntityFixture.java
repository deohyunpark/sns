package com.snsapplication.project.sns.fixture;

import com.snsapplication.project.sns.model.entity.PostEntity;
import com.snsapplication.project.sns.model.entity.UserEntity;

public class PostEntityFixture {
    //테스트용 UserEntity
    public static PostEntity get(String userName, Integer postId,Integer userId) {
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setUserName(userName);

        PostEntity result = new PostEntity();
        result.setUser(user);
        result.setId(postId);
        return result;
    }
}
