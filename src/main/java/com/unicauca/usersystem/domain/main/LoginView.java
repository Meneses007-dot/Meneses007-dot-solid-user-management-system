package com.unicauca.usersystem.domain.main;

import com.unicauca.usersystem.domain.User;
import com.unicauca.usersystem.domain.service.UserService;
import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    private final UserService userService;
    public LoginView(UserService userService) {
        this.userService = userService;
        setTitle("Iniciar Sesión");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2));
        
        add(new JLabel("Login:")); JTextField txtLogin = new JTextField(); add(txtLogin);
        add(new JLabel("Password:")); JPasswordField txtPass = new JPasswordField(); add(txtPass);
        
        JButton btnLogin = new JButton("Entrar");
        JButton btnRegister = new JButton("Registrarse");
        add(btnLogin); add(btnRegister);

        btnLogin.addActionListener(e -> {
            try {
                User u = userService.authenticate(txtLogin.getText(), new String(txtPass.getPassword()));
                dispose();
                new DashboardView(u, userService).setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnRegister.addActionListener(e -> new RegisterView(userService).setVisible(true));
    }
}
