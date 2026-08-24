package com.unicauca.usersystem.abstractions;

import com.unicauca.usersystem.domain.User;
import java.util.Optional;

public interface UserRepository {
    void save(User user);
    Optional<User> findByLogin(String login);
}