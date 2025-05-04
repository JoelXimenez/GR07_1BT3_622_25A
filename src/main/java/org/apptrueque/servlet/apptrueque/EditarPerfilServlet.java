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
                usuarioBD.setNombre(nuevosDatos.getNombre());
                usuarioBD.setEmail(nuevosDatos.getEmail());
                usuarioBD.setPassword(nuevosDatos.getPassword());

                em.merge(usuarioBD);
                em.getTransaction().commit();

                request.getSession().setAttribute("usuario", usuarioBD);
            }

            response.sendRedirect("perfil.jsp");
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        } finally {
            if (em != null) em.close();
        }
    }
}

