package org.apptrueque.servlet.apptrueque;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import org.apptrueque.model.Usuario;
import org.apptrueque.model.Closet;

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

        // Verificar si la cédula o el correo ya existen
        if (usuarioExistentePorEmail(email)) {
            mostrarErrorRegistro(request, response, "El correo electrónico ya está registrado. Por favor, intenta con otro.");
        } else if (usuarioExistentePorCedula(cedula)) {
            mostrarErrorRegistro(request, response, "La cédula ya está registrada. Por favor, intenta con otra.");
        } else {
            registrarUsuario(cedula, nombre, email, password, request, response);
        }
    }

    private boolean usuarioExistentePorEmail(String email) {
        Usuario usuarioExistente = Usuario.obtenerPorEmail(email);
        return usuarioExistente != null;
    }

    private boolean usuarioExistentePorCedula(String cedula) {
        Usuario usuarioExistente = Usuario.obtenerPorCedula(cedula);
        return usuarioExistente != null;
    }

    private void mostrarErrorRegistro(HttpServletRequest request, HttpServletResponse response, String errorMessage)
            throws ServletException, IOException {
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("/signup.jsp").forward(request, response);
    }

    private void registrarUsuario(String cedula, String nombre, String email, String password,
                                  HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Crear usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setCedula(cedula);
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPassword(password);

        // Crear closet y vincular ambos lados de la relación
        Closet closet = new Closet();
        closet.setUsuario(nuevoUsuario);              // Closet conoce a su dueño
        nuevoUsuario.setClosetActual(closet);         // Usuario conoce su closet

        try {
            Usuario.registrar(nuevoUsuario);          // Guarda en BD
            request.setAttribute("mensaje", "Usuario registrado exitosamente.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Hubo un error al registrar el usuario.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
