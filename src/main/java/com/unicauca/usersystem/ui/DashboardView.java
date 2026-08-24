package com.unicauca.usersystem.ui;

import com.unicauca.usersystem.domain.Role;
import com.unicauca.usersystem.domain.User;
import com.unicauca.usersystem.service.UserService;
import javax.swing.*;
import java.awt.*;

public class DashboardView extends JFrame {
    private final User user;
    private final UserService userService;

    public DashboardView(User user, UserService userService) {
        this.user = user;
        this.userService = userService;
        setTitle("Tablero - " + user.getRole());
        setSize(450, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel lblWelcome = new JLabel("Bienvenido " + user.getFullName() + " | Rol: " + user.getRole(), SwingConstants.CENTER);
        add(lblWelcome, BorderLayout.CENTER);

        buildMenuBar();
    }

    private void buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Opciones");

        switch (user.getRole()) {
            case ADMINISTRADOR -> {
                menu.add(item("Gestionar usuarios", () -> info("Administrar usuarios del sistema.")));
                menu.add(item("Ver reportes", () -> info("Reportes del sistema.")));
            }
            case AUTOR -> menu.add(item("Crear pregunta", () -> info("Formulario de creación de preguntas.")));
            case REVISOR -> menu.add(item("Revisar preguntas", () -> info("Cola de revisión de preguntas.")));
            case DOCENTE -> {
                menu.add(item("Gestionar evaluaciones", () -> info("Administración de evaluaciones.")));
                menu.add(item("Ver estudiantes", () -> info("Listado de estudiantes.")));
            }
            case ESTUDIANTE -> menu.add(item("Responder cuestionarios", () -> info("Cuestionarios disponibles.")));
        }

        JMenuItem mntmLogout = new JMenuItem("Cerrar sesión");
        mntmLogout.addActionListener(e -> {
            dispose();
            new LoginView(userService).setVisible(true);
        });

        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    private JMenuItem item(String label, Runnable action) {
        JMenuItem it = new JMenuItem(label);
        it.addActionListener(e -> action.run());
        return it;
    }

    private void info(String msg) {
        JOptionPane.showMessageDialog(this, msg, user.getRole().name(), JOptionPane.INFORMATION_MESSAGE);
    }
}
