package com.snsapplication.project.sns.service;

import com.snsapplication.project.sns.exception.SnsApplicationException;
import com.snsapplication.project.sns.fixture.UserEntityFixture;
import com.snsapplication.project.sns.model.entity.UserEntity;
import com.snsapplication.project.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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

    @Test
    void 회원가입이_정상적으로_가능한_경우() {
        String userName = "username";
        String password = "password";

        //회원가입시 가입된 정보가 없으므로
        when(userEntityRepository.findAllByUsername(userName)).thenReturn(Optional.empty());
        //fixture 반환
        when(userEntityRepository.save(any())).thenReturn(Optional.of(UserEntityFixture.get(userName,password)));

        Assertions.assertDoesNotThrow(()-> userService.join(userName, password));
    }
    @Test
    void 회원가입시_중복된_아이디로_가입한_경우() {
        String userName = "username";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(userName, password);

        //fixture 반환
        when(userEntityRepository.findAllByUsername(userName)).thenReturn(Optional.of(fixture));
        //로직은 save 그치만 중복된 아이디가 있으므로 save까지는 가지 않을거임
        when(userEntityRepository.save(any())).thenReturn(Optional.of(fixture));

        Assertions.assertThrows(SnsApplicationException.class, ()-> userService.join(userName, password));
    }

    @Test
    void 로그인이_정상적으로_가능한_경우() {
        String userName = "username";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(userName, password);

        when(userEntityRepository.findAllByUsername(userName)).thenReturn(Optional.of(fixture));
        Assertions.assertDoesNotThrow(()-> userService.login(userName, password));
    }
    @Test
    void 로그인시_회원가입이_안되어있는_경우() {
        String userName = "username";
        String password = "password";

        when(userEntityRepository.findAllByUsername(userName)).thenReturn(Optional.empty());


        Assertions.assertThrows(SnsApplicationException.class, ()-> userService.login(userName, password));
    }
    @Test
    void 로그인시_비밀번호가_틀린_경우() {
        String userName = "username";
        String password = "password";
        String wrongPassword = "wrong";
        UserEntity fixture = UserEntityFixture.get(userName, password);

        when(userEntityRepository.findAllByUsername(userName)).thenReturn(Optional.of(fixture));


        Assertions.assertThrows(SnsApplicationException.class, ()-> userService.login(userName, wrongPassword));
    }
}

