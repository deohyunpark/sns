package com.snsapplication.project.sns.service;

import com.snsapplication.project.sns.exception.ErrorCode;
import com.snsapplication.project.sns.exception.SnsApplicationException;
import com.snsapplication.project.sns.model.Post;
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
                () -> new SnsApplicationException(ErrorCode.USER_NOT_FOUNDED, String.format("%s not founded", userName)));
        //post save
        postEntityRepository.save(PostEntity.of(title, body, userEntity));
    }

    @Transactional
    public Post modify(String title, String body, String userName, Integer postId) {
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(
                () -> new SnsApplicationException(ErrorCode.USER_NOT_FOUNDED, String.format("%s not founded", userName)));
        //post exist
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));
        //post permission
        if (postEntity.getUser() != userEntity) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", userName, postId));
        }
        postEntity.setTitle(title);
        postEntity.setBody(body);

        return Post.fromEntity(postEntityRepository.save(postEntity));
    }
    @Transactional
    public void delete(String userName, Integer postId) {
        //작성한 유저와 글삭제를 하려는 유저가 같은 유저인지 확인하기
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(
                () -> new SnsApplicationException(ErrorCode.USER_NOT_FOUNDED, String.format("%s not founded", userName)));

        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));

        if (postEntity.getUser() != userEntity) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", userName, postId));
        }
        postEntityRepository.delete(postEntity);
    }
}
