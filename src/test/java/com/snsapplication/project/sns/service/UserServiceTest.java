package com.snsapplication.project.sns.service;

import com.snsapplication.project.sns.exception.ErrorCode;
import com.snsapplication.project.sns.exception.SnsApplicationException;
import com.snsapplication.project.sns.fixture.UserEntityFixture;
import com.snsapplication.project.sns.model.entity.UserEntity;
import com.snsapplication.project.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @MockBean
    private UserEntityRepository userEntityRepository;

    @MockBean
    private BCryptPasswordEncoder encoder;

    @Test
    void 회원가입이_정상적으로_가능한_경우() {
        String userName = "username";
        String password = "password";

        //회원가입시 가입된 정보가 없으므로
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(encoder.encode(password)).thenReturn("encrypt_password");
        //fixture 반환
        when(userEntityRepository.save(any())).thenReturn(UserEntityFixture.get(userName,password,1));

        Assertions.assertDoesNotThrow(()-> userService.join(userName, password));
    }
    @Test
    void 회원가입시_중복된_아이디로_가입한_경우() {
        String userName = "username";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(userName, password,1);

        //fixture 반환
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));
        when(encoder.encode(password)).thenReturn("encrypt_password");

        //로직은 save 그치만 중복된 아이디가 있으므로 save까지는 가지 않을거임
        when(userEntityRepository.save(any())).thenReturn(Optional.of(fixture));

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, ()-> userService.join(userName, password));
        Assertions.assertEquals(ErrorCode.DUPLICATED_USER_NAME, e.getErrorCode());
    }

    @Test
    void 로그인이_정상적으로_가능한_경우() {
        String userName = "username";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(userName, password,1);

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));
        when(encoder.matches(password, fixture.getPassword())).thenReturn(true);
        Assertions.assertDoesNotThrow(()-> userService.login(userName, password));
    }
    @Test
    void 로그인시_회원가입이_안되어있는_경우() {
        String userName = "username";
        String password = "password";

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());


        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, ()-> userService.login(userName, password));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUNDED, e.getErrorCode());
    }
    @Test
    void 로그인시_비밀번호가_틀린_경우() {
        String userName = "username";
        String password = "password";
        String wrongPassword = "wrong";
        UserEntity fixture = UserEntityFixture.get(userName, password,1);

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));


        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, ()-> userService.login(userName, password));
        Assertions.assertEquals(ErrorCode.INVALID_PASSWORD, e.getErrorCode());
    }
}

