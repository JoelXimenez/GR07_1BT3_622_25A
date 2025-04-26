package org.apptrueque.servlet.apptrueque;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import org.apptrueque.model.Usuario;

import java.io.IOException;

@WebServlet("/signup")  // Esta URL debe coincidir con el action del formulario JSP
public class SignupServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Recoger los datos del formulario
        String cedula = request.getParameter("cedula");
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Verificar si ya existe un usuario con el mismo correo electrónico
        Usuario usuarioExistente = Usuario.obtenerPorEmail(email);

        if (usuarioExistente != null) {
            // Si el correo electrónico ya está registrado, mostrar un mensaje de error
            String errorMessage = "El correo electrónico ya está registrado. Por favor, intenta con otro.";
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("/signup.jsp").forward(request, response);  // Redirigir de nuevo a signup.jsp
        } else {
            // Crear un nuevo objeto Usuario
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setCedula(cedula);
            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setEmail(email);
            nuevoUsuario.setPassword(password); // ⚡ Opcional: encriptar la contraseña

            try {
                // Registrar el usuario (guardar en base de datos)
                Usuario.registrar(nuevoUsuario);  // Aquí guardamos el usuario

                // Mostrar mensaje de éxito en la respuesta
                request.setAttribute("mensaje", "Usuario registrado exitosamente.");
                request.getRequestDispatcher("login.jsp").forward(request, response);  // Redirigir a login.jsp con mensaje

            } catch (Exception e) {
                e.printStackTrace();
                // En caso de error, redirigir a una página de error (opcional)
                request.setAttribute("error", "Hubo un error al registrar el usuario.");
                request.getRequestDispatcher("error.jsp").forward(request, response);  // Redirigir a error.jsp
            }
        }
    }

}
