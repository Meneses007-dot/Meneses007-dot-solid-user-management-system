package com.unicauca.usersystem.domain.service;

import com.unicauca.usersystem.domain.Role;
import com.unicauca.usersystem.domain.User;
import com.unicauca.usersystem.domain.UserStatus;
import com.unicauca.usersystem.domain.access.PasswordHasher;
import com.unicauca.usersystem.domain.access.UserRepository;

import java.util.Optional;
import java.util.regex.Pattern;

public class UserService {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public UserService(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public void registerUser(String login, String fullName, String plainPassword, Role role) {
        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("El login es obligatorio.");
        }
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("El nombre completo es obligatorio.");
        }
        if (role == null) {
            throw new IllegalArgumentException("Debe seleccionar un rol.");
        }
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }
        if (!isValidPassword(plainPassword)) {
            throw new IllegalArgumentException("La contraseña debe tener mín 6 caracteres, un dígito, una mayúscula y un carácter especial.");
        }
        if (userRepository.findByLogin(login).isPresent()) {
            throw new IllegalArgumentException("El usuario ya existe.");
        }

        String hashedPassword = passwordHasher.hash(plainPassword);
        User newUser = new User(login, fullName, hashedPassword, role, UserStatus.ACTIVO);
        userRepository.save(newUser);
    }

    public User authenticate(String login, String plainPassword) {
        Optional<User> optUser = userRepository.findByLogin(login);
        if (optUser.isEmpty()) throw new SecurityException("Credenciales inválidas.");
        
        User user = optUser.get();
        if (user.getStatus() == UserStatus.INACTIVO) throw new SecurityException("Usuario inactivo.");
        
        if (!passwordHasher.verify(plainPassword, user.getPassword())) {
            throw new SecurityException("Credenciales inválidas.");
        }
        return user;
    }

    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{6,}$";
        return Pattern.compile(regex).matcher(password).matches();
    }
}
