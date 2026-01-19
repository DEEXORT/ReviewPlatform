package com.javarush.reviewplatform.user;

import com.javarush.reviewplatform.common.AbstractBaseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService extends AbstractBaseService<User, UserTo, UserRepository, UserMapper> {

    public UserService(UserRepository repository, UserMapper mapper) {
        super(repository, mapper);
    }

    public UserTo findByEmail(UserTo userTo) {
        User user = repository.findByEmail(userTo.email);
        if (user == null) throw new EntityNotFoundException("User not found");
        return mapper.mapToDto(user);
    }
}
