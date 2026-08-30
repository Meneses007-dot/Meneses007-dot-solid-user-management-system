package com.unicauca.usersystem.domain.access;

import com.unicauca.usersystem.domain.Role;
import com.unicauca.usersystem.domain.User;
import com.unicauca.usersystem.domain.UserStatus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class SQLiteUserRepository implements UserRepository {

    private final String jdbcUrl;

    public SQLiteUserRepository(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
        initTable();
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(jdbcUrl);
    }

    private void initTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                    login     TEXT PRIMARY KEY,
                    full_name TEXT NOT NULL,
                    password  TEXT NOT NULL,
                    role      TEXT NOT NULL,
                    status    TEXT NOT NULL
                )
                """;
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error al inicializar la base de datos", e);
        }
    }

    @Override
    public void save(User user) {
        String sql = """
                INSERT INTO users (login, full_name, password, role, status)
                VALUES (?, ?, ?, ?, ?)
                ON CONFLICT(login) DO UPDATE SET
                    full_name = excluded.full_name,
                    password  = excluded.password,
                    role      = excluded.role,
                    status    = excluded.status
                """;
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getFullName());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole().name());
            ps.setString(5, user.getStatus().name());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el usuario", e);
        }
    }

    @Override
    public Optional<User> findByLogin(String login) {
        String sql = "SELECT login, full_name, password, role, status FROM users WHERE login = ?";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                            rs.getString("login"),
                            rs.getString("full_name"),
                            rs.getString("password"),
                            Role.valueOf(rs.getString("role")),
                            UserStatus.valueOf(rs.getString("status"))
                    );
                    return Optional.of(user);
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el usuario", e);
        }
    }
}
