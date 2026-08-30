package com.unicauca.usersystem.domain.access;

import com.unicauca.usersystem.domain.User;
import java.util.Optional;

public interface IUserRepository {
    void save(User user);
    Optional<User> findByLogin(String login);
}
