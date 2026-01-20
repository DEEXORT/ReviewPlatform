package com.javarush.reviewplatform.user;

import com.javarush.reviewplatform.common.AbstractBaseService;
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
        User user = repository.findByEmail(userTo.email);
        if (user == null) throw new EntityNotFoundException("User not found");
        return mapper.mapToDto(user);
    }

    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }
}
