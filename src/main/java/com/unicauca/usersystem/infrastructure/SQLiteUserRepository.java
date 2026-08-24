package com.unicauca.usersystem.infrastructure;

import com.unicauca.usersystem.abstractions.UserRepository;
import com.unicauca.usersystem.domain.User;
import com.unicauca.usersystem.domain.Role;
import com.unicauca.usersystem.domain.UserStatus;
import java.sql.*;
import java.util.Optional;

public class SQLiteUserRepository implements UserRepository {
    private String dbUrl;

    public SQLiteUserRepository(String dbUrl) {
        this.dbUrl = dbUrl;
        initDatabase();
    }

    private void initDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                     "login TEXT PRIMARY KEY, fullName TEXT, password TEXT, role TEXT, status TEXT)";
        try (Connection conn = DriverManager.getConnection(dbUrl); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void save(User user) {
        String sql = "INSERT OR REPLACE INTO users(login, fullName, password, role, status) VALUES(?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(dbUrl); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getLogin());
            pstmt.setString(2, user.getFullName());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getRole().name());
            pstmt.setString(5, user.getStatus().name());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public Optional<User> findByLogin(String login) {
        String sql = "SELECT * FROM users WHERE login = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new User(
                    rs.getString("login"), rs.getString("fullName"), rs.getString("password"),
                    Role.valueOf(rs.getString("role")), UserStatus.valueOf(rs.getString("status"))
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }
}