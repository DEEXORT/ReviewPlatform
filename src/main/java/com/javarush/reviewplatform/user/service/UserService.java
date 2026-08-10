package com.javarush.reviewplatform.user.service;

import com.javarush.reviewplatform.common.AbstractBaseService;
import com.javarush.reviewplatform.user.exception.UserNotFoundException;
import com.javarush.reviewplatform.user.mapper.UserMapper;
import com.javarush.reviewplatform.user.model.User;
import com.javarush.reviewplatform.user.model.UserTo;
import com.javarush.reviewplatform.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService extends AbstractBaseService<User, UserTo, UserRepository, UserMapper> {
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, UserMapper mapper, PasswordEncoder passwordEncoder) {
        super(repository, mapper);
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserTo save(UserTo to) {
        String encode = passwordEncoder.encode(to.getPassword());
        to.setPassword(encode);
        return super.save(to);
    }

    public UserTo findByEmail(UserTo userTo) {
        User user = repository.findByEmail(userTo.getEmail());
        if (user == null) throw new EntityNotFoundException("User not found");
        return mapper.mapToDto(user);
    }

    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    public UserTo findByUsername(String username) {
        return repository
                .findByUsername(username)
                .map(mapper::mapToDto)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
    }
}
