package com.snsapplication.project.sns.service;

import com.snsapplication.project.sns.exception.ErrorCode;
import com.snsapplication.project.sns.exception.SnsApplicationException;
import com.snsapplication.project.sns.model.User;
import com.snsapplication.project.sns.model.entity.UserEntity;
import com.snsapplication.project.sns.repository.UserEntityRepository;
import com.snsapplication.project.sns.utils.JwtTokenUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    public User loadUserByUserName(String userName){
        //User타입을 반환해야하니까 map(User::fromEntity)
        return userEntityRepository.findByUserName(userName).map(User::fromEntity).orElseThrow(
                () -> new SnsApplicationException(ErrorCode.USER_NOT_FOUNDED, String.format("%s 유저를 찾을 수 없습니다", userName)));
    }
    @Transactional
    public User join(String userName, String password) {
        //username으로 가입하려는 User가 있는지 찾기
        //이미 가입되어있는 User가 있다면 예외 반환
        userEntityRepository.findByUserName(userName).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME,String.format("%s 는 이미 가입되어있는 아이디 입니다.",userName));
        });
        //있으면 회원가입 진행
        UserEntity userEntity = userEntityRepository.save(UserEntity.of(userName, encoder.encode(password)));

        return User.fromEntity(userEntity);
    }

    public String login(String userName, String password) {
        //회원가입 여부 체크
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(()-> new SnsApplicationException(ErrorCode.USER_NOT_FOUNDED,String.format("%s 인 회원을 찾을 수 없습니다.", userName)));

        //비밀번호 체크
        if (!encoder.matches(password, userEntity.getPassword())) {
            throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }
        //token 생성기
        String token = JwtTokenUtils.generateToken(userName, secretKey, expiredTimeMs);
        return token;
    }

}
