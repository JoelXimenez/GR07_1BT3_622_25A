package org.apptrueque.servlet.apptrueque;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.apptrueque.model.Usuario;
import org.apptrueque.util.JpaUtil;

import java.io.IOException;

import jakarta.persistence.EntityManager;

@WebServlet(name = "editarPerfil", value = "/editar-perfil")
public class EditarPerfilServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario datosActualizados = recolectarDatosFormulario(request);

        actualizarPerfil(datosActualizados, request, response);
    }

    // Recolectar los datos del formulario
    private Usuario recolectarDatosFormulario(HttpServletRequest request) {
        Usuario usuario = new Usuario();
        usuario.setCedula(request.getParameter("cedula"));
        usuario.setNombre(request.getParameter("nombre"));
        usuario.setEmail(request.getParameter("email"));
        usuario.setPassword(request.getParameter("password"));
        return usuario;
    }

    // Método que realiza la lógica de actualización
    private void actualizarPerfil(Usuario nuevosDatos, HttpServletRequest request, HttpServletResponse response) throws IOException {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManagerFactory().createEntityManager();
            em.getTransaction().begin();

            Usuario usuarioBD = em.find(Usuario.class, nuevosDatos.getCedula());
            if (usuarioBD != null) {
                // Solo actualiza los campos que no estén vacíos
                if (nuevosDatos.getNombre() != null && !nuevosDatos.getNombre().trim().isEmpty()) {
                    usuarioBD.setNombre(nuevosDatos.getNombre());
                }

                if (nuevosDatos.getEmail() != null && !nuevosDatos.getEmail().trim().isEmpty()) {
                    usuarioBD.setEmail(nuevosDatos.getEmail());
                }

                if (nuevosDatos.getPassword() != null && !nuevosDatos.getPassword().trim().isEmpty()) {
                    usuarioBD.setPassword(nuevosDatos.getPassword());
                }

                em.merge(usuarioBD);
                em.getTransaction().commit();

                // Refresca el usuario en sesión
                request.getSession().setAttribute("usuario", usuarioBD);
            }

            response.sendRedirect("perfil.jsp"); // Asegúrate que este es el JSP correcto
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            response.sendRedirect("login.jsp");
        } finally {
            if (em != null) em.close();
        }
    }

}

