package com.javarush.reviewplatform.user;

import com.javarush.reviewplatform.common.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User> {

    User findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);
}
