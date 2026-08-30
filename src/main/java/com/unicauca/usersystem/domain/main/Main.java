package com.unicauca.usersystem.domain.main;

import com.unicauca.usersystem.domain.access.Argon2Hasher;
import com.unicauca.usersystem.domain.access.PasswordHasher;
import com.unicauca.usersystem.domain.access.SQLiteUserRepository;
import com.unicauca.usersystem.domain.access.UserRepository;
import com.unicauca.usersystem.domain.service.UserService;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        UserRepository repo = new SQLiteUserRepository("jdbc:sqlite:users.db");
        PasswordHasher hasher = new Argon2Hasher();
        UserService service = new UserService(repo, hasher);

        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView(service);
            loginView.setVisible(true);
        });
    }
}
