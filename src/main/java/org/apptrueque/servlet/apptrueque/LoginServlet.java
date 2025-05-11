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
        procesarLogin(request, response);
    }

    private void procesarLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = obtenerEmail(request);
        String password = obtenerPassword(request);

        Usuario usuario = validarUsuario(email, password);

        if (usuario != null) {
            iniciarSesion(request, usuario);
            response.sendRedirect("home.jsp");
        } else {
            mostrarError(request, response);
        }
    }

    private String obtenerEmail(HttpServletRequest request) {
        return request.getParameter("email");
    }

    private String obtenerPassword(HttpServletRequest request) {
        return request.getParameter("password");
    }

    private Usuario validarUsuario(String email, String password) {
        Usuario usuario = Usuario.obtenerPorEmail(email);
        return (usuario != null && usuario.getPassword().equals(password)) ? usuario : null;
    }

    private void iniciarSesion(HttpServletRequest request, Usuario usuario) {
        HttpSession session = request.getSession(true);
        session.setAttribute("usuario", usuario);
        session.setAttribute("usuarioEmail", usuario.getEmail()); // ← Esta línea es clave
    }


    private void mostrarError(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String errorMessage = "Credenciales incorrectas. Intenta de nuevo.";
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}
