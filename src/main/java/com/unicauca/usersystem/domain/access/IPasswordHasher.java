package com.unicauca.usersystem.domain.access;

public interface IPasswordHasher {
    String hash(String plainPassword);
    boolean verify(String plainPassword, String hashedPassword);
}
