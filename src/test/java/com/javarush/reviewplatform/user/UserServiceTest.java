package com.javarush.reviewplatform.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
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