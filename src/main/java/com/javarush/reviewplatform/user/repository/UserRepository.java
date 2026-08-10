package com.javarush.reviewplatform.user.repository;

import com.javarush.reviewplatform.common.BaseRepository;
import com.javarush.reviewplatform.user.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User> {

    User findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);
}
