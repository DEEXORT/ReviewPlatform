package com.javarush.reviewplatform.user;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Test
    void should_saveUser() {
        UserTo userTo = UserTo.builder()
                .username("test")
                .password("pass")
                .firstName("user")
                .lastName("user")
                .email("test@test.ru")
                .build();


    }
}