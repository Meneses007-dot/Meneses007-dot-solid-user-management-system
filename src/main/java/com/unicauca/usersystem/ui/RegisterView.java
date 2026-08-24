package com.unicauca.usersystem.ui;

import com.unicauca.usersystem.domain.Role;
import com.unicauca.usersystem.service.UserService;
import javax.swing.*;
import java.awt.*;

public class RegisterView extends JFrame {
    public RegisterView(UserService userService) {
        setTitle("Registro de Usuario");
        setSize(350, 280);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2));

        add(new JLabel("Login:")); JTextField txtLogin = new JTextField(); add(txtLogin);
        add(new JLabel("Nombre completo:")); JTextField txtFullName = new JTextField(); add(txtFullName);
        add(new JLabel("Password:")); JPasswordField txtPass = new JPasswordField(); add(txtPass);
        add(new JLabel("Rol:")); JComboBox<Role> cmbRole = new JComboBox<>(Role.values()); add(cmbRole);

        JButton btnRegister = new JButton("Crear cuenta");
        JButton btnCancel = new JButton("Cancelar");
        add(btnRegister); add(btnCancel);

        btnRegister.addActionListener(e -> {
            try {
                userService.registerUser(
                    txtLogin.getText().trim(),
                    txtFullName.getText().trim(),
                    new String(txtPass.getPassword()),
                    (Role) cmbRole.getSelectedItem()
                );
                JOptionPane.showMessageDialog(this,
                    "Usuario registrado con éxito.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al registrar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> dispose());
    }
}
