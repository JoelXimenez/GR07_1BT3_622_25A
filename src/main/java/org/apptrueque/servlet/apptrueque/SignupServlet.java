package org.apptrueque.servlet.apptrueque;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import org.apptrueque.model.Usuario;

import java.io.IOException;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        procesarRegistro(request, response);
    }

    private void procesarRegistro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cedula = request.getParameter("cedula");
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (usuarioExistente(email)) {
            mostrarErrorRegistro(request, response);
        } else {
            registrarUsuario(cedula, nombre, email, password, request, response);
        }
    }

    private boolean usuarioExistente(String email) {
        Usuario usuarioExistente = Usuario.obtenerPorEmail(email);
        return usuarioExistente != null;
    }

    private void mostrarErrorRegistro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String errorMessage = "El correo electrónico ya está registrado. Por favor, intenta con otro.";
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("/signup.jsp").forward(request, response);
    }

    private void registrarUsuario(String cedula, String nombre, String email, String password, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setCedula(cedula);
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPassword(password);

        try {
            Usuario.registrar(nuevoUsuario);
            request.setAttribute("mensaje", "Usuario registrado exitosamente.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Hubo un error al registrar el usuario.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
