package com.unicauca.usersystem.service;

import com.unicauca.usersystem.abstractions.PasswordHasher;
import com.unicauca.usersystem.abstractions.UserRepository;
import com.unicauca.usersystem.domain.Role;
import com.unicauca.usersystem.domain.User;
import com.unicauca.usersystem.domain.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    private UserRepository mockRepo;
    private PasswordHasher mockHasher;
    private UserService userService;

    @BeforeEach
    void setUp() {
        mockRepo = Mockito.mock(UserRepository.class);
        mockHasher = Mockito.mock(PasswordHasher.class);
        userService = new UserService(mockRepo, mockHasher);
    }

    @Test
    void testRegisterUser_Success() {
        when(mockRepo.findByLogin("admin1")).thenReturn(Optional.empty());
        when(mockHasher.hash("Pass123!")).thenReturn("hashed_value");

        assertDoesNotThrow(() -> userService.registerUser("admin1", "Admin User", "Pass123!", Role.ADMINISTRADOR));
        
        verify(mockRepo, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_InvalidPassword() {
        // Falta mayúscula y carácter especial
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser("user1", "User", "password123", Role.ESTUDIANTE);
        });
        assertTrue(exception.getMessage().contains("carácter especial"));
    }

    @Test
    void testRegisterUser_PasswordWithoutLowercaseIsValid() {
        when(mockRepo.findByLogin("autor1")).thenReturn(Optional.empty());
        when(mockHasher.hash("ABC123!")).thenReturn("hashed_value");

        assertDoesNotThrow(() -> userService.registerUser("autor1", "Autor", "ABC123!", Role.AUTOR));
        verify(mockRepo, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_DuplicateLogin() {
        when(mockRepo.findByLogin("admin1")).thenReturn(Optional.of(
            new User("admin1", "Admin", "hash", Role.ADMINISTRADOR, UserStatus.ACTIVO)));

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            userService.registerUser("admin1", "Admin", "Pass123!", Role.ADMINISTRADOR));
        assertTrue(exception.getMessage().contains("ya existe"));
    }

    @Test
    void testAuthenticate_InactiveUser() {
        User inactive = new User("rev1", "Revisor", "hashed_pass", Role.REVISOR, UserStatus.INACTIVO);
        when(mockRepo.findByLogin("rev1")).thenReturn(Optional.of(inactive));

        assertThrows(SecurityException.class, () -> userService.authenticate("rev1", "Clave123!"));
        verify(mockHasher, never()).verify(anyString(), anyString());
    }

    @Test
    void testRegisterUser_StoresHashedPassword() {
        when(mockRepo.findByLogin("est1")).thenReturn(Optional.empty());
        when(mockHasher.hash("Clave123!")).thenReturn("argon2$hash");

        userService.registerUser("est1", "Estudiante", "Clave123!", Role.ESTUDIANTE);

        verify(mockRepo).save(argThat(u -> u.getPassword().equals("argon2$hash") && !u.getPassword().equals("Clave123!")));
    }

    @Test
    void testAuthenticate_Success() {
        User mockUser = new User("docente1", "Docente", "hashed_pass", Role.DOCENTE, UserStatus.ACTIVO);
        when(mockRepo.findByLogin("docente1")).thenReturn(Optional.of(mockUser));
        when(mockHasher.verify("MiClave1!", "hashed_pass")).thenReturn(true);

        User result = userService.authenticate("docente1", "MiClave1!");
        assertEquals("docente1", result.getLogin());
    }
}