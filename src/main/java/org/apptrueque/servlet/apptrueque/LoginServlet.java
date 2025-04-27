package org.apptrueque.servlet.apptrueque;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import org.apptrueque.model.Usuario;

import java.io.IOException;

@WebServlet(name = "loginServlet", value = "/login-servlet")
public class LoginServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");

        // Obtenemos los parámetros del formulario
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Intentamos obtener el usuario de la base de datos
        Usuario usuario = Usuario.obtenerPorEmail(email);

        // Verificamos si el usuario existe y la contraseña es correcta
        if (usuario != null && usuario.getPassword().equals(password)) {
            // 🔥 Guardamos el usuario en sesión
            HttpSession session = request.getSession(true);
            session.setAttribute("usuario", usuario);

            // Redirigimos a la página principal
            response.sendRedirect("home.jsp");
        } else {
            // Si las credenciales son incorrectas, establecemos un mensaje de error
            String errorMessage = "Credenciales incorrectas. Intenta de nuevo.";

            // Enviamos el mensaje de error a la página de login
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
