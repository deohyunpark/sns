package com.snsapplication.project.sns.service;

import com.snsapplication.project.sns.exception.ErrorCode;
import com.snsapplication.project.sns.exception.SnsApplicationException;
import com.snsapplication.project.sns.model.entity.PostEntity;
import com.snsapplication.project.sns.model.entity.UserEntity;
import com.snsapplication.project.sns.repository.PostEntityRepository;
import com.snsapplication.project.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;


    @Transactional
    public void create(String title, String body, String userName) {
        //find user
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(
                () -> new SnsApplicationException(ErrorCode.USER_NOT_FOUNDED, String.format("%s 이라는 유저를 찾을 수 없습니다", userName)));
        //post save
        postEntityRepository.save(PostEntity.of(title, body, userEntity));
    }

    @Transactional
    public void modify(String title, String body, String userName, Integer postId) {
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(
                () -> new SnsApplicationException(ErrorCode.USER_NOT_FOUNDED, String.format("%s 이라는 유저를 찾을 수 없습니다", userName)));
        //post exist

        //post permission
    }
}
