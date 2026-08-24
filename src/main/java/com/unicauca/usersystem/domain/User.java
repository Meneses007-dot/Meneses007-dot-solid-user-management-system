package com.unicauca.usersystem.domain;

public class User {
    private String login;
    private String fullName;
    private String password;
    private Role role;
    private UserStatus status;

    public User(String login, String fullName, String password, Role role, UserStatus status) {
        this.login = login;
        this.fullName = fullName;
        this.password = password;
        this.role = role;
        this.status = status;
    }
    public String getLogin() { return login; }
    public String getFullName() { return fullName; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
    public UserStatus getStatus() { return status; }
}