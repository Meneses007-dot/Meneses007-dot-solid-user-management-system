package com.unicauca.usersystem.ui;

import com.unicauca.usersystem.abstractions.PasswordHasher;
import com.unicauca.usersystem.abstractions.UserRepository;
import com.unicauca.usersystem.infrastructure.Argon2Hasher;
import com.unicauca.usersystem.infrastructure.SQLiteUserRepository;
import com.unicauca.usersystem.service.UserService;

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