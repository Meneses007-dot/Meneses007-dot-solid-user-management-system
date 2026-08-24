# Requerimientos del Taller 2: Principios SOLID (Laboratorio Software II)

## 1. Requisitos Funcionales (40%)
- **Gestión de Usuarios:**
  - Registro de usuario con los campos: `username` (Login), `fullName`, `role`, `status` y `password`.
  - **Roles válidos:** Administrador, Autor de preguntas, Revisor, Docente, Estudiante.
  - **Estados válidos:** Activo, Inactivo.
  - **Reglas de contraseña:**
    - Mínimo 6 caracteres.
    - Al menos 1 dígito.
    - Al menos 1 mayúscula.
    - Al menos 1 carácter especial (`@#$%^&+=!`).
    - Almacenamiento obligatorio en base de datos de forma cifrada (Argon2).
- **Autenticación e Interfaz:**
  - Vista de Iniciar Sesión (`LoginView`).
  - Carga de menú/tablero (`DashboardView`) ajustado según el rol del usuario tras la autenticación.

## 2. Arquitectura y Principios SOLID (40%)
- Estructura basada en el **Principio de Inversión de Dependencias (DIP)** según Robert C. Martin.
- La capa de servicio/negocio (`UserService`) debe depender exclusivamente de **abstracciones** (`UserRepository`, `PasswordHasher`), jamás de implementaciones concretas.
- Persistencia en **SQLite** (archivo físico `users.db` o en memoria) mediante JDBC.
- Inyección manual de dependencias en el punto de entrada (`ui.Main`).

## 3. Pruebas Unitarias Automatizadas (20%)
- Pruebas unitarias completas a la capa de dominio y servicios (`UserServiceTest`).
- Uso de JUnit 5 y Mockito para simular el comportamiento de las abstracciones.

## 4. Criterios de Entrega
- Repositorio Git funcional ejecutado por consola.
- Proyecto compilable y ejecutable mediante Maven (`mvn compile`, `mvn test`, `mvn exec:java`).