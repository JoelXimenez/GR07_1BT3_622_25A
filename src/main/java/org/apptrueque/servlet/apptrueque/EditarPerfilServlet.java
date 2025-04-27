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
        String cedula = request.getParameter("cedula");
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManagerFactory().createEntityManager();
            em.getTransaction().begin();

            Usuario usuario = em.find(Usuario.class, cedula);
            if (usuario != null) {
                usuario.setNombre(nombre);
                usuario.setEmail(email);
                usuario.setPassword(password);

                em.merge(usuario); // Actualiza el usuario
                em.getTransaction().commit();

                // Actualizar el usuario en sesión también
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);
            }

            response.sendRedirect("perfil.jsp");
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
