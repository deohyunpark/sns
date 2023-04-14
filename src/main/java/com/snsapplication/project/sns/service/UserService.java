package com.snsapplication.project.sns.service;

import com.snsapplication.project.sns.exception.SnsApplicationException;
import com.snsapplication.project.sns.model.User;
import com.snsapplication.project.sns.model.entity.UserEntity;
import com.snsapplication.project.sns.repository.UserEntityRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;
    public User join(String userName, String password) {
        //username으로 가입하려는 User가 있는지 찾기
        Optional<UserEntity> userEntity = userEntityRepository.findAllByUsername(userName);
        //있으면 회원가입 진행
        userEntityRepository.save(new UserEntity());

        return new User();
    }

    public String login(String userName, String password) {
        //회원가입 여부 체크
        UserEntity userEntity = userEntityRepository.findAllByUsername(userName).orElseThrow(()-> new SnsApplicationException());

        //비밀번호 체크
        if (!userEntity.getPassword().equals(password)) {
            throw new SnsApplicationException();
        }
        //token 생성

        return "";
    }

}
