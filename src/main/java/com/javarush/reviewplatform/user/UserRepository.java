package com.javarush.reviewplatform.user;

import com.javarush.reviewplatform.common.BaseRepository;

public interface UserRepository extends BaseRepository<User> {

    User findByEmail(String email);
}
